package hu.bsstudio.robonaut.skill

import hu.bsstudio.robonaut.race.skill.timer.SkillTimerService
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

class UpdateSkillTimerHandler(private val service: SkillTimerService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<SkillTimer>()
            .flatMap(service::updateTimer)
            .let { skillTimer -> ServerResponse.ok().body(skillTimer) }
    }
}
