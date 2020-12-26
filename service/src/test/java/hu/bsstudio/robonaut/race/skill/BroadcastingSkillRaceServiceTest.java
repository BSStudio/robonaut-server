package hu.bsstudio.robonaut.race.skill;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.race.skill.model.GateInformation;
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class BroadcastingSkillRaceServiceTest {

    private static final SkillRaceResult SKILL_RACE_RESULT = new SkillRaceResult(0, 0);
    private static final GateInformation GATE_INFORMATION = new GateInformation(0, 0, 0, 0, 0);
    private static final DetailedTeam DETAILED_TEAM = DetailedTeam.builder().build();
    private static final String GATE_ROUTING_KEY = "skill.gate";
    private static final String TEAM_ROUTING_KEY = "team.teamData";

    private BroadcastingSkillRaceService underTest;

    @Mock
    private RabbitTemplate mockTemplate;
    @Mock
    private SkillRaceService mockService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new BroadcastingSkillRaceService(mockTemplate, mockService);
    }

    @Test
    void shouldReturnDetailedTeamFromUnderLyingServiceAndSendItWhenRaceResultWasSubmitted() {
        when(mockService.updateSkillRaceResult(SKILL_RACE_RESULT))
            .thenReturn(Mono.just(DETAILED_TEAM));

        final var result = underTest.updateSkillRaceResult(SKILL_RACE_RESULT);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(TEAM_ROUTING_KEY, DETAILED_TEAM);
    }

    @Test
    void shouldSendGateInformationThenShouldReturnDetailedTeamFromUnderLyingServiceAndSendIt() {
        when(mockService.updateSkillRaceResultOnGate(GATE_INFORMATION))
            .thenReturn(Mono.just(DETAILED_TEAM));

        final var result = underTest.updateSkillRaceResultOnGate(GATE_INFORMATION);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(GATE_ROUTING_KEY, GATE_INFORMATION);
        verify(mockTemplate).convertAndSend(TEAM_ROUTING_KEY, DETAILED_TEAM);
    }
}
