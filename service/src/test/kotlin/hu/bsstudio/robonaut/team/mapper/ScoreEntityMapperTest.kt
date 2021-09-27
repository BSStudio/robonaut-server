package hu.bsstudio.robonaut.team.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.entity.ScoreEntity;
import hu.bsstudio.robonaut.team.model.Score;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class ScoreEntityMapperTest {

    private static final int SCORE = 420;
    private static final int SPEED_SCORE = 2021;
    private static final int BEST_SPEED_TIME = 999;
    public static final Score MODEL = new Score(SPEED_SCORE, BEST_SPEED_TIME, SCORE);

    private ScoreEntityMapper underTest;
    public static final ScoreEntity ENTITY = createScoreEntity();

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new ScoreEntityMapper();
    }

    @Test
    void shouldReturnModel() {

        final var result = underTest.toModel(ENTITY);

        assertThat(result).isEqualTo(MODEL);
    }

    @Test
    void shouldReturnEntity() {

        final var result = underTest.toEntity(MODEL);

        assertThat(result).isEqualTo(ENTITY);
    }

    private static ScoreEntity createScoreEntity() {
        final var entity = new ScoreEntity();
        entity.setSpeedScore(SPEED_SCORE);
        entity.setBestSpeedTime(BEST_SPEED_TIME);
        entity.setScore(SCORE);
        return entity;
    }
}
