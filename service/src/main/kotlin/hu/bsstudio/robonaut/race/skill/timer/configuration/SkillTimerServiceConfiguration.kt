package hu.bsstudio.robonaut.race.skill.timer.configuration;

import hu.bsstudio.robonaut.race.skill.timer.BroadcastingSkillTimerService;
import hu.bsstudio.robonaut.race.skill.timer.DefaultSkillTimerService;
import hu.bsstudio.robonaut.race.skill.timer.SkillTimerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SkillTimerServiceConfiguration {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public SkillTimerService skillTimerService(final SkillTimerService defaultSkillTimerService) {
        return new BroadcastingSkillTimerService(rabbitTemplate, defaultSkillTimerService);
    }

    @Bean
    public SkillTimerService defaultSkillTimerService() {
        return new DefaultSkillTimerService();
    }
}
