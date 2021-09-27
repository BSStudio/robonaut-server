package hu.bsstudio.robonaut.safetycar

import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class SafetyCarFollowHandler(private val service: SafetyCarService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<SafetyCarFollowInformation>()
            .flatMap(service::safetyCarWasFollowed)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
