package hu.bsstudio.robonaut.scores.endresult;

import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BroadcastingEndResultService implements EndResultService {

    public static final String TEAM_TEAM_DATA_ROUTING_KEY = "team.teamData";

    @NonNull
    private final RabbitTemplate template;
    @NonNull
    private final EndResultService service;

    @Override
    public Mono<DetailedTeam> updateEndResult(final EndResultedTeam endResultedTeam) {
        return service.updateEndResult(endResultedTeam)
            .doOnNext(this::sendTeamInfo);
    }

    private void sendTeamInfo(final DetailedTeam detailedTeam) {
        template.convertAndSend(TEAM_TEAM_DATA_ROUTING_KEY, detailedTeam);
    }
}
