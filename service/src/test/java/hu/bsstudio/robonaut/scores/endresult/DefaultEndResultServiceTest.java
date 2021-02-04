package hu.bsstudio.robonaut.scores.endresult;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.entity.ScoreEntity;
import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.entity.TeamType;
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
    private static final DetailedTeam DETAILED_TEAM = DetailedTeam.builder().build();

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
    void shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdatedOnJunior() {
        final var foundTeamEntity = new TeamEntity();
        foundTeamEntity.setScore(new ScoreEntity());
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        final var updatedScore = new ScoreEntity();
        updatedScore.setScore(POINTS);
        updatedTeamEntity.setScore(updatedScore);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.just(updatedTeamEntity));
        when(mockMapper.toModel(updatedTeamEntity)).thenReturn(DETAILED_TEAM);

        Mono.just(END_RESULTED_TEAM)
            .flatMap(underTest::updateEndResultSenior)

            .as(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenEntityWasNotFoundOnJunior() {
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.empty());

        Mono.just(END_RESULTED_TEAM)
            .flatMap(underTest::updateEndResultSenior)

            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    void shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdatedOnSenior() {
        final var foundTeamEntity = new TeamEntity();
        foundTeamEntity.setTeamType(TeamType.JUNIOR);
        foundTeamEntity.setJuniorScore(new ScoreEntity());
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        final var updatedScore = new ScoreEntity();
        updatedScore.setScore(POINTS);
        updatedTeamEntity.setJuniorScore(updatedScore);
        updatedTeamEntity.setTeamType(TeamType.JUNIOR);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.just(updatedTeamEntity));
        when(mockMapper.toModel(updatedTeamEntity)).thenReturn(DETAILED_TEAM);

        Mono.just(END_RESULTED_TEAM)
            .flatMap(underTest::updateEndResultJunior)

            .as(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenSeniorTeamTriesToUpdateJuniorScore() {
        final var foundTeamEntity = new TeamEntity();
        foundTeamEntity.setTeamType(TeamType.SENIOR);
        foundTeamEntity.setJuniorScore(new ScoreEntity());
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));

        Mono.just(END_RESULTED_TEAM)
            .flatMap(underTest::updateEndResultJunior)

            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenEntityWasNotFoundOnSenior() {
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.empty());

        Mono.just(END_RESULTED_TEAM)
            .flatMap(underTest::updateEndResultJunior)

            .as(StepVerifier::create)
            .verifyComplete();
    }
}
