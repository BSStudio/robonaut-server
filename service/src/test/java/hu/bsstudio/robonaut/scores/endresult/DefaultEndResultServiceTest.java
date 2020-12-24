package hu.bsstudio.robonaut.scores.endresult;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

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
    private static final int TOTAL_SCORE = 420;
    private static final int RANK = 2;
    private static final int JUNIOR_RANK = 1;
    private static final EndResultedTeam END_RESULTED_TEAM = new EndResultedTeam(TEAM_ID, TOTAL_SCORE, RANK, JUNIOR_RANK);

    private DefaultEndResultService underTest;

    @Mock
    private TeamRepository mockRepository;
    @Mock
    private TeamModelEntityMapper mockMapper;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.underTest = new DefaultEndResultService(mockRepository);
        this.underTest.setTeamModelEntityMapper(mockMapper);
    }

    @Test
    void shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdated() {
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setRank(RANK);
        updatedTeamEntity.setJuniorRank(JUNIOR_RANK);
        updatedTeamEntity.setTotalScore(TOTAL_SCORE);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.just(updatedTeamEntity));
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockMapper.toModel(updatedTeamEntity)).thenReturn(detailedTeam);

        final var result = underTest.updateEndResult(END_RESULTED_TEAM);

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenEntityWasNotFound() {
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setRank(RANK);
        updatedTeamEntity.setJuniorRank(JUNIOR_RANK);
        updatedTeamEntity.setTotalScore(TOTAL_SCORE);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.empty());

        final var result = underTest.updateEndResult(END_RESULTED_TEAM);

        StepVerifier.create(result)
            .verifyComplete();
    }
}
