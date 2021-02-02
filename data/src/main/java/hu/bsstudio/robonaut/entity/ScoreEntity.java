package hu.bsstudio.robonaut.entity;

import java.util.List;
import lombok.Data;

@Data
public class ScoreEntity {
    // speed
    private int speedScore;
    private int speedBonusScore;
    private List<Integer> speedTimes;

    // calculated
    private int score;
    private int rank;
}
