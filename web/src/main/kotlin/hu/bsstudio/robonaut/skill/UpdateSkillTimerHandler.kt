package hu.bsstudio.robonaut.skill

import hu.bsstudio.robonaut.race.skill.timer.SkillTimerService
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class UpdateSkillTimerHandler(private val service: SkillTimerService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val skillTimer = request.bodyToMono(SkillTimer::class.java)
            .flatMap { skillTimer: SkillTimer? -> service.updateTimer(skillTimer) }
        return ServerResponse.ok().body(skillTimer, SkillTimer::class.java)
    }
}
