package hu.bsstudio.robonaut.skill

import hu.bsstudio.robonaut.race.skill.timer.SkillTimerService
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class UpdateSkillTimerHandler(private val service: SkillTimerService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<SkillTimer>()
            .flatMap(service::updateTimer)
            .let { skillTimer -> ServerResponse.ok().body(skillTimer) }
    }
}
