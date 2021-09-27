package hu.bsstudio.robonaut.entity;

import lombok.Data;

@Data
public class ScoreEntity {
    // speed
    private int speedScore;
    private int bestSpeedTime;

    // calculated
    private int score;
}
