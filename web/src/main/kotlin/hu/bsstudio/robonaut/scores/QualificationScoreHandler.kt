package hu.bsstudio.robonaut.scores

import hu.bsstudio.robonaut.scores.qualification.QualificationScoreService
import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class QualificationScoreHandler(private val service: QualificationScoreService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToFlux<QualifiedTeam>()
            .flatMap(service::updateQualificationScore)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
