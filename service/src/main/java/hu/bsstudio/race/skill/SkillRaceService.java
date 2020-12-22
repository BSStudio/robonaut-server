package hu.bsstudio.race.skill;

import hu.bsstudio.race.skill.model.GateInfo;
import hu.bsstudio.race.skill.model.SkillRaceResult;
import hu.bsstudio.team.model.DetailedTeam;
import reactor.core.publisher.Mono;

public interface SkillRaceService {
    Mono<DetailedTeam> updateSkillRaceResultOnGate(final GateInfo gateInfo);
    Mono<DetailedTeam> updateSkillRaceResult(final SkillRaceResult skillRaceResult);
}
