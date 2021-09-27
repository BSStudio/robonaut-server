package hu.bsstudio.robonaut.skill

import hu.bsstudio.robonaut.race.skill.SkillRaceService
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class SkillRaceResultHandler(private val service: SkillRaceService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<SkillRaceResult>()
            .flatMap(service::updateSkillRaceResult)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
