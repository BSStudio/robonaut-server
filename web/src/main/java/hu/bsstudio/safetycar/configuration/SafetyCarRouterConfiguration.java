package hu.bsstudio.safetycar.configuration;

import hu.bsstudio.safetycar.SafetyCarFollowHandler;
import hu.bsstudio.safetycar.SafetyCarOvertakeHandler;
import hu.bsstudio.safetycar.SafetyCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SafetyCarRouterConfiguration {

    @Autowired
    private SafetyCarService service;

    @Bean
    public RouterFunction<ServerResponse> safetyCarRouterFunction(final SafetyCarFollowHandler safetyCarFollowHandler,
                                                                  final SafetyCarOvertakeHandler safetyCarOvertakeHandler) {
        return RouterFunctions.route()
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
