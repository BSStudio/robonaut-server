package hu.bsstudio.robonaut.team.model;

import hu.bsstudio.robonaut.entity.TeamType;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class Team {
    long teamId;
    int year;
    String teamName;
    List<String> teamMembers;
    TeamType teamType;
}
