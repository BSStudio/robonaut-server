package hu.bsstudio.robonaut.safetycar;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation;
import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class DefaultSafetyCarServiceTest {

    private static final long TEAM_ID = 42;
    private static final boolean SAFETY_CAR_FOLLOWED = true;
    private static final int NUMBER_OF_OVERTAKES = 5;
    private static final SafetyCarFollowInformation FOLLOW_INFORMATION = new SafetyCarFollowInformation(TEAM_ID, SAFETY_CAR_FOLLOWED);
    private static final SafetyCarOvertakeInformation SAFETY_CAR_OVERTAKE_INFORMATION = new SafetyCarOvertakeInformation(TEAM_ID, NUMBER_OF_OVERTAKES);

    private DefaultSafetyCarService underTest;

    @Mock
    private TeamRepository mockRepository;
    @Mock
    private TeamModelEntityMapper mockMapper;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new DefaultSafetyCarService(mockRepository);
        this.underTest.setMapper(mockMapper);
    }

    @Test
    void shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdatedWhenSafetyCarWasFollowed() {
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setSafetyCarWasFollowed(SAFETY_CAR_FOLLOWED);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.just(updatedTeamEntity));
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockMapper.toModel(updatedTeamEntity)).thenReturn(detailedTeam);

        final var result = underTest.safetyCarWasFollowed(FOLLOW_INFORMATION);

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenEntityWasNotFoundWhenSafetyCarWasFollowed() {
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setSafetyCarWasFollowed(SAFETY_CAR_FOLLOWED);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.empty());

        final var result = underTest.safetyCarWasFollowed(FOLLOW_INFORMATION);

        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdatedWhenSafetyCarWasOvertaken() {
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setNumberOfOvertakes(NUMBER_OF_OVERTAKES);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.just(updatedTeamEntity));
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockMapper.toModel(updatedTeamEntity)).thenReturn(detailedTeam);

        final var result = underTest.safetyCarWasOvertaken(SAFETY_CAR_OVERTAKE_INFORMATION);

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenEntityWasNotFoundWhenSafetyCarWasOvertaken() {
        final var foundTeamEntity = new TeamEntity();
        when(mockRepository.findById(TEAM_ID)).thenReturn(Mono.just(foundTeamEntity));
        final var updatedTeamEntity = new TeamEntity();
        updatedTeamEntity.setNumberOfOvertakes(NUMBER_OF_OVERTAKES);
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.empty());

        final var result = underTest.safetyCarWasOvertaken(SAFETY_CAR_OVERTAKE_INFORMATION);

        StepVerifier.create(result)
            .verifyComplete();
    }
}
