package com.ticketclever.go.timerservice.services;

import akka.Done;
import akka.actor.AbstractActorWithStash;
import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Identify;
import akka.actor.InvalidActorNameException;
import akka.actor.Props;
import akka.japi.Pair;
import akka.japi.pf.ReceiveBuilder;
import com.ticketclever.go.timerservice.api.Activation;

import com.ticketclever.go.timerservice.api.AllocatableTicketDetails;

import com.ticketclever.go.timerservice.api.JourneyAbandonmentEvent;
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

    @Override
    public void preStart() {
        this.timerResponseBroker = getContext().watch(getContext().actorOf(TimerResponseBroker.properties(this.broker), "response-broker"));
        LOGGER.info("Started Actor [{}]", getSelf().path());
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(Activation.class, this::receiveMessage)
                .match(AllocatableTicketDetails.class, this::sendMessage)
                .match(JourneyAbandonmentEvent.class, this::cancelMessage)
                .match(ActorIdentity.class, id -> !id.getActorRef().isPresent(), id -> this.cancelMessage((Pair<ActorRef, JourneyAbandonmentEvent>) id.correlationId()))
                .match(ActorIdentity.class, id -> id.getActorRef().isPresent(), this::cancelMessage)
                .build();
    }

    private void receiveMessage(final Activation activation) {
        try {
            final ActorRef timer = getContext().watch(getContext().actorOf(EventTimer.properties(this.tickSlice), activation.getJourneyId()));
            Optional.ofNullable(create().data(activation).start(activation.getActivationTime()).finish(activation.getActivationTime().plus(this.timerDuration)).duration(this.timerDuration).elapsed(0L))
                    .ifPresent(state -> {
                        timer.tell(state, getSelf());
                        getSender().tell(state, getSelf());
                    });
            LOGGER.info("Submitted timer [{}]", timer.path());
        } catch (InvalidActorNameException e) {
            LOGGER.error("A Timer already exists for this journey reference [{}]: {}", activation.getJourneyId(), e.message());
        } catch (Exception f) {
            LOGGER.error("Error during timer creation for [{}]: {} ", activation.getJourneyId(), f.getMessage());
        }
    }

    private void sendMessage(final AllocatableTicketDetails ticketDetails) {
        Optional.ofNullable(this.timerResponseBroker).ifPresent(actor -> actor.forward(ticketDetails, getContext()));
        LOGGER.info("Timer has expired for journey [{}]", ticketDetails.getJourneyId());
    }

    private void cancelMessage(final JourneyAbandonmentEvent abandonmentEvent) {
        final ActorSelection selection = getContext().actorSelection(getSelf().path().toStringWithoutAddress().concat("/").concat(abandonmentEvent.getJourneyId()));
        selection.tell(new Identify(Pair.create(getSender(), abandonmentEvent)), getSelf());
    }

    private void cancelMessage(final ActorIdentity id) {
        id.getActorRef().ifPresent(actor -> {
            getContext().stop(actor);
            LOGGER.info("Sent cancellation request to timer [{}]", actor.path().name());
        });
        ((Pair<ActorRef, JourneyAbandonmentEvent>) id.correlationId()).first().tell(Done.getInstance(), getSelf());
    }

    private void cancelMessage(final Pair<ActorRef, JourneyAbandonmentEvent> correlationId) {
        LOGGER.warn("Timer not found for journey [{}]", correlationId.second().getJourneyId());
        correlationId.first().tell(Done.getInstance(), getSelf());
    }

}