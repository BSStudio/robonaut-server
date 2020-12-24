package hu.bsstudio.robonaut.race.skill.timer.model;

import hu.bsstudio.robonaut.common.model.TimerAction;
import lombok.Value;

@Value
public class SkillTimer {
    int timerAt;
    TimerAction timerAction;
}
