package hu.bsstudio.robonaut.team.query

import hu.bsstudio.robonaut.team.TeamService
import hu.bsstudio.robonaut.team.model.DetailedTeam
import hu.bsstudio.robonaut.team.query.model.Requester
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.Disposable
import reactor.core.publisher.Flux

internal class QueryTeamDataTest {

    @MockK
    private lateinit var mockService: TeamService

    @MockK
    private lateinit var detailedTeamFlux: Flux<DetailedTeam>

    private lateinit var underTest: QueryTeamData

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = QueryTeamData(mockService)
    }

    @Test
    internal fun shouldCallFindAll() {
        every { mockService.findAllTeam() } returns detailedTeamFlux
        every { detailedTeamFlux.subscribe() } returns Disposable { }

        underTest.sendTeamData(REQUESTER)
    }

    companion object {
        private val REQUESTER = Requester("Bence")
    }
}
