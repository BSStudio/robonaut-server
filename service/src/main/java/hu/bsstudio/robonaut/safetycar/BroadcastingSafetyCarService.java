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
        template.convertAndSend("speed.safetyCar.follow", safetyCarFollowInformation);
    }

    private void sendSafetyCarOvertake(final SafetyCarOvertakeInformation safetyCarOvertakeInformation) {
        template.convertAndSend("speed.safetyCar.overtake", safetyCarOvertakeInformation);
    }
}
