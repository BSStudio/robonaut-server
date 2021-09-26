package hu.bsstudio.robonaut.scores.qualification.configuration;

import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.scores.qualification.BroadcastingQualificationScoreService;
import hu.bsstudio.robonaut.scores.qualification.DefaultQualificationScoreService;
import hu.bsstudio.robonaut.scores.qualification.QualificationScoreService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QualificationScoreServiceConfiguration {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TeamRepository repository;

    @Bean
    public QualificationScoreService qualificationScoreService(final QualificationScoreService defaultQualificationScoreService) {
        return new BroadcastingQualificationScoreService(rabbitTemplate, defaultQualificationScoreService);
    }

    @Bean
    public QualificationScoreService defaultQualificationScoreService() {
        return new DefaultQualificationScoreService(repository);
    }
}
