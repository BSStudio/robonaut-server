package hu.bsstudio.robonaut.race.speed

import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono

class BroadcastingSpeedRaceService(
    private val template: RabbitTemplate,
    private val service: SpeedRaceService
) : SpeedRaceService {

    override fun updateSpeedRaceOnLap(speedRaceScore: SpeedRaceScore): Mono<DetailedTeam> {
        return Mono.just(speedRaceScore)
            .doOnNext(this::sendLapInfo)
            .flatMap(service::updateSpeedRaceOnLap)
            .doOnNext(this::sendTeamInfo)
    }

    override fun updateSpeedRaceJunior(speedRaceResult: SpeedRaceResult): Mono<DetailedTeam> {
        return service.updateSpeedRaceJunior(speedRaceResult)
            .doOnNext(this::sendTeamInfo)
    }

    override fun updateSpeedRaceSenior(speedRaceResult: SpeedRaceResult): Mono<DetailedTeam> {
        return service.updateSpeedRaceSenior(speedRaceResult)
            .doOnNext(this::sendTeamInfo)
    }

    private fun sendLapInfo(raceScore: SpeedRaceScore) {
        template.convertAndSend("speed.lap", raceScore)
    }

    private fun sendTeamInfo(detailedTeam: DetailedTeam) {
        template.convertAndSend("team.teamData", detailedTeam)
    }
}
