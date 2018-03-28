package com.ticketclever.go.timerservice.services;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import com.ticketclever.go.timerservice.api.Activation;
import com.ticketclever.go.timerservice.api.AllocatableTicketDetails;
import com.ticketclever.go.timerservice.model.ActivationTimerState;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import static akka.pattern.PatternsCS.ask;

@Service
@ConfigurationProperties("timerservice")
public class TimerRequestBroker {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerRequestBroker.class);

    private ActorSystem system;
    private ActorRef manager;

    @Value("#{T(java.time.Duration).parse('${timerservice.eventtimer.duration}')}")
    private Duration timerDuration;

    @Value("${timerservice.eventtimer.tickslice}")
    private Long tickSlice;

    public TimerRequestBroker() {

    }

    @PostConstruct
    public void initialiseAkkaSystem() {
        this.system = ActorSystem.create("timer-system");
        LOGGER.info("Started Akka System [{}]", this.system.name());

        try {
            Optional.ofNullable(this.system).ifPresent(system -> this.manager = system.actorOf(TimerManager.properties(this::publishEvent, this.tickSlice, this.timerDuration), "timers"));
        } catch (Exception e) {
            LOGGER.info("Terminating Akka System [{}] due to exception: ", this.system.name(), e.getMessage());
            Optional.ofNullable(this.system).ifPresent(system -> system.terminate());
            this.system = null;
        }

    }

    @PreDestroy
    public void terminateAkkaSystem() {
        Optional.ofNullable(this.system).ifPresent(system -> system.terminate());
    }

    public ActivationTimerState receiveEvent(final Activation activation) throws ExecutionException, InterruptedException {
        CompletionStage<Object> submitted = ask(this.manager, activation, new Timeout(scala.concurrent.duration.Duration.create(200, TimeUnit.MILLISECONDS)));
        return (ActivationTimerState) submitted.handle((obj, err) -> {
            if (Optional.ofNullable(err).isPresent()) {
                LOGGER.error("Unable to submit timer for Activation: {}", activation);
                LOGGER.error("Submission failure: {}", err.getMessage());
                return err;
            }
            return obj;
        }).toCompletableFuture().get();
    }

    public void publishEvent(final AllocatableTicketDetails details) {

    }
}