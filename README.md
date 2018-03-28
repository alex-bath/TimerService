# Timer Service

#### Accepts Activation

Activation messages are received over kafka and after a configurable 
time period (timer) they are transformed into a *AllocatableTicketDetails* 
message and returned. 

#### Accepts JourneyAbandonmentEvent

At any time during this scheduled waiting period the timer may be 
cancelled with an incoming *JourneyAbandonmentEvent* message. 

#### Summary

This service is, in Enterprise Integration Patterns terminology
 a *Delayer*. 

#### Configuration

```timerservice:
  eventtimer:
    duration: PT20M
    tickslice: 20
```

   - **duration** marks the total duration of each timer, as an expression
   - **tickslice** the subdivisions of the duration for recovery persistence and drift adjustment

   Note: Akka timers only drift late 

  

### Key Contacts

- Alex Bath <alex@globaltravelventures.com>
