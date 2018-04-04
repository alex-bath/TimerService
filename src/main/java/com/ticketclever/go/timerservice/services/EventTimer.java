package com.ticketclever.go.timerservice.services;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Pair;
import akka.japi.pf.ReceiveBuilder;
import com.ticketclever.go.timerservice.model.ActivationTimerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.FiniteDuration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class EventTimer extends AbstractActorWithTimers {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventTimer.class);

    private static final int JITTER_LWM = 100;
    private static final int JITTER_HWM = 200;

    private final long slice;
    private Pair<ActivationTimerState, Long> last = null;
    private Supplier<LocalDateTime> dateTimeSupplier = () -> LocalDateTime.now();

    public EventTimer(final long slice) {
        this.slice = slice;
    }

    public static Props properties(final long slice) {
        return Props.create(EventTimer.class, slice);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ActivationTimerState.class, this::schedule)
                .match(ActorRef.class, this::currentState)
                .build();
    }

    @Override
    public void postStop() {
        LOGGER.info("Stopping timer [{}]", getSelf().path().name());
    }

    protected ActivationTimerState schedule(final ActivationTimerState state) {
        if (getTimers().isTimerActive(state.data.getJourneyId())) getTimers().cancel(state.data.getJourneyId());

        this.last = advance(state);
        if (this.last.second() < 0) {
            getContext().getParent().tell(state.data.toAllocatableTicketDetails(), getSelf());
            getContext().stop(getSelf());
            return this.last.first();
        }

        LOGGER.info("Scheduling timer of {} millis with {}", last.second(), last.first());

        getTimers().startSingleTimer(state.data.getJourneyId(), last.first(), FiniteDuration.create(last.second(), TimeUnit.MILLISECONDS));
        return this.last.first();
    }

    private Pair<ActivationTimerState, Long> advance(final ActivationTimerState state) {
        final long step = state.duration.dividedBy(this.slice).toMillis();
        final long remaining = Duration.between(this.dateTimeSupplier.get(), state.finish).toMillis();
        final long next = this.slice - Math.floorDiv(remaining, step);
        final long time = remaining % step;
        final long finalTime = (time < JITTER_LWM && step > JITTER_HWM) ? ((next + 1 > this.slice) ? -1 : step + time) : time;

        return Pair.create(ActivationTimerState.advance(state, finalTime >= step ? next + 1 : next), finalTime);
    }

    private void currentState(final ActorRef target) {
        Optional.ofNullable(target).ifPresent(actor -> target.tell(Optional.ofNullable(this.last).orElse(Pair.create(null, 0L)), getSelf()));
    }

}