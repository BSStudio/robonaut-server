package hu.bsstudio.safetycar;

import hu.bsstudio.safetycar.model.SafetyCarFollowInformation;
import hu.bsstudio.safetycar.model.SafetyCarOvertakeInformation;
import hu.bsstudio.team.model.DetailedTeam;
import reactor.core.publisher.Mono;

public interface SafetyCarService {
    Mono<DetailedTeam> safetyCarWasFollowed(final SafetyCarFollowInformation safetyCarFollowInformation);

    Mono<DetailedTeam> safetyCarWasOvertaken(final SafetyCarOvertakeInformation safetyCarOvertakeInformation);
}
