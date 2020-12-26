package hu.bsstudio.robonaut.race.skill.configuration;

import hu.bsstudio.robonaut.race.skill.BroadcastingSkillRaceService;
import hu.bsstudio.robonaut.race.skill.DefaultSkillRaceService;
import hu.bsstudio.robonaut.race.skill.SkillRaceService;
import hu.bsstudio.robonaut.repository.TeamRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SkillRaceServiceConfiguration {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TeamRepository repository;

    @Bean
    public SkillRaceService skillRaceService(final SkillRaceService defaultSkillRaceService) {
        return new BroadcastingSkillRaceService(rabbitTemplate, defaultSkillRaceService);
    }

    @Bean
    public SkillRaceService defaultSkillRaceService() {
        return new DefaultSkillRaceService(repository);
    }
}
