package hu.bsstudio.robonaut.race.speed;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult;
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BroadcastingSpeedRaceService implements SpeedRaceService {

    @NonNull
    private final RabbitTemplate template;
    @NonNull
    private final SpeedRaceService service;

    @Override
    public Mono<DetailedTeam> updateSpeedRaceOnLap(final SpeedRaceScore speedRaceScore) {
        return Mono.just(speedRaceScore)
            .doOnNext(this::sendLapInfo)
            .flatMap(service::updateSpeedRaceOnLap);
    }

    @Override
    public Mono<DetailedTeam> updateSpeedRace(final SpeedRaceResult speedRaceResult) {
        return service.updateSpeedRace(speedRaceResult)
            .doOnNext(this::sendTeamInfo);
    }

    private void sendLapInfo(final SpeedRaceScore raceScore) {
        template.convertAndSend("speed.lap", raceScore);
    }

    private void sendTeamInfo(final DetailedTeam detailedTeam) {
        template.convertAndSend("team.teamData", detailedTeam);
    }
}
