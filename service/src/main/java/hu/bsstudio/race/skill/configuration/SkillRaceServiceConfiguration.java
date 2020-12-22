package hu.bsstudio.race.skill.configuration;

import hu.bsstudio.race.skill.DefaultSkillRaceService;
import hu.bsstudio.race.skill.SkillRaceService;
import hu.bsstudio.repository.TeamRepository;
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
