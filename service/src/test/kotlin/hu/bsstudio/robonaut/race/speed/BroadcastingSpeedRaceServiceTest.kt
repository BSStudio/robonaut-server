package hu.bsstudio.robonaut.race.speed;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult;
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class BroadcastingSpeedRaceServiceTest {

    private static final SpeedRaceResult SPEED_RACE_RESULT = new SpeedRaceResult(0, 0, 0, Collections.emptyList());
    private static final SpeedRaceScore SPEED_RACE_SCORE = new SpeedRaceScore(0, Collections.emptyList());
    private static final DetailedTeam DETAILED_TEAM = DetailedTeam.builder().build();
    private static final String TEAM_DATA_ROUTING_KEY = "team.teamData";
    private static final String SPEED_LAP_ROUTING_KEY = "speed.lap";

    private BroadcastingSpeedRaceService underTest;

    @Mock
    private RabbitTemplate mockTemplate;
    @Mock
    private SpeedRaceService mockService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new BroadcastingSpeedRaceService(mockTemplate, mockService);
    }

    @Test
    void shouldReturnDetailedTeamFromUnderLyingServiceAndSendItWhenRaceResultWasSubmittedOnJunior() {
        when(mockService.updateSpeedRaceJunior(SPEED_RACE_RESULT))
            .thenReturn(Mono.just(DETAILED_TEAM));

        final var result = underTest.updateSpeedRaceJunior(SPEED_RACE_RESULT);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM);
    }

    @Test
    void shouldReturnDetailedTeamFromUnderLyingServiceAndSendItWhenRaceResultWasSubmittedOnSenior() {
        when(mockService.updateSpeedRaceSenior(SPEED_RACE_RESULT))
            .thenReturn(Mono.just(DETAILED_TEAM));

        final var result = underTest.updateSpeedRaceSenior(SPEED_RACE_RESULT);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM);
    }

    @Test
    void shouldSendSpeedRaceScoreThenShouldReturnDetailedTeamFromUnderLyingServiceAndSendIt() {
        when(mockService.updateSpeedRaceOnLap(SPEED_RACE_SCORE))
            .thenReturn(Mono.just(DETAILED_TEAM));

        final var result = underTest.updateSpeedRaceOnLap(SPEED_RACE_SCORE);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(SPEED_LAP_ROUTING_KEY, SPEED_RACE_SCORE);
        verify(mockTemplate).convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM);
    }
}
