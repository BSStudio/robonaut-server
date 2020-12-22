package hu.bsstudio.race.speed.model;

import java.util.List;
import lombok.Value;

@Value
public class SpeedRaceScore {
    long teamId;
    List<Integer> speedTimes;
}
