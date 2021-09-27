package hu.bsstudio.robonaut.skill

import hu.bsstudio.robonaut.race.skill.SkillRaceService
import hu.bsstudio.robonaut.race.skill.model.GateInformation
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class SkillGateHandler(private val service: SkillRaceService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<GateInformation>()
            .flatMap(service::updateSkillRaceResultOnGate)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
