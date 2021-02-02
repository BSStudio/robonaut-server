package hu.bsstudio.robonaut.team.mapper;

import hu.bsstudio.robonaut.entity.ScoreEntity;
import hu.bsstudio.robonaut.team.model.Score;

public class ScoreEntityMapper {

    public ScoreEntity toEntity(final Score score) {
        final var entity = new ScoreEntity();
        entity.setScore(score.getTotalScore());
        entity.setSpeedScore(score.getSpeedScore());
        entity.setBestSpeedTime(score.getBestSpeedTime());
        return entity;
    }

    public Score toModel(final ScoreEntity entity) {
        return new Score(entity.getSpeedScore(), entity.getBestSpeedTime(), entity.getScore());
    }
}
