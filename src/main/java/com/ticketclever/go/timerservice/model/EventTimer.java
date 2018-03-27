package com.ticketclever.go.timerservice.model;

import akka.actor.AbstractActorWithTimers;
import akka.actor.Props;
import akka.japi.Pair;
import akka.japi.pf.ReceiveBuilder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.FiniteDuration;

public class EventTimer extends AbstractActorWithTimers {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventTimer.class);

    private final long slice;
    private Supplier<LocalDateTime> dateTimeSupplier = () -> LocalDateTime.now();

    private EventTimer(final long slice) {
        this.slice = slice;
    }

    public static Props properties(final long slice) {
        return Props.create(EventTimer.class, slice);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ActivationTimerState.class, this::schedule)
                .build();
    }

    private void schedule(final ActivationTimerState state) {
        if(getTimers().isTimerActive(state.data.getJourneyId())) getTimers().cancel(state.data.getJourneyId());

        Pair<ActivationTimerState, Long> newState = advance(state);
        if(newState.second() < 0) {
            getContext().getParent().tell(state.data.toAllocatableTicketDetails(), getSelf());
            getContext().stop(getSelf());
            return;
        }

        LOGGER.info("Scheduling timer of {} millis with {}", newState.second(), newState.first());

        getTimers().startSingleTimer(state.data.getJourneyId(), newState.first(), FiniteDuration.create(newState.second(), TimeUnit.MILLISECONDS));
    }

    private Pair<ActivationTimerState, Long> advance(final ActivationTimerState state) {
        final long step = Duration.between(state.start, state.finish).dividedBy(this.slice).get(ChronoUnit.MILLIS);
        final long remaining = Duration.between(this.dateTimeSupplier.get(), state.finish).get(ChronoUnit.MILLIS);
        final long next = this.slice - Math.floorDiv(remaining, step);
        final long time = remaining % step;
        final long finalTime = (time < 100 && step > 200) ? ((next + 1 > this.slice) ? -1 : step + time) : time;

        return Pair.create(ActivationTimerState.advance(state, finalTime >= step ? next + 1 : next), finalTime);
    }

}