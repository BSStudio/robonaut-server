package hu.bsstudio.team.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import hu.bsstudio.entity.TeamEntity;
import hu.bsstudio.entity.TeamType;
import hu.bsstudio.team.model.DetailedTeam;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class TeamModelEntityMapperTest {

    private static final long TEAM_ID = 42;
    private static final int YEAR = 2020;
    private static final String TEAM_NAME = "BSS";
    private static final List<String> TEAM_MEMBERS = List.of("Bence", "Boldi");
    private static final TeamType TEAM_TYPE = TeamType.SENIOR;
    private static final int SKILL_SCORE = 420;
    private static final int SPEED_SCORE = 16;
    private static final int SPEED_BONUS_SCORE = 8;
    private static final int NUMBER_OF_OVERTAKES = 1;
    private static final boolean SAFETY_CAR_WAS_FOLLOWED = true;
    private static final List<Integer> SPEED_TIMES = List.of(1, 2);
    private static final int VOTES = 999;
    private static final int AUDIENCE_SCORE = 4;
    private static final int QUALIFICATION_SCORE = 444;
    private static final int TOTAL_SCORE = Integer.MAX_VALUE;
    private static final int RANK = 2;
    private static final int JUNIOR_RANK = -1;
    private TeamModelEntityMapper underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new TeamModelEntityMapper();
    }

    @Test
    void shouldReturnMappedDetailedTeam() {
        final var teamEntity = new TeamEntity();
        teamEntity.setTeamId(TEAM_ID);
        teamEntity.setYear(YEAR);
        teamEntity.setTeamName(TEAM_NAME);
        teamEntity.setTeamMembers(TEAM_MEMBERS);
        teamEntity.setTeamType(TEAM_TYPE);
        teamEntity.setSkillScore(SKILL_SCORE);
        teamEntity.setSpeedScore(SPEED_SCORE);
        teamEntity.setSpeedBonusScore(SPEED_BONUS_SCORE);
        teamEntity.setNumberOfOvertakes(NUMBER_OF_OVERTAKES);
        teamEntity.setSafetyCarWasFollowed(SAFETY_CAR_WAS_FOLLOWED);
        teamEntity.setSpeedTimes(SPEED_TIMES);
        teamEntity.setVotes(VOTES);
        teamEntity.setAudienceScore(AUDIENCE_SCORE);
        teamEntity.setQualificationScore(QUALIFICATION_SCORE);
        teamEntity.setTotalScore(TOTAL_SCORE);
        teamEntity.setRank(RANK);
        teamEntity.setJuniorRank(JUNIOR_RANK);

        final var detailedTeam = underTest.toModel(teamEntity);

        final var expected = DetailedTeam.builder()
            .teamId(TEAM_ID)
            .year(YEAR)
            .teamName(TEAM_NAME)
            .teamMembers(TEAM_MEMBERS)
            .teamType(TEAM_TYPE)
            .skillScore(SKILL_SCORE)
            .speedScore(SPEED_SCORE)
            .speedBonusScore(SPEED_BONUS_SCORE)
            .numberOfOvertakes(NUMBER_OF_OVERTAKES)
            .safetyCarWasFollowed(SAFETY_CAR_WAS_FOLLOWED)
            .speedTimes(SPEED_TIMES)
            .votes(VOTES)
            .audienceScore(AUDIENCE_SCORE)
            .qualificationScore(QUALIFICATION_SCORE)
            .totalScore(TOTAL_SCORE)
            .rank(RANK)
            .juniorRank(JUNIOR_RANK)
            .build();
        assertThat(detailedTeam).isEqualTo(expected);
    }
}
