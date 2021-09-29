package hu.bsstudio.robonaut.scores.qualification

import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono

class BroadcastingQualificationScoreService(
    private val template: RabbitTemplate,
    private val service: QualificationScoreService,
) : QualificationScoreService {

    override fun updateQualificationScore(qualifiedTeam: QualifiedTeam): Mono<DetailedTeam> {
        return service.updateQualificationScore(qualifiedTeam)
            .doOnNext(::sendTeamInfo)
    }

    private fun sendTeamInfo(detailedTeam: DetailedTeam) {
        template.convertAndSend("team.teamData", detailedTeam)
    }
}
