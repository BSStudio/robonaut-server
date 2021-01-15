package hu.bsstudio.robonaut.team;

import hu.bsstudio.robonaut.team.model.DetailedTeam;
import hu.bsstudio.robonaut.team.model.Team;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BroadcastingTeamService implements TeamService {

    public static final String TEAM_TEAM_DATA_ROUTING_KEY = "team.teamData";

    @NonNull
    private final RabbitTemplate template;
    @NonNull
    private final TeamService service;


    @Override
    public Mono<DetailedTeam> addTeam(final Team team) {
        return service.addTeam(team)
            .doOnNext(this::sendTeamInfo);
    }

    @Override
    public Mono<DetailedTeam> updateTeam(final Team team) {
        return service.updateTeam(team)
            .doOnNext(this::sendTeamInfo);
    }

    @Override
    public Mono<DetailedTeam> updateTeam(final DetailedTeam detailedTeam) {
        return service.updateTeam(detailedTeam)
            .doOnNext(this::sendTeamInfo);
    }

    @Override
    public Flux<DetailedTeam> findAllTeam() {
        return service.findAllTeam()
            .doOnNext(this::sendTeamInfo);
    }

    private void sendTeamInfo(final DetailedTeam detailedTeam) {
        template.convertAndSend(TEAM_TEAM_DATA_ROUTING_KEY, detailedTeam);
    }
}
