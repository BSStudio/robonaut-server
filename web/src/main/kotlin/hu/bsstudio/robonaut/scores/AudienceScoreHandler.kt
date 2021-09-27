package hu.bsstudio.robonaut.scores

import hu.bsstudio.robonaut.scores.audience.AudienceScoreService
import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class AudienceScoreHandler(private val service: AudienceScoreService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToFlux<AudienceScoredTeam>()
            .flatMap(service::updateAudienceScore)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
