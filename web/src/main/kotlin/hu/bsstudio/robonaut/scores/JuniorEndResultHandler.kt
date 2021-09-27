package hu.bsstudio.robonaut.scores

import hu.bsstudio.robonaut.scores.endresult.EndResultService
import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class JuniorEndResultHandler(private val service: EndResultService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToFlux<EndResultedTeam>()
            .flatMap(service::updateEndResultJunior)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
