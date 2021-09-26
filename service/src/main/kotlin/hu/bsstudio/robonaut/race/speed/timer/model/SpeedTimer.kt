package hu.bsstudio.robonaut.race.speed.timer.model;

import hu.bsstudio.robonaut.common.model.TimerAction;
import lombok.Value;

@Value
public class SpeedTimer {
    int timerAt;
    TimerAction timerAction;
}
