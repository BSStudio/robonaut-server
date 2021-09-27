package hu.bsstudio.robonaut.safetycar;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation;
import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class BroadcastingSafetyCarServiceTest {

    private static final SafetyCarFollowInformation SAFETY_CAR_FOLLOW_INFORMATION = new SafetyCarFollowInformation(0, true);
    private static final SafetyCarOvertakeInformation SAFETY_CAR_OVERTAKE_INFORMATION = new SafetyCarOvertakeInformation(0, 0);
    private static final DetailedTeam DETAILED_TEAM = DetailedTeam.builder().build();
    private static final String FOLLOW_ROUTING_KEY = "speed.safetyCar.follow";
    private static final String OVERTAKE_ROUTING_KEY = "speed.safetyCar.overtake";
    private static final String TEAM_DATA_ROUTING_KEY = "team.teamData";

    private BroadcastingSafetyCarService underTest;

    @Mock
    private RabbitTemplate mockTemplate;
    @Mock
    private SafetyCarService mockService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new BroadcastingSafetyCarService(mockTemplate, mockService);
    }

    @Test
    void shouldSendSafetyCarFollowInformationThenShouldReturnDetailedTeamFromUnderLyingServiceAndSendIt() {
        when(mockService.safetyCarWasFollowed(SAFETY_CAR_FOLLOW_INFORMATION))
            .thenReturn(Mono.just(DETAILED_TEAM));

        final var result = underTest.safetyCarWasFollowed(SAFETY_CAR_FOLLOW_INFORMATION);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(FOLLOW_ROUTING_KEY, SAFETY_CAR_FOLLOW_INFORMATION);
        verify(mockTemplate).convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM);
    }

    @Test
    void shouldSendSafetyCarOvertakeInformationThenShouldReturnDetailedTeamFromUnderLyingServiceAndSendIt() {
        when(mockService.safetyCarWasOvertaken(SAFETY_CAR_OVERTAKE_INFORMATION))
            .thenReturn(Mono.just(DETAILED_TEAM));

        final var result = underTest.safetyCarWasOvertaken(SAFETY_CAR_OVERTAKE_INFORMATION);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(OVERTAKE_ROUTING_KEY, SAFETY_CAR_OVERTAKE_INFORMATION);
        verify(mockTemplate).convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM);
    }
}
