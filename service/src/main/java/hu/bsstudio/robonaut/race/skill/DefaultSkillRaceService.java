package hu.bsstudio.robonaut.race.skill;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.race.skill.model.GateInfo;
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultSkillRaceService implements SkillRaceService {

    private final TeamRepository repository;

    @Setter(AccessLevel.PACKAGE)
    private TeamModelEntityMapper mapper = new TeamModelEntityMapper();

    @Override
    public Mono<DetailedTeam> updateSkillRaceResultOnGate(final GateInfo gateInfo) {
        return Mono.just(gateInfo)
            // todo broadcast gate info
            .map(GateInfo::getTeamId)
            .flatMap(repository::findById)
            .map(entity -> updateSkillRaceInfo(entity, gateInfo))
            .flatMap(repository::save)
            .map(mapper::toModel);
    }

    @Override
    public Mono<DetailedTeam> updateSkillRaceResult(final SkillRaceResult skillRaceResult) {
        return Mono.just(skillRaceResult)
            // todo broadcast final result
            .map(SkillRaceResult::getTeamId)
            .flatMap(repository::findById)
            .map(entity -> updateSkillRaceInfo(entity, skillRaceResult))
            .flatMap(repository::save)
            .map(mapper::toModel);
    }

    private TeamEntity updateSkillRaceInfo(final TeamEntity entity, final GateInfo gateInfo) {
        entity.setSkillScore(gateInfo.getTotalSkillScore());
        return entity;
    }

    private TeamEntity updateSkillRaceInfo(final TeamEntity entity, final SkillRaceResult skillRaceResult) {
        entity.setSkillScore(skillRaceResult.getSkillScore());
        return entity;
    }
}
