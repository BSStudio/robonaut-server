package hu.bsstudio.scores.endresult.model;

import lombok.Value;

@Value
public class EndResultedTeam {
    long teamId;
    int totalScore;
    int rank;
    int juniorRank;
}
