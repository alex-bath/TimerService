package com.ticketclever.go.timerservice.services;

import akka.actor.AbstractActorWithStash;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.ticketclever.go.timerservice.api.AllocatableTicketDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerResponseBroker extends AbstractActorWithStash {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerManager.class);

    private final TimerRequestBroker broker;

    private TimerResponseBroker(final TimerRequestBroker broker) {
        this.broker = broker;
    }

    public static Props properties(final TimerRequestBroker broker) {
        return Props.create(TimerManager.class, broker);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(AllocatableTicketDetails.class, this::send)
                .build();
    }

    public void send(final AllocatableTicketDetails ticket) {
        LOGGER.info("Sending message {}", ticket);
        this.broker.publishEvent(ticket);
    }

}