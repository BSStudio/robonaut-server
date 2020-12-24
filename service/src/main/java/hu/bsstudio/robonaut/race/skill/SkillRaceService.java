package hu.bsstudio.robonaut.race.skill;

import hu.bsstudio.robonaut.race.skill.model.GateInfo;
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import reactor.core.publisher.Mono;

public interface SkillRaceService {
    Mono<DetailedTeam> updateSkillRaceResultOnGate(final GateInfo gateInfo);

    Mono<DetailedTeam> updateSkillRaceResult(final SkillRaceResult skillRaceResult);
}
