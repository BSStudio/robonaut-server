package hu.bsstudio.robonaut.scores.qualification.configuration;

import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.scores.qualification.DefaultQualificationScoreService;
import hu.bsstudio.robonaut.scores.qualification.QualificationScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QualificationScoreServiceConfiguration {

    @Autowired
    private TeamRepository repository;

    @Bean
    public QualificationScoreService qualificationScoreService() {
        return new DefaultQualificationScoreService(repository);
    }
}
