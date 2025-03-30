package hu.bsstudio.robonaut.skill

import hu.bsstudio.robonaut.race.skill.SkillRaceService
import hu.bsstudio.robonaut.race.skill.model.GateInformation
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

class SkillGateHandler(
    private val service: SkillRaceService,
) : HandlerFunction<ServerResponse> {
    override fun handle(request: ServerRequest): Mono<ServerResponse> =
        request
            .bodyToMono<GateInformation>()
            .flatMap(service::updateSkillRaceResultOnGate)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
}
