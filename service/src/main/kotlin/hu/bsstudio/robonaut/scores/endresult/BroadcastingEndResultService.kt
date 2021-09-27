package hu.bsstudio.robonaut.scores.endresult

import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import lombok.RequiredArgsConstructor
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono

@RequiredArgsConstructor
class BroadcastingEndResultService(
    private val template: RabbitTemplate,
    private val service: EndResultService,
) : EndResultService {

    override fun updateEndResultSenior(endResultedTeam: EndResultedTeam): Mono<DetailedTeam> {
        return service.updateEndResultSenior(endResultedTeam)
            .doOnNext(this::sendTeamInfo)
    }

    override fun updateEndResultJunior(endResultedTeam: EndResultedTeam): Mono<DetailedTeam> {
        return service.updateEndResultJunior(endResultedTeam)
            .doOnNext(this::sendTeamInfo)
    }

    private fun sendTeamInfo(detailedTeam: DetailedTeam) {
        template.convertAndSend("team.teamData", detailedTeam)
    }

}
