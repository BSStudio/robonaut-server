package hu.bsstudio.robonaut.team.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.entity.TeamType;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
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

    private static final TeamEntity TEAM_ENTITY = createTeamEntity();
    private static final DetailedTeam DETAILED_TEAM = createDetailedTeam();

    private TeamModelEntityMapper underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new TeamModelEntityMapper();
    }

    @Test
    void shouldReturnMappedDetailedTeam() {

        final var result = underTest.toModel(TEAM_ENTITY);

        assertThat(result).isEqualTo(DETAILED_TEAM);
    }

    @Test
    void shouldReturnMapperEntity() {

        final var result = underTest.toEntity(DETAILED_TEAM);

        assertThat(result).isEqualTo(TEAM_ENTITY);
    }

    private static DetailedTeam createDetailedTeam() {
        return DetailedTeam.builder()
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
    }

    private static TeamEntity createTeamEntity() {
        final var entity = new TeamEntity();
        entity.setTeamId(TEAM_ID);
        entity.setYear(YEAR);
        entity.setTeamName(TEAM_NAME);
        entity.setTeamMembers(TEAM_MEMBERS);
        entity.setTeamType(TEAM_TYPE);
        entity.setSkillScore(SKILL_SCORE);
        entity.setSpeedScore(SPEED_SCORE);
        entity.setSpeedBonusScore(SPEED_BONUS_SCORE);
        entity.setNumberOfOvertakes(NUMBER_OF_OVERTAKES);
        entity.setSafetyCarWasFollowed(SAFETY_CAR_WAS_FOLLOWED);
        entity.setSpeedTimes(SPEED_TIMES);
        entity.setVotes(VOTES);
        entity.setAudienceScore(AUDIENCE_SCORE);
        entity.setQualificationScore(QUALIFICATION_SCORE);
        entity.setTotalScore(TOTAL_SCORE);
        entity.setRank(RANK);
        entity.setJuniorRank(JUNIOR_RANK);
        return entity;
    }
}
