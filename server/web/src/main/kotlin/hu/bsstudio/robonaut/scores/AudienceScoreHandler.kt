package hu.bsstudio.robonaut.scores

import hu.bsstudio.robonaut.scores.audience.AudienceScoreService
import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToFlux
import reactor.core.publisher.Mono

class AudienceScoreHandler(
    private val service: AudienceScoreService,
) : HandlerFunction<ServerResponse> {
    override fun handle(request: ServerRequest): Mono<ServerResponse> =
        request
            .bodyToFlux<AudienceScoredTeam>()
            .flatMap(service::updateAudienceScore)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
}
