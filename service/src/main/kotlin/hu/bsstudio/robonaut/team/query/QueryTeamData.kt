package hu.bsstudio.robonaut.team.query

import hu.bsstudio.robonaut.team.TeamService
import hu.bsstudio.robonaut.team.query.model.Requester
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class QueryTeamData @Autowired constructor(private val teamService: TeamService) {

    @RabbitListener(queues = ["general.teamData"])
    fun sendTeamData(requester: Requester) {
        LOG.info("Teams were requested by {}", requester)
        teamService.findAllTeam()
            .subscribe()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}
