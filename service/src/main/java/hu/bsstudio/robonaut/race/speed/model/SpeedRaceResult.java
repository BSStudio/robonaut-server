package hu.bsstudio.robonaut.race.speed.model;

import java.util.List;
import lombok.Value;

@Value
public class SpeedRaceResult {
    long teamId;
    int speedScore;
    int bestSpeedTime;
    List<Integer> speedTimes;
}
