package hu.bsstudio.robonaut.safetycar.configuration;

import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.safetycar.DefaultSafetyCarService;
import hu.bsstudio.robonaut.safetycar.SafetyCarService;
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
