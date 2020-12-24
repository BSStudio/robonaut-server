package hu.bsstudio.robonaut.race.skill;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.race.skill.model.GateInformation;
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class DefaultSkillRaceServiceTest {

    private static final long TEAM_ID = 2020;
    private static final int BONUS_TIME = 42;
    private static final int TIME_LEFT = 4321;
    private static final int SKILL_SCORE = 1234;
    private static final int TOTAL_SKILL_SCORE = 9999;
    private static final GateInformation GATE_INFO = new GateInformation(TEAM_ID, BONUS_TIME, TIME_LEFT, SKILL_SCORE, TOTAL_SKILL_SCORE);
    private static final SkillRaceResult SKILL_RACE_RESULT = new SkillRaceResult(TEAM_ID, SKILL_SCORE);

    private DefaultSkillRaceService underTest;

    @Mock
    private TeamRepository mockRepository;
    @Mock
    private TeamModelEntityMapper mockMapper;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.underTest = new DefaultSkillRaceService(mockRepository);
        this.underTest.setMapper(mockMapper);
    }

    @Test
    void shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdated() {
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setSkillScore(TOTAL_SKILL_SCORE);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.just(updatedTeamEntity));
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockMapper.toModel(updatedTeamEntity)).thenReturn(detailedTeam);

        final var result = underTest.updateSkillRaceResultOnGate(GATE_INFO);

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenEntityWasNotFound() {
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.empty());

        final var result = underTest.updateSkillRaceResultOnGate(GATE_INFO);

        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdatedWhenRaceResultWasSubmitted() {
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setSkillScore(SKILL_SCORE);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.just(updatedTeamEntity));
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockMapper.toModel(updatedTeamEntity)).thenReturn(detailedTeam);

        final var result = underTest.updateSkillRaceResult(SKILL_RACE_RESULT);

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenEntityWasNotFoundWhenRaceResultWasSubmitted() {
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.empty());

        final var result = underTest.updateSkillRaceResult(SKILL_RACE_RESULT);

        StepVerifier.create(result)
            .verifyComplete();
    }
}
