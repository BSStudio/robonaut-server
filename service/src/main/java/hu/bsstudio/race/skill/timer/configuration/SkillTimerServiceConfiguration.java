package hu.bsstudio.race.skill.timer.configuration;

import hu.bsstudio.race.skill.timer.DefaultSkillTimerService;
import hu.bsstudio.race.skill.timer.SkillTimerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SkillTimerServiceConfiguration {

    @Bean
    public SkillTimerService skillTimerService() {
        return new DefaultSkillTimerService();
    }
}
