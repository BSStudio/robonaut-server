package hu.bsstudio.scores.audience.model;

import lombok.Value;

@Value
public class AudienceScoredTeam {
    long teamId;
    int votes;
    int audienceScore;
}
