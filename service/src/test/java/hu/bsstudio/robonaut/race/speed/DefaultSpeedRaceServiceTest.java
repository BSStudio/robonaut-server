package hu.bsstudio.robonaut.race.speed;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import hu.bsstudio.robonaut.entity.TeamEntity;
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

    private static final long TEAM_ID = 42;
    private static final int SPEED_SCORE = 120;
    private static final int SPEED_BONUS_SCORE = 2;
    private static final List<Integer> SPEED_TIMES = List.of(420, 4200);

    private DefaultSpeedRaceService underTest;

    @Mock
    private TeamRepository mockRepository;
    @Mock
    private TeamModelEntityMapper mockMapper;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.underTest = new DefaultSpeedRaceService(mockRepository);
        this.underTest.setMapper(mockMapper);
    }

    @Test
    void shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdated() {
        final var speedRaceResult = new SpeedRaceScore(TEAM_ID, SPEED_TIMES);
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setSpeedTimes(SPEED_TIMES);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.just(updatedTeamEntity));
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockMapper.toModel(updatedTeamEntity)).thenReturn(detailedTeam);

        final var result = underTest.updateSpeedRaceOnLap(speedRaceResult);

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete();
    }

    @Test
    void shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdatedWhenRaceResultWasSubmitted() {
        final var speedRaceResult = new SpeedRaceResult(TEAM_ID, SPEED_SCORE, SPEED_BONUS_SCORE, SPEED_TIMES);
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setSpeedTimes(SPEED_TIMES);
        updatedTeamEntity.setSpeedBonusScore(SPEED_BONUS_SCORE);
        updatedTeamEntity.setSpeedScore(SPEED_SCORE);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.just(updatedTeamEntity));
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockMapper.toModel(updatedTeamEntity)).thenReturn(detailedTeam);

        final var result = underTest.updateSpeedRace(speedRaceResult);

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenEntityWasNotFound() {
        final var speedRaceResult = new SpeedRaceScore(TEAM_ID, SPEED_TIMES);
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setSpeedTimes(SPEED_TIMES);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.empty());

        final var result = underTest.updateSpeedRaceOnLap(speedRaceResult);

        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenEntityWasNotFoundWhenRaceResultWasSubmitted() {
        final var speedRaceResult = new SpeedRaceResult(TEAM_ID, SPEED_SCORE, SPEED_BONUS_SCORE, SPEED_TIMES);
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setSpeedTimes(SPEED_TIMES);
        updatedTeamEntity.setSpeedBonusScore(SPEED_BONUS_SCORE);
        updatedTeamEntity.setSpeedScore(SPEED_SCORE);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.empty());

        final var result = underTest.updateSpeedRace(speedRaceResult);

        StepVerifier.create(result)
            .verifyComplete();
    }
}
