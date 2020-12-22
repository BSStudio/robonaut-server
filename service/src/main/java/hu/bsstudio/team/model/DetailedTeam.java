package hu.bsstudio.team.model;

import hu.bsstudio.entity.TeamType;
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
    int technicalScore;
    int speedScore;
    int speedBonusScore;
    int numberOfOvertakes;
    boolean safetyCarWasFollowed;
    List<Integer> speedTimes;
    int votes;
    int audienceScore;
    int qualificationScore;
    int totalScore;
    int rank;
    int juniorRank;
}
