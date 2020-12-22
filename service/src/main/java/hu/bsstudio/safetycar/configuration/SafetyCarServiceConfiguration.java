package hu.bsstudio.safetycar.configuration;

import hu.bsstudio.repository.TeamRepository;
import hu.bsstudio.safetycar.DefaultSafetyCarService;
import hu.bsstudio.safetycar.SafetyCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SafetyCarServiceConfiguration {

    @Autowired
    private TeamRepository repository;

    @Bean
    public SafetyCarService safetyCarService() {
        return new DefaultSafetyCarService(repository);
    }
}
