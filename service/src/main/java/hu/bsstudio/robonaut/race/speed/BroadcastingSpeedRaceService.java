package hu.bsstudio.robonaut.race.speed;

import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BroadcastingSpeedRaceService implements SpeedRaceService {

    public static final String SPEED_LAP_ROUTING_KEY = "speed.lap";
    public static final String TEAM_TEAM_DATA_ROUTING_KEY = "team.teamData";

    @NonNull
    private final RabbitTemplate template;
    @NonNull
    private final SpeedRaceService service;

    @Override
    public Mono<DetailedTeam> updateSpeedRace(final SpeedRaceScore speedRaceScore) {
        return Mono.just(speedRaceScore)
            .doOnNext(this::sendLapInfo)
            .flatMap(service::updateSpeedRace)
            .doOnNext(this::sendTeamInfo);
    }

    private void sendLapInfo(final SpeedRaceScore raceScore) {
        template.convertAndSend(SPEED_LAP_ROUTING_KEY, raceScore);
    }

    private void sendTeamInfo(final DetailedTeam detailedTeam) {
        template.convertAndSend(TEAM_TEAM_DATA_ROUTING_KEY, detailedTeam);
    }
}
