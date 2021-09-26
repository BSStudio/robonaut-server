package hu.bsstudio.robonaut.scores

import hu.bsstudio.robonaut.scores.endresult.EndResultService
import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class SeniorEndResultHandler(private val service: EndResultService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val detailedTeam = request.bodyToFlux(EndResultedTeam::class.java)
            .flatMap { endResultedTeam: EndResultedTeam? -> service.updateEndResultSenior(endResultedTeam) }
        return ServerResponse.ok().body(detailedTeam, DetailedTeam::class.java)
    }
}
