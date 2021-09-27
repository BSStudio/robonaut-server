package hu.bsstudio.robonaut.team;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.entity.ScoreEntity;
import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.entity.TeamType;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import hu.bsstudio.robonaut.team.model.Score;
import hu.bsstudio.robonaut.team.model.Team;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class DefaultTeamServiceTest {

    private static final long TEAM_ID_1 = 42;
    private static final long TEAM_ID_2 = 43;
    private static final List<String> TEAM_MEMBERS_1 = List.of("Bence", "Boldi");
    private static final List<String> TEAM_MEMBERS_2 = List.of("Bence", "Boldi", "Máté");
    private static final String TEAM_NAME_1 = "BSS";
    private static final String TEAM_NAME_2 = "Budavári Schönherz Stúdió";
    private static final TeamType TEAM_TYPE_1 = TeamType.SENIOR;
    private static final TeamType TEAM_TYPE_2 = TeamType.JUNIOR;
    private static final int YEAR_1 = 2020;
    private static final int YEAR_2 = 2021;
    private static final TeamEntity OLD_TEAM_ENTITY = createTeamEntity(TEAM_ID_1, YEAR_2, TEAM_NAME_2, TEAM_MEMBERS_2, TEAM_TYPE_2);
    private static final TeamEntity TEAM_ENTITY_1 = createTeamEntity(TEAM_ID_1, YEAR_1, TEAM_NAME_1, TEAM_MEMBERS_1, TEAM_TYPE_1);
    private static final TeamEntity TEAM_ENTITY_2 = createTeamEntity(TEAM_ID_2, YEAR_2, TEAM_NAME_2, TEAM_MEMBERS_2, TEAM_TYPE_2);
    private static final Team TEAM_1 = Team.builder()
        .teamId(TEAM_ID_1)
        .teamMembers(TEAM_MEMBERS_1)
        .teamName(TEAM_NAME_1)
        .teamType(TEAM_TYPE_1)
        .year(YEAR_1)
        .build();
    private static final DetailedTeam DETAILED_TEAM_1 = DetailedTeam.builder()
        .teamId(TEAM_ID_1)
        .combinedScore(new Score(0, 0, 0))
        .juniorScore(new Score(0, 0, 0))
        .build();
    private static final DetailedTeam DETAILED_TEAM_2 = DetailedTeam.builder().teamId(TEAM_ID_2).build();

    private DefaultTeamService underTest;

    @Mock
    private TeamRepository mockRepository;
    @Mock
    private TeamModelEntityMapper mockMapper;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new DefaultTeamService(mockRepository);
        this.underTest.setTeamMapper(mockMapper);
    }

    @Test
    void shouldReturnCreatedTeam() {
        when(mockRepository.insert(TEAM_ENTITY_1)).thenReturn(Mono.just(TEAM_ENTITY_1));
        when(mockMapper.toModel(TEAM_ENTITY_1)).thenReturn(DETAILED_TEAM_1);

        final var result = underTest.addTeam(TEAM_1);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .verifyComplete();
    }

    @Test
    void shouldReturnUpdatedTeam() {
        when(mockRepository.findById(TEAM_ID_1)).thenReturn(Mono.just(OLD_TEAM_ENTITY));
        when(mockRepository.save(TEAM_ENTITY_1)).thenReturn(Mono.just(TEAM_ENTITY_1));
        when(mockMapper.toModel(TEAM_ENTITY_1)).thenReturn(DETAILED_TEAM_1);

        final var result = underTest.updateTeam(TEAM_1);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .verifyComplete();
    }

    @Test
    void shouldReturnUpdatedTeamByAdmin() {
        when(mockMapper.toEntity(DETAILED_TEAM_1)).thenReturn(TEAM_ENTITY_1);
        when(mockRepository.save(TEAM_ENTITY_1)).thenReturn(Mono.just(TEAM_ENTITY_1));
        when(mockMapper.toModel(TEAM_ENTITY_1)).thenReturn(DETAILED_TEAM_1);

        final var result = underTest.updateTeam(DETAILED_TEAM_1);

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .verifyComplete();
    }

    @Test
    void shouldReturnAllTeam() {
        when(mockRepository.findAll()).thenReturn(Flux.just(TEAM_ENTITY_1, TEAM_ENTITY_2));
        when(mockMapper.toModel(TEAM_ENTITY_1)).thenReturn(DETAILED_TEAM_1);
        when(mockMapper.toModel(TEAM_ENTITY_2)).thenReturn(DETAILED_TEAM_2);

        final var result = underTest.findAllTeam();

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .expectNext(DETAILED_TEAM_2)
            .verifyComplete();
    }

    private static TeamEntity createTeamEntity(final Long teamId, final int year, final String teamName,
                                               final List<String> teamMembers, final TeamType teamType) {
        final var defaultScore = new ScoreEntity();
        final var teamEntity = new TeamEntity();
        teamEntity.setTeamId(teamId);
        teamEntity.setYear(year);
        teamEntity.setTeamName(teamName);
        teamEntity.setTeamMembers(teamMembers);
        teamEntity.setTeamType(teamType);
        teamEntity.setSpeedTimes(Collections.emptyList());
        teamEntity.setScore(defaultScore);
        teamEntity.setJuniorScore(defaultScore);
        return teamEntity;
    }
}
