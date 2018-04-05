package com.ticketclever.go.timerservice.service;

import com.ticketclever.go.timerservice.services.TimerRequestBroker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) //reset db after each test
@ActiveProfiles("cloudless")
public class TimerRequestBrokerTest {

    @Autowired
    TimerRequestBroker broker;

    @Test
    public void createTimerTest() {
        assertThat("Irrational string behaviour", "something", is(not(nullValue())));
    }

}
