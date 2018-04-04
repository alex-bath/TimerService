package com.ticketclever.go.timerservice.service;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.scalatest.junit.JUnitSuite;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class EventTimerTest extends JunaitSuit {

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
    public void firstMessageTest() {

    }

}
