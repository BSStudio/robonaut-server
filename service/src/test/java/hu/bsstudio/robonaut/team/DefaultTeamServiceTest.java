package hu.bsstudio.robonaut.team;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.entity.TeamType;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
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
    public static final TeamEntity TEAM_ENTITY_1 = createTeamEntity(TEAM_ID_1, YEAR_1, TEAM_NAME_1, TEAM_MEMBERS_1, TEAM_TYPE_1);
    public static final TeamEntity OLD_TEAM_ENTITY = createTeamEntity(TEAM_ID_1, YEAR_2, TEAM_NAME_2, TEAM_MEMBERS_2, TEAM_TYPE_2);
    private static final Team TEAM_1 = Team.builder()
        .teamId(TEAM_ID_1)
        .teamMembers(TEAM_MEMBERS_1)
        .teamName(TEAM_NAME_1)
        .teamType(TEAM_TYPE_1)
        .year(YEAR_1)
        .build();

    private DefaultTeamService underTest;

    @Mock
    private TeamRepository mockRepository;
    @Mock
    private TeamModelEntityMapper mockMapper;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.underTest = new DefaultTeamService(mockRepository);
        this.underTest.setTeamMapper(mockMapper);
    }

    @Test
    void shouldReturnCreatedTeam() {
        when(mockRepository.insert(TEAM_ENTITY_1)).thenReturn(Mono.just(TEAM_ENTITY_1));
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockMapper.toModel(TEAM_ENTITY_1)).thenReturn(detailedTeam);

        final var result = underTest.addTeam(TEAM_1);

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete();
    }

    @Test
    void shouldReturnUpdatedTeam() {
        when(mockRepository.findById(TEAM_ID_1)).thenReturn(Mono.just(OLD_TEAM_ENTITY));
        final var updatedTeamEntity = TEAM_ENTITY_1;
        when(mockRepository.save(updatedTeamEntity)).thenReturn(Mono.just(updatedTeamEntity));
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockMapper.toModel(updatedTeamEntity)).thenReturn(detailedTeam);

        final var result = underTest.updateTeam(TEAM_1);

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete();
    }

    @Test
    void shouldReturnAllTeam() {
        final var teamEntity1 = new TeamEntity();
        teamEntity1.setTeamId(TEAM_ID_1);
        final var teamEntity2 = new TeamEntity();
        teamEntity2.setTeamId(TEAM_ID_2);
        when(mockRepository.findAll()).thenReturn(Flux.just(teamEntity1, teamEntity2));
        final var detailedTeam1 = DetailedTeam.builder().teamId(TEAM_ID_1).build();
        final var detailedTeam2 = DetailedTeam.builder().teamId(TEAM_ID_2).build();
        when(mockMapper.toModel(teamEntity1)).thenReturn(detailedTeam1);
        when(mockMapper.toModel(teamEntity2)).thenReturn(detailedTeam2);

        final var result = underTest.findAllTeam();

        StepVerifier.create(result)
            .expectNext(detailedTeam1)
            .expectNext(detailedTeam2)
            .verifyComplete();
    }

    private static TeamEntity createTeamEntity(final Long teamId, final int year, final String teamName,
                                               final List<String> teamMembers, final TeamType teamType) {
        final var teamEntity = new TeamEntity();
        teamEntity.setTeamId(teamId);
        teamEntity.setYear(year);
        teamEntity.setTeamName(teamName);
        teamEntity.setTeamMembers(teamMembers);
        teamEntity.setTeamType(teamType);
        teamEntity.setSpeedTimes(Collections.emptyList());
        return teamEntity;
    }
}
