package com.ticketclever.go.timerservice.streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ActivationEventStream {

    String ACTIVATION = "Activation";
    String ACTIVATION_CANCELLED = "ActivationCancelled";
    String ALLOCATABLE_TICKET_EVENT = "AllocatableTicketEvent";

    /**
     * Origin: JourneyActivationService
     * @return SubscibableChannel channel
     */
    @Input(ActivationEventStream.ACTIVATION)
    SubscribableChannel newActivation();

    @Input(ActivationEventStream.ACTIVATION_CANCELLED)
    SubscribableChannel activationCancelled();

    /**
     * Origin: JourneyActivationService
     * @return SubscibableChannel channel
     */
    @Output(ActivationEventStream.ALLOCATABLE_TICKET_EVENT)
    MessageChannel committedActivation();

}
