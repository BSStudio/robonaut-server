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

    public TeamEntity toEntity(final DetailedTeam detailedTeam) {
        final var entity = new TeamEntity();
        entity.setTeamId(detailedTeam.getTeamId());
        entity.setYear(detailedTeam.getYear());
        entity.setTeamName(detailedTeam.getTeamName());
        entity.setTeamMembers(detailedTeam.getTeamMembers());
        entity.setTeamType(detailedTeam.getTeamType());
        entity.setSkillScore(detailedTeam.getSkillScore());
        entity.setSpeedScore(detailedTeam.getSpeedScore());
        entity.setSpeedBonusScore(detailedTeam.getSpeedBonusScore());
        entity.setNumberOfOvertakes(detailedTeam.getNumberOfOvertakes());
        entity.setSafetyCarWasFollowed(detailedTeam.isSafetyCarWasFollowed());
        entity.setSpeedTimes(detailedTeam.getSpeedTimes());
        entity.setVotes(detailedTeam.getVotes());
        entity.setAudienceScore(detailedTeam.getAudienceScore());
        entity.setQualificationScore(detailedTeam.getQualificationScore());
        entity.setTotalScore(detailedTeam.getTotalScore());
        entity.setRank(detailedTeam.getRank());
        entity.setJuniorRank(detailedTeam.getJuniorRank());
        return entity;
    }
}
