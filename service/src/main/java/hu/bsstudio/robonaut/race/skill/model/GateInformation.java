package hu.bsstudio.robonaut.race.skill.model;

import lombok.Value;

@Value
public class GateInformation {
    long teamId;
    int bonusTime;
    int timeLeft;
    int skillScore;
    int totalSkillScore;
}
