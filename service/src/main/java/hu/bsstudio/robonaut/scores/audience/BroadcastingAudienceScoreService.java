package hu.bsstudio.robonaut.scores.audience;

import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BroadcastingAudienceScoreService implements AudienceScoreService {

    public static final String TEAM_TEAM_DATA_ROUTING_KEY = "team.teamData";

    @NonNull
    private final RabbitTemplate template;
    @NonNull
    private final AudienceScoreService service;

    @Override
    public Mono<DetailedTeam> updateAudienceScore(final AudienceScoredTeam audienceScoredTeam) {
        return service.updateAudienceScore(audienceScoredTeam)
            .doOnNext(this::sendTeamInfo);
    }

    private void sendTeamInfo(final DetailedTeam detailedTeam) {
        template.convertAndSend(TEAM_TEAM_DATA_ROUTING_KEY, detailedTeam);
    }
}
