package hu.bsstudio.robonaut.scores

import hu.bsstudio.robonaut.scores.audience.AudienceScoreService
import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class AudienceScoreHandler(private val service: AudienceScoreService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val detailedTeam = request.bodyToFlux(AudienceScoredTeam::class.java)
            .flatMap(service::updateAudienceScore)
        return ServerResponse.ok().body(detailedTeam, DetailedTeam::class.java)
    }
}
