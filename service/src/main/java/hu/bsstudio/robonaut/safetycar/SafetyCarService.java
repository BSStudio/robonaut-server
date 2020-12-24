package hu.bsstudio.robonaut.safetycar;

import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation;
import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import reactor.core.publisher.Mono;

public interface SafetyCarService {
    Mono<DetailedTeam> safetyCarWasFollowed(final SafetyCarFollowInformation safetyCarFollowInformation);

    Mono<DetailedTeam> safetyCarWasOvertaken(final SafetyCarOvertakeInformation safetyCarOvertakeInformation);
}
