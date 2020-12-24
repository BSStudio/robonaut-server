package hu.bsstudio.robonaut.team.mapper;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.team.model.DetailedTeam;

public class TeamModelEntityMapper {

    public DetailedTeam toModel(final TeamEntity teamEntity) {
        return DetailedTeam.builder()
            .teamId(teamEntity.getTeamId())
            .year(teamEntity.getYear())
            .teamName(teamEntity.getTeamName())
            .teamMembers(teamEntity.getTeamMembers())
            .teamType(teamEntity.getTeamType())
            .skillScore(teamEntity.getSkillScore())
            .speedScore(teamEntity.getSpeedScore())
            .speedBonusScore(teamEntity.getSpeedBonusScore())
            .numberOfOvertakes(teamEntity.getNumberOfOvertakes())
            .safetyCarWasFollowed(teamEntity.isSafetyCarWasFollowed())
            .speedTimes(teamEntity.getSpeedTimes())
            .votes(teamEntity.getVotes())
            .audienceScore(teamEntity.getAudienceScore())
            .qualificationScore(teamEntity.getQualificationScore())
            .totalScore(teamEntity.getTotalScore())
            .rank(teamEntity.getRank())
            .juniorRank(teamEntity.getJuniorRank())
            .build();
    }
}
