package hu.bsstudio.robonaut.race.skill;

import hu.bsstudio.robonaut.race.skill.model.GateInformation;
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BroadcastingSkillRaceService implements SkillRaceService {

    public static final String SKILL_GATE_ROUTING_KEY = "skill.gate";
    public static final String TEAM_TEAM_DATA_ROUTING_KEY = "team.teamData";

    @NonNull
    private final RabbitTemplate template;
    @NonNull
    private final SkillRaceService service;

    @Override
    public Mono<DetailedTeam> updateSkillRaceResultOnGate(final GateInformation gateInformation) {
        return Mono.just(gateInformation)
            .doOnNext(this::sendGateInfo)
            .flatMap(service::updateSkillRaceResultOnGate)
            .doOnNext(this::sendTeamInfo);
    }

    @Override
    public Mono<DetailedTeam> updateSkillRaceResult(final SkillRaceResult skillRaceResult) {
        return service.updateSkillRaceResult(skillRaceResult)
            .doOnNext(this::sendTeamInfo);
    }


    private void sendGateInfo(final GateInformation gateInfo) {
        template.convertAndSend(SKILL_GATE_ROUTING_KEY, gateInfo);
    }

    private void sendTeamInfo(final DetailedTeam detailedTeam) {
        template.convertAndSend(TEAM_TEAM_DATA_ROUTING_KEY, detailedTeam);
    }
}
