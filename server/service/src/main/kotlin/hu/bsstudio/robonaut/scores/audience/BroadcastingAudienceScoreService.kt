package hu.bsstudio.robonaut.scores.audience

import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono

class BroadcastingAudienceScoreService(
  private val template: RabbitTemplate,
  private val service: AudienceScoreService,
) : AudienceScoreService {
  override fun updateAudienceScore(audienceScoredTeam: AudienceScoredTeam): Mono<DetailedTeam> =
    service
      .updateAudienceScore(audienceScoredTeam)
      .doOnNext(::sendTeamInfo)

  private fun sendTeamInfo(detailedTeam: DetailedTeam) {
    template.convertAndSend("team.teamData", detailedTeam)
  }
}
