package hu.bsstudio.robonaut.race.skill;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.race.skill.model.GateInformation;
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultSkillRaceService implements SkillRaceService {

    @NonNull
    private final TeamRepository repository;

    @Setter(AccessLevel.PACKAGE)
    private TeamModelEntityMapper mapper = new TeamModelEntityMapper();

    @Override
    public Mono<DetailedTeam> updateSkillRaceResultOnGate(final GateInformation gateInformation) {
        return Mono.just(gateInformation)
            .map(GateInformation::getTeamId)
            .flatMap(repository::findById)
            .map(entity -> updateSkillRaceInfo(entity, gateInformation))
            .flatMap(repository::save)
            .map(mapper::toModel);
    }

    @Override
    public Mono<DetailedTeam> updateSkillRaceResult(final SkillRaceResult skillRaceResult) {
        return Mono.just(skillRaceResult)
            .map(SkillRaceResult::getTeamId)
            .flatMap(repository::findById)
            .map(entity -> updateSkillRaceInfo(entity, skillRaceResult))
            .flatMap(repository::save)
            .map(mapper::toModel);
    }

    private TeamEntity updateSkillRaceInfo(final TeamEntity entity, final GateInformation gateInformation) {
        entity.setSkillScore(gateInformation.getTotalSkillScore());
        return entity;
    }

    private TeamEntity updateSkillRaceInfo(final TeamEntity entity, final SkillRaceResult skillRaceResult) {
        entity.setSkillScore(skillRaceResult.getSkillScore());
        return entity;
    }
}
