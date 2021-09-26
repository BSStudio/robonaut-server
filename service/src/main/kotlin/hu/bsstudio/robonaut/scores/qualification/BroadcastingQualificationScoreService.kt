package hu.bsstudio.robonaut.scores.qualification

import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import lombok.RequiredArgsConstructor
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono

@RequiredArgsConstructor
class BroadcastingQualificationScoreService(
    private val template: RabbitTemplate,
    private val service: QualificationScoreService,
) : QualificationScoreService {

    override fun updateQualificationScore(qualifiedTeam: QualifiedTeam): Mono<DetailedTeam> {
        return service.updateQualificationScore(qualifiedTeam)
            .doOnNext(this::sendTeamInfo)
    }

    private fun sendTeamInfo(detailedTeam: DetailedTeam?) {
        template.convertAndSend(TEAM_TEAM_DATA_ROUTING_KEY, detailedTeam!!)
    }

    companion object {
        const val TEAM_TEAM_DATA_ROUTING_KEY = "team.teamData"
    }
}
