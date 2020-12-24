package hu.bsstudio.robonaut.team;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import hu.bsstudio.robonaut.team.model.Team;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultTeamService implements TeamService {

    @NonNull
    private final TeamRepository teamRepository;

    @Setter(AccessLevel.PACKAGE)
    private TeamModelEntityMapper teamMapper = new TeamModelEntityMapper();

    @Override
    public Mono<DetailedTeam> addTeam(final Team team) {
        return Mono.just(team)
            .map(this::toEntity)
            .flatMap(teamRepository::insert)
            .map(teamMapper::toModel);
    }

    @Override
    public Mono<DetailedTeam> updateTeam(final Team team) {
        return Mono.just(team)
            .map(Team::getTeamId)
            .flatMap(teamRepository::findById)
            .map(entity -> updateBasicTeamInfo(entity, team))
            .flatMap(teamRepository::save)
            .map(teamMapper::toModel);
    }

    @Override
    public Flux<DetailedTeam> findAllTeam() {
        return teamRepository.findAll()
            .map(teamMapper::toModel);
    }

    private TeamEntity toEntity(final Team team) {
        return updateBasicTeamInfo(new TeamEntity(), team);
    }

    private TeamEntity updateBasicTeamInfo(final TeamEntity entity, final Team team) {
        entity.setTeamId(team.getTeamId());
        entity.setYear(team.getYear());
        entity.setTeamName(team.getTeamName());
        entity.setTeamMembers(team.getTeamMembers());
        entity.setTeamType(team.getTeamType());
        entity.setSpeedTimes(Collections.emptyList());
        return entity;
    }
}
