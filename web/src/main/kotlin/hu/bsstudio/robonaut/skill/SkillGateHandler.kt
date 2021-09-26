package hu.bsstudio.robonaut.skill

import hu.bsstudio.robonaut.race.skill.SkillRaceService
import hu.bsstudio.robonaut.race.skill.model.GateInformation
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class SkillGateHandler(private val service: SkillRaceService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val detailedTeam = request.bodyToMono(GateInformation::class.java)
            .flatMap(service::updateSkillRaceResultOnGate)
        return ServerResponse.ok().body(detailedTeam, DetailedTeam::class.java)
    }
}
