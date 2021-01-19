package hu.bsstudio.robonaut.team;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.team.model.DetailedTeam;
import hu.bsstudio.robonaut.team.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class BroadcastingTeamServiceTest {

    private static final Team TEAM = Team.builder().build();
    private static final DetailedTeam DETAILED_TEAM_1 = DetailedTeam.builder().teamId(1).build();
    private static final DetailedTeam DETAILED_TEAM_2 = DetailedTeam.builder().teamId(2).build();
    private static final String TEAM_DATA_ROUTING_KEY = "team.teamData";

    private BroadcastingTeamService underTest;

    @Mock
    private RabbitTemplate mockTemplate;
    @Mock
    private TeamService mockService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new BroadcastingTeamService(mockTemplate, mockService);
    }

    @Test
    void shouldReturnDetailedTeamFromUnderLyingServiceAndSendItWhenTeamIsCreated() {
        when(mockService.addTeam(TEAM))
            .thenReturn(Mono.just(DETAILED_TEAM_1));

        final var result = underTest.addTeam(TEAM);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM_1);
    }

    @Test
    void shouldReturnDetailedTeamFromUnderLyingServiceAndSendItWhenTeamIsUpdated() {
        when(mockService.updateTeam(TEAM))
            .thenReturn(Mono.just(DETAILED_TEAM_1));

        final var result = underTest.updateTeam(TEAM);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM_1);
    }

    @Test
    void shouldReturnDetailedTeamFromUnderLyingServiceAndSendItWhenTeamIsUpdatedByAdmin() {
        when(mockService.updateTeam(DETAILED_TEAM_1))
            .thenReturn(Mono.just(DETAILED_TEAM_1));

        final var result = underTest.updateTeam(DETAILED_TEAM_1);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM_1);
    }

    @Test
    void shouldReturnDetailedTeamFromUnderLyingServiceAndSendItWhenFindingAllTeam() {
        when(mockService.findAllTeam())
            .thenReturn(Flux.just(DETAILED_TEAM_1, DETAILED_TEAM_2));

        final var result = underTest.findAllTeam();

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .expectNext(DETAILED_TEAM_2)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM_1);
        verify(mockTemplate).convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM_2);
    }
}
