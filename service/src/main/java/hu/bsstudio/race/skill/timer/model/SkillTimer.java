package hu.bsstudio.race.skill.timer.model;

import hu.bsstudio.common.TimerAction;
import lombok.Value;

@Value
public class SkillTimer {
    int timerAt;
    TimerAction timerAction;
}
