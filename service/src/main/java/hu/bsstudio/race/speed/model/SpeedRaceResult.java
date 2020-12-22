package hu.bsstudio.race.speed.model;

import java.util.List;
import lombok.Value;

@Value
public class SpeedRaceResult {
    long teamId;
    int speedScore;
    int speedBonusScore;
    List<Integer> speedTimes;
}
