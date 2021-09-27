package hu.bsstudio.robonaut.team.query;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.team.TeamService;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import hu.bsstudio.robonaut.team.query.model.Requester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;

final class QueryTeamDataTest {

    private static final Requester REQUESTER = new Requester("Bence");

    private QueryTeamData underTest;

    @Mock
    private TeamService mockService;
    @Mock
    private Flux<DetailedTeam> detailedTeamFlux;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new QueryTeamData(mockService);
    }

    @Test
    void shouldCallFindAll() {
        when(mockService.findAllTeam()).thenReturn(detailedTeamFlux);

        underTest.sendTeamData(REQUESTER);

        verify(mockService).findAllTeam();
        verify(detailedTeamFlux).subscribe();
    }
}
