package com.ticketclever.go.timerservice.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.ticketclever.go.timerservice.api.Activation;
import com.ticketclever.go.timerservice.api.AllocatableTicketDetails;
import com.ticketclever.go.timerservice.api.JourneyAbandonmentEvent;
import com.ticketclever.go.timerservice.services.TimerManager;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class TimerManagerTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("timer-system");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void createTimerTest() {
        new TestKit(system) {{
            final TestKit probe = new TestKit(system);
            final ActorRef subject = probe.childActorOf(TimerManager.properties(ticket ->
                    probe.getRef().tell(ticket, ActorRef.noSender()), 2L, Duration.ofSeconds(2)));

            final Activation activation = new Activation();
            activation.setJourneyId(UUID.randomUUID().toString());
            activation.setActivationTime(LocalDateTime.now());

            within(duration("6 seconds"), () -> {
                subject.tell(activation, ActorRef.noSender());

                AllocatableTicketDetails ticket = probe.expectMsgClass(duration("6 seconds"), AllocatableTicketDetails.class);

                assertThat("AllocatableTicketDetails not emitted", ticket, is(not(nullValue())));

                return null;
            });
        }};
    }

    @Test
    public void duplicateTimerNameTest() {
        new TestKit(system) {{
            final TestKit probe = new TestKit(system);
            final ActorRef subject = probe.childActorOf(TimerManager.properties(ticket ->
                    probe.getRef().tell(ticket, ActorRef.noSender()), 4L, Duration.ofSeconds(4)));

            final Activation activation = new Activation();
            activation.setJourneyId(UUID.randomUUID().toString());
            activation.setActivationTime(LocalDateTime.now());

            within(duration("6 seconds"), () -> {
                subject.tell(activation, ActorRef.noSender());
                activation.setActivationTime(LocalDateTime.now().plus(10, ChronoUnit.MILLIS));
                subject.tell(activation, ActorRef.noSender());

                AllocatableTicketDetails ticket = probe.expectMsgClass(duration("6 seconds"), AllocatableTicketDetails.class);

                assertThat("AllocatableTicketDetails not emitted", ticket, is(not(nullValue())));
                assertThat("There are remaining messages", probe.msgAvailable(), is(false));

                return null;
            });
        }};
    }

    @Test
    public void cancelExistingTimerTest() {
        new TestKit(system) {{
            final TestKit probe = new TestKit(system);
            final ActorRef subject = probe.childActorOf(TimerManager.properties(ticket ->
                    probe.getRef().tell(ticket, ActorRef.noSender()), 2L, Duration.ofSeconds(2)), "timers");

            final Activation activation = new Activation();
            activation.setJourneyId(UUID.randomUUID().toString());
            activation.setActivationTime(LocalDateTime.now());

            final JourneyAbandonmentEvent abandon = new JourneyAbandonmentEvent(activation.getJourneyId(), Instant.now());

            within(duration("6 seconds"), () -> {
                subject.tell(activation, ActorRef.noSender());

                subject.tell(abandon, ActorRef.noSender());

                probe.expectNoMessage(duration("4 seconds"));

                assertThat("There are remaining messages", probe.msgAvailable(), is(false));

                return null;
            });
        }};
    }
}