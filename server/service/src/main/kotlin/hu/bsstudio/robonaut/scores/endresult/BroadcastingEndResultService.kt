package hu.bsstudio.robonaut.scores.endresult

import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono

class BroadcastingEndResultService(
    private val template: RabbitTemplate,
    private val service: EndResultService,
) : EndResultService {
    override fun updateEndResultSenior(endResultedTeam: EndResultedTeam): Mono<DetailedTeam> =
        service
            .updateEndResultSenior(endResultedTeam)
            .doOnNext(::sendTeamInfo)

    override fun updateEndResultJunior(endResultedTeam: EndResultedTeam): Mono<DetailedTeam> =
        service
            .updateEndResultJunior(endResultedTeam)
            .doOnNext(::sendTeamInfo)

    private fun sendTeamInfo(detailedTeam: DetailedTeam) {
        template.convertAndSend("team.teamData", detailedTeam)
    }
}
