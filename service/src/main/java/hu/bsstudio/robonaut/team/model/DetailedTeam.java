package hu.bsstudio.robonaut.team.model;

import hu.bsstudio.robonaut.entity.TeamType;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class DetailedTeam {
    long teamId;
    int year;
    String teamName;
    List<String> teamMembers;
    TeamType teamType;

    int skillScore;

    int numberOfOvertakes;
    boolean safetyCarWasFollowed;

    int speedScore;
    int speedBonusScore;
    List<Integer> speedTimes;

    int votes;
    int audienceScore;

    int qualificationScore;

    int totalScore;
    int rank;
    int juniorRank;
}
