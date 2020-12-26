package hu.bsstudio.robonaut.scores.endresult;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class BroadcastingEndResultServiceTest {

    private static final EndResultedTeam END_RESULTED_TEAM = new EndResultedTeam(0, 0, 0, 0);
    private static final DetailedTeam DETAILED_TEAM = DetailedTeam.builder().build();
    private static final String TEAM_DATA_ROUTING_KEY = "team.teamData";

    private BroadcastingEndResultService underTest;

    @Mock
    private RabbitTemplate mockTemplate;
    @Mock
    private EndResultService mockService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new BroadcastingEndResultService(mockTemplate, mockService);
    }

    @Test
    void shouldReturnDetailedTeamFromUnderLyingServiceAndSendIt() {
        when(mockService.updateEndResult(END_RESULTED_TEAM))
            .thenReturn(Mono.just(DETAILED_TEAM));

        final var result = underTest.updateEndResult(END_RESULTED_TEAM);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM);
    }
}
