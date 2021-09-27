package hu.bsstudio.robonaut.safetycar

import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class SafetyCarOvertakeHandler(private val service: SafetyCarService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<SafetyCarOvertakeInformation>()
            .flatMap(service::safetyCarWasOvertaken)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
