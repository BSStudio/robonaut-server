package hu.bsstudio.robonaut.scores

import hu.bsstudio.robonaut.scores.endresult.EndResultService
import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class SeniorEndResultHandler(private val service: EndResultService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToFlux<EndResultedTeam>()
            .flatMap(service::updateEndResultSenior)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
