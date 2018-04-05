package com.ticketclever.go.timerservice.streams;

import com.ticketclever.go.timerservice.api.AllocatableTicketDetails;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(ActivationEventStream.class)
public class ActivationEventPublisher {

    private static final Logger LOGGER = LogManager.getLogger();

    private final ActivationEventStream activationEventStream;

    @Inject
    public ActivationEventPublisher(final ActivationEventStream activationEventStream){
        this.activationEventStream = activationEventStream;
    }

    public boolean committedActivation(final AllocatableTicketDetails event) {
        LOGGER.info("Sending AllocatableTicketDetails event: {}", event);
        return this.activationEventStream.committedActivation().send(MessageBuilder.withPayload(event).build());
    }

}