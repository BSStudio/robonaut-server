package hu.bsstudio.robonaut.race.speed;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.entity.ScoreEntity;
import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.entity.TeamType;
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult;
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class DefaultSpeedRaceServiceTest {

    private static final long TEAM_ID = 1;
    private static final int SPEED_SCORE = 420;
    private static final int BEST_SPEED_TIME = 444;
    private static final List<Integer> SPEED_TIMES = List.of(123, 456);
    private static final SpeedRaceResult SPEED_RACE_RESULT = new SpeedRaceResult(TEAM_ID, SPEED_SCORE, BEST_SPEED_TIME, SPEED_TIMES);
    private static final DetailedTeam DETAILED_TEAM = DetailedTeam.builder().teamId(TEAM_ID).build();
    private static final SpeedRaceScore SPEED_RACE_SCORE = new SpeedRaceScore(TEAM_ID, SPEED_TIMES);

    private DefaultSpeedRaceService underTest;

    @Mock
    private TeamRepository mockRepository;
    @Mock
    private TeamModelEntityMapper mockMapper;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new DefaultSpeedRaceService(mockRepository);
        this.underTest.setMapper(mockMapper);
    }

    @Test
    void onLap() {
        final TeamEntity foundEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundEntity));

        final var updatedEntity = new TeamEntity();
        updatedEntity.setSpeedTimes(SPEED_TIMES);
        when(mockRepository.save(updatedEntity)).thenReturn(Mono.just(updatedEntity));
        when(mockMapper.toModel(updatedEntity)).thenReturn(DETAILED_TEAM);

        Mono.just(SPEED_RACE_SCORE)
            .flatMap(underTest::updateSpeedRaceOnLap)
            .as(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
    }

    @Test
    void onLapNotFound() {
        final TeamEntity foundEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundEntity));

        final var updatedEntity = new TeamEntity();
        updatedEntity.setSpeedTimes(SPEED_TIMES);
        when(mockRepository.save(updatedEntity)).thenReturn(Mono.empty());

        Mono.just(SPEED_RACE_SCORE)
            .flatMap(underTest::updateSpeedRaceOnLap)
            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    void junior() {
        final TeamEntity foundEntity = new TeamEntity();
        foundEntity.setTeamType(TeamType.JUNIOR);
        final var juniorScore = new ScoreEntity();
        foundEntity.setJuniorScore(juniorScore);
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundEntity));

        final var updatedEntity = new TeamEntity();
        updatedEntity.setTeamType(TeamType.JUNIOR);
        updatedEntity.setSpeedTimes(SPEED_TIMES);
        juniorScore.setSpeedScore(SPEED_SCORE);
        juniorScore.setBestSpeedTime(BEST_SPEED_TIME);
        updatedEntity.setJuniorScore(juniorScore);
        when(mockRepository.save(updatedEntity)).thenReturn(Mono.just(updatedEntity));

        when(mockMapper.toModel(updatedEntity)).thenReturn(DETAILED_TEAM);

        Mono.just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceJunior)
            .as(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
    }

    @Test
    void juniorNotFound() {
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.empty());

        Mono.just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceJunior)
            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    void juniorWhenSenior() {
        final TeamEntity foundEntity = new TeamEntity();
        foundEntity.setTeamType(TeamType.SENIOR);
        final var juniorScore = new ScoreEntity();
        foundEntity.setJuniorScore(juniorScore);
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundEntity));

        Mono.just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceJunior)
            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    void senior() {
        final TeamEntity foundEntity = new TeamEntity();
        final var scoreEntity = new ScoreEntity();
        foundEntity.setScore(scoreEntity);
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundEntity));

        final var updatedEntity = new TeamEntity();
        updatedEntity.setSpeedTimes(SPEED_TIMES);
        scoreEntity.setSpeedScore(SPEED_SCORE);
        scoreEntity.setBestSpeedTime(BEST_SPEED_TIME);
        updatedEntity.setScore(scoreEntity);
        when(mockRepository.save(updatedEntity)).thenReturn(Mono.just(updatedEntity));

        when(mockMapper.toModel(updatedEntity)).thenReturn(DETAILED_TEAM);

        Mono.just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceSenior)
            .as(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
    }

    @Test
    void seniorNotFound() {
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.empty());

        Mono.just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceSenior)
            .as(StepVerifier::create)
            .verifyComplete();
    }

    // todo rename
}
