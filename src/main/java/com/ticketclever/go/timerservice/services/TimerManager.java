package com.ticketclever.go.timerservice.services;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.ticketclever.go.timerservice.api.Activation;

import com.ticketclever.go.timerservice.model.ActivationTimerState;
import com.ticketclever.go.timerservice.model.EventTimer;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ticketclever.go.timerservice.model.ActivationTimerState.create;

/**
 * This class manages the timer actor lifecycle
 */
public class TimerManager extends AbstractActorWithStash {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerManager.class);

    private final TimerRequestBroker broker;
    private static final long tickSlice = 20;

    public static Props properties(final TimerRequestBroker broker) {
        return Props.create(TimerManager.class, broker);
    }

    private TimerManager(final TimerRequestBroker broker) {
     this.broker = broker;
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(Activation.class, this::receiveMessage)
                .build();
    }

    private void receiveMessage(final Activation activation) {
        ActivationTimerState state = create().data(activation).start(activation.getActivationTime()).finish(activation.getActivationTime().plusMinutes(20)).duration(Duration.of(20, ChronoUnit.MINUTES)).elapsed(0L);
        ActorRef timer = getContext().watch(getContext().actorOf(EventTimer.properties(this.tickSlice), activation.getJourneyId()));
        LOGGER.info("Submitted timer [{}]", timer.path().name());
        getSender().tell(state, getSelf());
    }


}