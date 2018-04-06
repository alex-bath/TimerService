package com.ticketclever.go.timerservice.service;

import com.ticketclever.go.timerservice.api.Activation;
import com.ticketclever.go.timerservice.api.AllocatableTicketDetails;
import com.ticketclever.go.timerservice.model.ActivationTimerState;
import com.ticketclever.go.timerservice.services.TimerRequestBroker;
import com.ticketclever.go.timerservice.streams.ActivationEventPublisher;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import javax.validation.constraints.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) //reset db after each test
@ActiveProfiles("test")
public class TimerRequestBrokerTest {

    @Autowired
    TimerRequestBroker broker;

    @MockBean
    ActivationEventPublisher publisher;

    @Test
    public void createTimerTest() throws ExecutionException, InterruptedException {
        final Activation activation = new Activation();
        activation.setJourneyId(UUID.randomUUID().toString());
        activation.setActivationTime(LocalDateTime.now());

        ActivationTimerState state = this.broker.receiveEvent(activation);

        assertThat("initialisation of timer failed", state, is(not(nullValue())));
    }

    @Test
    public void timerSendsMessageTest() throws ExecutionException, InterruptedException {
        ReflectionTestUtils.setField(this.broker, "timerDuration", Duration.ofSeconds(1));
        ReflectionTestUtils.setField(this.broker, "tickSlice", 2L);
        BlockingQueue<TimerRequestBrokerTest.DelayObject> queue = new DelayQueue<>();


        final Activation input = new Activation();
        input.setJourneyId(UUID.randomUUID().toString());
        input.setActivationTime(LocalDateTime.now());

        this.broker.receiveEvent(input);

        IntStream.of(5).forEach(count -> {
                    try {
                        queue.put(new TimerRequestBrokerTest.DelayObject("timerSendsMessageTest", 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        try {
            while (!queue.isEmpty()) queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        verify(this.publisher, times(1)).committedActivation(any(AllocatableTicketDetails.class));
    }

    public class DelayObject implements Delayed {
        String data;
        long startTime;

        DelayObject(String data, long delayInMilliseconds) {
            this.data = data;
            this.startTime = System.currentTimeMillis() + delayInMilliseconds;
        }

        @Override
        public long getDelay(@NotNull TimeUnit unit) {
            long diff = startTime - System.currentTimeMillis();
            return unit.convert(diff, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(@NotNull Delayed o) {
            return Math.toIntExact(this.startTime - ((TimerRequestBrokerTest.DelayObject) o).startTime);
        }
    }

}