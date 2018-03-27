package com.ticketclever.go.timerservice.model;

import com.ticketclever.go.timerservice.api.Activation;
import java.time.Duration;
import java.time.LocalDateTime;

public class ActivationTimerState {

    public final Activation data;
    public final LocalDateTime start;
    public final LocalDateTime finish;
    public final Duration duration;
    public final Long elapsed;

    public static Data create() {
        return data -> start -> finish -> duration -> elapsed -> new ActivationTimerState(data, start, finish, duration, elapsed);
    }

    public static ActivationTimerState advance(final ActivationTimerState timerState, final long elapsed) {
        return create().data(timerState.data).start(timerState.start).finish(timerState.finish).duration(timerState.duration).elapsed(elapsed);
    }

    private ActivationTimerState(final Activation data, final LocalDateTime start, final LocalDateTime finish, final Duration duration, final Long elapsed) {
        this.data = data;
        this.start = start;
        this.finish = finish;
        this.duration = duration;
        this.elapsed = elapsed;
    }

    public interface Data {
        Start data(Activation activation);
    }

    public interface Start {
        Finish start(LocalDateTime start);
    }

    public interface Finish {
        TimerDuration finish(LocalDateTime finish);
    }

    public interface TimerDuration {
         Elapsed duration(Duration duration);
    }

    public interface Elapsed {
        ActivationTimerState elapsed(Long elapsed);
    }

    @Override
    public String toString() {
        return "ActivationTimerState{" +
                "data=" + data +
                ", start=" + start +
                ", finish=" + finish +
                ", duration=" + duration +
                ", elapsed=" + elapsed +
                '}';
    }
}