package hu.bsstudio.team;

import hu.bsstudio.entity.TeamEntity;
import hu.bsstudio.repository.TeamRepository;
import hu.bsstudio.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.team.model.DetailedTeam;
import hu.bsstudio.team.model.Team;
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
            .map(this::toEntity)
            .flatMap(teamRepository::save)
            .map(teamMapper::toModel);
        // todo rework
    }

    @Override
    public Flux<DetailedTeam> findAllTeam() {
        return teamRepository.findAll()
            .map(teamMapper::toModel);
    }

    private TeamEntity toEntity(final Team team) {
        final var teamEntity = new TeamEntity();
        teamEntity.setTeamId(team.getTeamId());
        teamEntity.setYear(team.getYear());
        teamEntity.setTeamName(team.getTeamName());
        teamEntity.setTeamMembers(team.getTeamMembers());
        teamEntity.setTeamType(team.getTeamType());
        teamEntity.setSpeedTimes(Collections.emptyList());
        return teamEntity;
    }
}
