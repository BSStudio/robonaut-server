package hu.bsstudio.robonaut.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.connection.SimplePropertyValueConnectionNameStrategy;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    public Queue generalTeamDataQueue() {
        return new Queue("general.teamData");
    }

    @Bean
    public Queue startSkillTimerQueue() {
        return new Queue("skill.timer");
    }

    @Bean
    public Queue skillRaceGateQueue() {
        return new Queue("skill.gate");
    }

    @Bean
    public Queue safetyCarFollowQueue() {
        return new Queue("speed.safetyCar.follow");
    }

    @Bean
    public Queue safetyCarOvertakeQueue() {
        return new Queue("speed.safetyCar.overtake");
    }

    @Bean
    public Queue startSpeedTimerQueue() {
        return new Queue("speed.timer");
    }

    @Bean
    public Queue speedRaceLapQueue() {
        return new Queue("speed.lap");
    }

    @Bean
    public Queue teamDataQueue() {
        return new Queue("team.teamData");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionNameStrategy connectionNameStrategy() {
        return new SimplePropertyValueConnectionNameStrategy("spring.application.name");
    }
}
