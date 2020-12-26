package hu.bsstudio.robonaut.team.query;

import hu.bsstudio.robonaut.team.TeamService;
import hu.bsstudio.robonaut.team.query.model.Requester;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueryTeamData {

    @Autowired
    private TeamService teamService;

    @RabbitListener(queues = "general.teamData")
    public void sendTeamData(final Requester requester) {
        LOG.info("Teams were requested by {}", requester);
        teamService.findAllTeam()
            .subscribe();
    }
}
