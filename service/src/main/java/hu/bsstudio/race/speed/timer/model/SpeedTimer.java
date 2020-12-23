package hu.bsstudio.race.speed.timer.model;

import hu.bsstudio.common.model.TimerAction;
import lombok.Value;

@Value
public class SpeedTimer {
    int timerAt;
    TimerAction timerAction;
}
