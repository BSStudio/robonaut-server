package hu.bsstudio.robonaut.team.query

import hu.bsstudio.robonaut.team.TeamService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class QueryTeamData(private val teamService: TeamService) {
    @RabbitListener(queues = ["general.teamData"])
    fun sendTeamData() {
        LOG.info("Teams were requested.")
        teamService.findAllTeam()
            .subscribe()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}
