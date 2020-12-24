package hu.bsstudio.robonaut.race.skill.configuration;

import hu.bsstudio.robonaut.race.skill.DefaultSkillRaceService;
import hu.bsstudio.robonaut.race.skill.SkillRaceService;
import hu.bsstudio.robonaut.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SkillRaceServiceConfiguration {

    @Autowired
    private TeamRepository repository;

    @Bean
    public SkillRaceService skillRaceService() {
        return new DefaultSkillRaceService(repository);
    }
}
