package com.ticketclever.go.timerservice.services;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.ticketclever.go.timerservice.api.Activation;

import com.ticketclever.go.timerservice.api.AllocatableTicketDetails;


import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ticketclever.go.timerservice.model.ActivationTimerState.create;

/**
 * This class manages the timer actor lifecycle
 */
public class TimerManager extends AbstractActorWithStash {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerManager.class);

    private final Duration timerDuration;
    private final Consumer<AllocatableTicketDetails> broker;
    private final Long tickSlice;

    private ActorRef timerResponseBroker;

    public static Props properties(final Consumer<AllocatableTicketDetails> broker, final Long tickSlice, final Duration timerDuration) {
        return Props.create(TimerManager.class, broker, tickSlice, timerDuration);
    }

    private TimerManager(final Consumer<AllocatableTicketDetails> broker, final Long tickSlice, final Duration timerDuration) {
        this.broker = broker;
        this.tickSlice = tickSlice;
        this.timerDuration = timerDuration;
    }

//    @Override
//    public void preStart() {
//        this.timerResponseBroker = getContext().watch(getContext().actorOf(TimerResponseBroker.properties(this.broker), "response-broker"));
//    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(Activation.class, this::receiveMessage)
                .match(AllocatableTicketDetails.class, this::sendMessage)
                .build();
    }

    private void receiveMessage(final Activation activation) {
        ActorRef timer = getContext().watch(getContext().actorOf(EventTimer.properties(this.tickSlice), activation.getJourneyId()));
        LOGGER.info("Submitted timer [{}]", timer.path().name());
        getSender().tell(create().data(activation).start(activation.getActivationTime()).finish(activation.getActivationTime().plus(this.timerDuration)).duration(this.timerDuration).elapsed(0L),
                getSelf());
    }

    private void sendMessage(final AllocatableTicketDetails ticketDetails) {
        Optional.ofNullable(this.timerResponseBroker).ifPresent(actor -> actor.forward(ticketDetails, getContext()));
    }

}