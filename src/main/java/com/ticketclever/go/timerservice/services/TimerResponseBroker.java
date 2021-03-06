package com.ticketclever.go.timerservice.services;

import akka.actor.AbstractActorWithStash;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.ticketclever.go.timerservice.api.AllocatableTicketDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class TimerResponseBroker extends AbstractActorWithStash {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerManager.class);

    private final Consumer<AllocatableTicketDetails> broker;

    private TimerResponseBroker(final Consumer<AllocatableTicketDetails> broker) {
        this.broker = broker;
    }

    public static Props properties(final Consumer<AllocatableTicketDetails> broker) {
        return Props.create(TimerResponseBroker.class, broker);
    }

    @Override
    public void preStart() {
        LOGGER.info("Started Actor [{}]", getSelf().path());
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(AllocatableTicketDetails.class, this::send)
                .build();
    }

    public void send(final AllocatableTicketDetails ticket) {
        LOGGER.info("Sending message {}", ticket);
        this.broker.accept(ticket);
    }

}