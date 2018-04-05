package com.ticketclever.go.timerservice.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.ticketclever.go.timerservice.api.Activation;
import com.ticketclever.go.timerservice.api.AllocatableTicketDetails;
import com.ticketclever.go.timerservice.model.ActivationTimerState;
import com.ticketclever.go.timerservice.services.EventTimer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class EventTimerTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void timingSequenceTest() {
        new TestKit(system) {{
            final TestKit probe = new TestKit(system);
            final class ScheduledMessage {
                public ActivationTimerState state;

                ScheduledMessage(ActivationTimerState state) {
                    this.state = state;
                }
            }

            final ActorRef target = system.actorOf(Props.create(EventTimer.class, () -> new EventTimer(10L) {
                @Override
                public ActivationTimerState schedule(final ActivationTimerState state) {
                    final ActivationTimerState newState = super.schedule(state);
                    probe.getRef().tell(new ScheduledMessage(newState), getSelf());
                    return newState;
                }
            }));
            final Activation activation = new Activation();
            activation.setJourneyId(UUID.randomUUID().toString());

            target.tell(ActivationTimerState.create()
                    .data(activation)
                    .start(LocalDateTime.now())
                    .finish(LocalDateTime.now().plus(10, ChronoUnit.SECONDS))
                    .duration(Duration.of(10L, ChronoUnit.SECONDS))
                    .elapsed(0L), ActorRef.noSender());
            ScheduledMessage subject = probe.expectMsgClass(ScheduledMessage.class);
            ScheduledMessage subject2 = probe.expectMsgClass(ScheduledMessage.class);
            ScheduledMessage subject3 = probe.expectMsgClass(ScheduledMessage.class);
            ScheduledMessage subject4 = probe.expectMsgClass(ScheduledMessage.class);
            ScheduledMessage subject5 = probe.expectMsgClass(ScheduledMessage.class);

            assertThat("Elapsed tick time not correct", subject.state.elapsed, is(equalTo(1L)));
            assertThat("Elapsed tick time not correct", subject2.state.elapsed, is(equalTo(2L)));
            assertThat("Elapsed tick time not correct", subject3.state.elapsed, is(equalTo(3L)));
            assertThat("Elapsed tick time not correct", subject4.state.elapsed, is(equalTo(4L)));
            assertThat("Elapsed tick time not correct", subject5.state.elapsed, is(equalTo(5L)));
        }};
    }

    @Test
    public void timerCompletionTest() {
        new TestKit(system) {{
            final TestKit probe = new TestKit(system);
            final ActorRef subject = probe.childActorOf(EventTimer.properties(5L));

            final Activation activation = new Activation();
            activation.setJourneyId(UUID.randomUUID().toString());

            within(duration("8 seconds"), () -> {
                subject.tell(ActivationTimerState.create()
                        .data(activation)
                        .start(LocalDateTime.now())
                        .finish(LocalDateTime.now().plus(5, ChronoUnit.SECONDS))
                        .duration(Duration.of(5L, ChronoUnit.SECONDS))
                        .elapsed(0L), ActorRef.noSender());

                final AllocatableTicketDetails ticket = probe.expectMsgClass(duration("8 seconds"), AllocatableTicketDetails.class);

                assertThat("AllocatableTicketDetails not emitted", ticket, is(not(nullValue())));
                return null;
            });
        }};
    }

    @Test
    public void timerDriftTest() {
        new TestKit(system) {
            {
                final TestKit probe = new TestKit(system);
                final ActorRef subject = probe.childActorOf(EventTimer.properties(5L));

                final Activation activation = new Activation();
                activation.setJourneyId(UUID.randomUUID().toString());

                within(duration("4 seconds"), () -> {
                    subject.tell(ActivationTimerState.create()
                            .data(activation)
                            .start(LocalDateTime.now().minus(4, ChronoUnit.SECONDS))
                            .finish(LocalDateTime.now().plus(1, ChronoUnit.SECONDS))
                            .duration(Duration.of(5L, ChronoUnit.SECONDS))
                            .elapsed(0L), ActorRef.noSender());

                    final AllocatableTicketDetails ticket = probe.expectMsgClass(duration("4 seconds"), AllocatableTicketDetails.class);

                    assertThat("AllocatableTicketDetails not emitted", ticket, is(not(nullValue())));
                    return null;
                });

            }
        };
    }

}