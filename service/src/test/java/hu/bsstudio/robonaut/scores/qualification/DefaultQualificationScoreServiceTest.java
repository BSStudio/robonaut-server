package hu.bsstudio.robonaut.scores.qualification;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class DefaultQualificationScoreServiceTest {

    private static final long TEAM_ID = 42;
    private static final int QUALIFICATION_SCORE = 30;
    private static final QualifiedTeam QUALIFIED_TEAM = new QualifiedTeam(TEAM_ID, QUALIFICATION_SCORE);

    private DefaultQualificationScoreService underTest;

    @Mock
    private TeamRepository mockRepository;
    @Mock
    private TeamModelEntityMapper mockMapper;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new DefaultQualificationScoreService(mockRepository);
        this.underTest.setMapper(mockMapper);
    }

    @Test
    void shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdated() {
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setQualificationScore(QUALIFICATION_SCORE);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.just(updatedTeamEntity));
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockMapper.toModel(updatedTeamEntity)).thenReturn(detailedTeam);

        final var result = underTest.updateQualificationScore(QUALIFIED_TEAM);

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenEntityWasNotFound() {
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setQualificationScore(QUALIFICATION_SCORE);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.empty());

        final var result = underTest.updateQualificationScore(QUALIFIED_TEAM);

        StepVerifier.create(result)
            .verifyComplete();
    }
}
