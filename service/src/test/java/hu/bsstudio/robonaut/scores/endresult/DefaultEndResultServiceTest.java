package hu.bsstudio.robonaut.scores.endresult;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.entity.ScoreEntity;
import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class DefaultEndResultServiceTest {

    private static final long TEAM_ID = 42;
    private static final int POINTS = 420;
    private static final EndResultedTeam END_RESULTED_TEAM = new EndResultedTeam(TEAM_ID, POINTS);

    private DefaultEndResultService underTest;

    @Mock
    private TeamRepository mockRepository;
    @Mock
    private TeamModelEntityMapper mockMapper;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new DefaultEndResultService(mockRepository);
        this.underTest.setTeamModelEntityMapper(mockMapper);
    }

    @Test
    void shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdated() {
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        final var updatedScore = new ScoreEntity();
        updatedScore.setScore(POINTS);
        updatedTeamEntity.setScore(updatedScore);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.just(updatedTeamEntity));
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockMapper.toModel(updatedTeamEntity)).thenReturn(detailedTeam);

        final var result = underTest.updateEndResultSenior(END_RESULTED_TEAM);

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenEntityWasNotFound() {
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        final var updatedScore = new ScoreEntity();
        updatedScore.setScore(POINTS);
        updatedTeamEntity.setScore(updatedScore);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.empty());

        final var result = underTest.updateEndResultSenior(END_RESULTED_TEAM);

        StepVerifier.create(result)
            .verifyComplete();
    }

    // todo junior and rename prev methods
}
