package hu.bsstudio.robonaut.scores

import hu.bsstudio.robonaut.scores.qualification.QualificationScoreService
import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToFlux
import reactor.core.publisher.Mono

class QualificationScoreHandler(
    private val service: QualificationScoreService,
) : HandlerFunction<ServerResponse> {
    override fun handle(request: ServerRequest): Mono<ServerResponse> =
        request
            .bodyToFlux<QualifiedTeam>()
            .flatMap(service::updateQualificationScore)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
}
