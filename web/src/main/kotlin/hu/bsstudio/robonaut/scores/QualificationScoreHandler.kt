package hu.bsstudio.robonaut.scores

import hu.bsstudio.robonaut.scores.qualification.QualificationScoreService
import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class QualificationScoreHandler(private val service: QualificationScoreService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val detailedTeam = request.bodyToFlux(QualifiedTeam::class.java)
            .flatMap { team: QualifiedTeam? -> service.updateQualificationScore(team) }
        return ServerResponse.ok().body(detailedTeam, DetailedTeam::class.java)
    }
}
