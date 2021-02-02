package hu.bsstudio.robonaut.team.mapper;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import java.awt.image.MultiPixelPackedSampleModel;
import lombok.AccessLevel;
import lombok.Setter;

public class TeamModelEntityMapper {

    @Setter(AccessLevel.PACKAGE)
    private ScoreEntityMapper scoreEntityMapper = new ScoreEntityMapper();

    public DetailedTeam toModel(final TeamEntity teamEntity) {
        return DetailedTeam.builder()
            .teamId(teamEntity.getTeamId())
            .year(teamEntity.getYear())
            .teamName(teamEntity.getTeamName())
            .teamMembers(teamEntity.getTeamMembers())
            .teamType(teamEntity.getTeamType())
            .skillScore(teamEntity.getSkillScore())
            .numberOfOvertakes(teamEntity.getNumberOfOvertakes())
            .safetyCarWasFollowed(teamEntity.isSafetyCarWasFollowed())
            .speedTimes(teamEntity.getSpeedTimes())
            .votes(teamEntity.getVotes())
            .audienceScore(teamEntity.getAudienceScore())
            .qualificationScore(teamEntity.getQualificationScore())
            .combinedScore(scoreEntityMapper.toModel(teamEntity.getScore()))
            .juniorScore(scoreEntityMapper.toModel(teamEntity.getJuniorScore()))
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
        entity.setNumberOfOvertakes(detailedTeam.getNumberOfOvertakes());
        entity.setSafetyCarWasFollowed(detailedTeam.isSafetyCarWasFollowed());
        entity.setSpeedTimes(detailedTeam.getSpeedTimes());
        entity.setVotes(detailedTeam.getVotes());
        entity.setAudienceScore(detailedTeam.getAudienceScore());
        entity.setQualificationScore(detailedTeam.getQualificationScore());
        entity.setScore(scoreEntityMapper.toEntity(detailedTeam.getCombinedScore()));
        entity.setJuniorScore(scoreEntityMapper.toEntity(detailedTeam.getJuniorScore()));
        return entity;
    }
}
