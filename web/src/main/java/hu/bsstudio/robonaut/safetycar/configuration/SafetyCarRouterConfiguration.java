package hu.bsstudio.robonaut.safetycar.configuration;

import hu.bsstudio.robonaut.safetycar.SafetyCarFollowHandler;
import hu.bsstudio.robonaut.safetycar.SafetyCarOvertakeHandler;
import hu.bsstudio.robonaut.safetycar.SafetyCarService;
import hu.bsstudio.robonaut.security.RobonAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SafetyCarRouterConfiguration {

    @Autowired
    private RobonAuthFilter robonAuthFilter;
    @Autowired
    private SafetyCarService service;

    @Bean
    public RouterFunction<ServerResponse> safetyCarRouterFunction(final SafetyCarFollowHandler safetyCarFollowHandler,
                                                                  final SafetyCarOvertakeHandler safetyCarOvertakeHandler) {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/speed/safetyCar/follow", safetyCarFollowHandler)
            .POST("/api/speed/safetyCar/overtake", safetyCarOvertakeHandler)
            .build();
    }

    @Bean
    public SafetyCarFollowHandler safetyCarFollowHandler() {
        return new SafetyCarFollowHandler(service);
    }

    @Bean
    public SafetyCarOvertakeHandler safetyCarOvertakeHandler() {
        return new SafetyCarOvertakeHandler(service);
    }
}
