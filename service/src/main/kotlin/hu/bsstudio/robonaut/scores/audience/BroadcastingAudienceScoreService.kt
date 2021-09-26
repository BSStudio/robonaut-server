package hu.bsstudio.robonaut.scores.audience

import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono

class BroadcastingAudienceScoreService(
    private val template: RabbitTemplate,
    private val service: AudienceScoreService,
) : AudienceScoreService {

    override fun updateAudienceScore(audienceScoredTeam: AudienceScoredTeam): Mono<DetailedTeam> {
        return service.updateAudienceScore(audienceScoredTeam)
            .doOnNext(this::sendTeamInfo)
    }

    private fun sendTeamInfo(detailedTeam: DetailedTeam) {
        template.convertAndSend(TEAM_TEAM_DATA_ROUTING_KEY, detailedTeam)
    }

    companion object {
        const val TEAM_TEAM_DATA_ROUTING_KEY = "team.teamData"
    }
}
