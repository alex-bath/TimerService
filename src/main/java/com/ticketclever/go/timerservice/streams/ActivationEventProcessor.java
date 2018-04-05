package com.ticketclever.go.timerservice.streams;

import com.ticketclever.go.timerservice.api.Activation;
import com.ticketclever.go.timerservice.api.JourneyAbandonmentEvent;
import com.ticketclever.go.timerservice.services.TimerRequestBroker;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(ActivationEventStream.class)
public class ActivationEventProcessor {

    private static final Logger LOGGER = LogManager.getLogger();

    private final TimerRequestBroker timerRequestBroker;

    @Inject
    public ActivationEventProcessor(final TimerRequestBroker timerRequestBroker) {
        this.timerRequestBroker = timerRequestBroker;
    }

    @StreamListener(ActivationEventStream.ACTIVATION)
    public void newActivation(Activation event) throws ExecutionException, InterruptedException {
        String journeyId = Optional.ofNullable(event.getJourneyId()).map(journey -> journey.isEmpty() ? null : journey).orElseThrow(() -> {
            LOGGER.error("Error processing committed activation for identity [{}]: No journeyId", event.getIdentityId());
            return new IllegalStateException(String.format("No JourneyId on activation for identity [%s]", event.getIdentityId()));
        });

        LOGGER.info("New Activation event for journey [{}]", journeyId);

        if(!Optional.ofNullable(this.timerRequestBroker.receiveEvent(event)).isPresent())
            LOGGER.info("Unable to process New Activation event for journey [{}]", journeyId);
    }

    @StreamListener(ActivationEventStream.ACTIVATION_CANCELLED)
    public void activationCancelled(JourneyAbandonmentEvent event) throws ExecutionException, InterruptedException {
        String journeyId = Optional.ofNullable(event.getJourneyId()).map(journey -> journey.isEmpty() ? null : journey).orElseThrow(() -> {
            LOGGER.error("Error processing activation cancellation for journey, no journeyId");
            return new IllegalStateException("no journeyId for activation cancellation");
        });

        LOGGER.info("New JourneyAbandonmentEvent event for journey [{}]", journeyId);

        this.timerRequestBroker.receiveEvent(event);
    }

}