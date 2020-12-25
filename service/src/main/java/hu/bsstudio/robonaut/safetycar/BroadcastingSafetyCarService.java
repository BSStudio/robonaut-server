package hu.bsstudio.robonaut.safetycar;

import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation;
import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BroadcastingSafetyCarService implements SafetyCarService {

    public static final String SPEED_SAFETY_CAR_FOLLOW_ROUTING_KEY = "speed.safetyCar.follow";
    public static final String SPEED_SAFETY_CAR_OVERTAKE_ROUTING_KEY = "speed.safetyCar.overtake";

    @NonNull
    private final RabbitTemplate template;
    @NonNull
    private final SafetyCarService service;

    @Override
    public Mono<DetailedTeam> safetyCarWasFollowed(final SafetyCarFollowInformation safetyCarFollowInformation) {
        return Mono.just(safetyCarFollowInformation)
            .doOnNext(this::sendSafetyCarFollow)
            .flatMap(service::safetyCarWasFollowed);
    }

    @Override
    public Mono<DetailedTeam> safetyCarWasOvertaken(final SafetyCarOvertakeInformation safetyCarOvertakeInformation) {
        return Mono.just(safetyCarOvertakeInformation)
            .doOnNext(this::sendSafetyCarOvertake)
            .flatMap(service::safetyCarWasOvertaken);
    }

    private void sendSafetyCarFollow(final SafetyCarFollowInformation safetyCarFollowInformation) {
        template.convertAndSend(SPEED_SAFETY_CAR_FOLLOW_ROUTING_KEY, safetyCarFollowInformation);
    }

    private void sendSafetyCarOvertake(final SafetyCarOvertakeInformation safetyCarOvertakeInformation) {
        template.convertAndSend(SPEED_SAFETY_CAR_OVERTAKE_ROUTING_KEY, safetyCarOvertakeInformation);
    }
}
