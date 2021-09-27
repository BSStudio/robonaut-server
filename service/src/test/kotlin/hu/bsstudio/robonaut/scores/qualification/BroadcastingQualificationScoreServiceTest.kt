package hu.bsstudio.robonaut.scores.qualification;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class BroadcastingQualificationScoreServiceTest {

    private static final QualifiedTeam QUALIFIED_TEAM = new QualifiedTeam(0, 0);
    private static final DetailedTeam DETAILED_TEAM = DetailedTeam.builder().build();
    private static final String TEAM_DATA_ROUTING_KEY = "team.teamData";

    private BroadcastingQualificationScoreService underTest;

    @Mock
    private RabbitTemplate mockTemplate;
    @Mock
    private QualificationScoreService mockService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new BroadcastingQualificationScoreService(mockTemplate, mockService);
    }

    @Test
    void shouldReturnDetailedTeamFromUnderLyingServiceAndSendIt() {
        when(mockService.updateQualificationScore(QUALIFIED_TEAM))
            .thenReturn(Mono.just(DETAILED_TEAM));

        final var result = underTest.updateQualificationScore(QUALIFIED_TEAM);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM);
    }
}
