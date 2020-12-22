package hu.bsstudio.scores.qualification.configuration;

import hu.bsstudio.repository.TeamRepository;
import hu.bsstudio.scores.qualification.DefaultQualificationScoreService;
import hu.bsstudio.scores.qualification.QualificationScoreService;
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
