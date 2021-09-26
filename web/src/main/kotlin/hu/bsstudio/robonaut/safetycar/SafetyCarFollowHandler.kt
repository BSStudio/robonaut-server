package hu.bsstudio.robonaut.safetycar

import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class SafetyCarFollowHandler(private val service: SafetyCarService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val safety = request.bodyToMono(SafetyCarFollowInformation::class.java)
            .flatMap { safetyCarFollowInformation: SafetyCarFollowInformation? ->
                service.safetyCarWasFollowed(safetyCarFollowInformation)
            }
        return ServerResponse.ok().body(safety, DetailedTeam::class.java)
    }
}
