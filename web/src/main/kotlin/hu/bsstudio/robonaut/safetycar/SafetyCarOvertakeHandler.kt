package hu.bsstudio.robonaut.safetycar

import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class SafetyCarOvertakeHandler(private val service: SafetyCarService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val detailedTeam = request.bodyToMono(SafetyCarOvertakeInformation::class.java)
            .flatMap { safetyCarOvertakeInformation: SafetyCarOvertakeInformation? ->
                service.safetyCarWasOvertaken(safetyCarOvertakeInformation)
            }
        return ServerResponse.ok().body(detailedTeam, DetailedTeam::class.java)
    }
}
