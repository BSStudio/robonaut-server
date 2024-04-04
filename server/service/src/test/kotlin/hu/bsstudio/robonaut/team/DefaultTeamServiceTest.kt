package hu.bsstudio.robonaut.team

import hu.bsstudio.robonaut.entity.ScoreEntity
import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.entity.TeamType
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import hu.bsstudio.robonaut.team.model.Score
import hu.bsstudio.robonaut.team.model.Team
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class DefaultTeamServiceTest {
    @MockK
    private lateinit var mockRepository: TeamRepository

    @MockK
    private lateinit var mockMapper: TeamModelEntityMapper

    private lateinit var underTest: DefaultTeamService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = DefaultTeamService(mockRepository)
        underTest = DefaultTeamService(mockRepository, mockMapper)
    }

    @Test
    internal fun `should return created Team`() {
        every { mockRepository.insert(TEAM_ENTITY_1) } returns Mono.just(TEAM_ENTITY_1)
        every { mockMapper.toModel(TEAM_ENTITY_1) } returns DETAILED_TEAM_1

        val result = underTest.addTeam(TEAM_1)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .verifyComplete()
    }

    @Test
    internal fun `should return updated Team`() {
        every { mockRepository.findById(TEAM_ID_1) } returns Mono.just(OLD_TEAM_ENTITY)
        every { mockRepository.save(TEAM_ENTITY_1) } returns Mono.just(TEAM_ENTITY_1)
        every { mockMapper.toModel(TEAM_ENTITY_1) } returns DETAILED_TEAM_1

        val result = underTest.updateTeam(TEAM_1)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .verifyComplete()
    }

    @Test
    internal fun `should return updated Team by admin`() {
        every { mockMapper.toEntity(DETAILED_TEAM_1) } returns TEAM_ENTITY_1
        every { mockRepository.save(TEAM_ENTITY_1) } returns Mono.just(TEAM_ENTITY_1)
        every { mockMapper.toModel(TEAM_ENTITY_1) } returns DETAILED_TEAM_1

        val result = underTest.updateTeam(DETAILED_TEAM_1)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .verifyComplete()
    }

    @Test
    internal fun `should return all Team`() {
        every { mockRepository.findAll() } returns Flux.just(TEAM_ENTITY_1, TEAM_ENTITY_2)
        every { mockMapper.toModel(TEAM_ENTITY_1) } returns DETAILED_TEAM_1
        every { mockMapper.toModel(TEAM_ENTITY_2) } returns DETAILED_TEAM_2

        val result = underTest.findAllTeam()

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .expectNext(DETAILED_TEAM_2)
            .verifyComplete()
    }

    companion object {
        private const val TEAM_ID_1: Long = 42
        private const val TEAM_ID_2: Long = 43
        private val TEAM_MEMBERS_1 = listOf("Bence", "Boldi")
        private val TEAM_MEMBERS_2 = listOf("Bence", "Boldi", "Máté")
        private const val TEAM_NAME_1 = "BSS"
        private const val TEAM_NAME_2 = "Budavári Schönherz Stúdió"
        private val TEAM_TYPE_1 = TeamType.SENIOR
        private val TEAM_TYPE_2 = TeamType.JUNIOR
        private const val YEAR_1 = 2020
        private const val YEAR_2 = 2021
        private val OLD_TEAM_ENTITY = createTeamEntity(TEAM_ID_1, YEAR_2, TEAM_NAME_2, TEAM_MEMBERS_2, TEAM_TYPE_2)
        private val TEAM_ENTITY_1 = createTeamEntity(TEAM_ID_1, YEAR_1, TEAM_NAME_1, TEAM_MEMBERS_1, TEAM_TYPE_1)
        private val TEAM_ENTITY_2 = createTeamEntity(TEAM_ID_2, YEAR_2, TEAM_NAME_2, TEAM_MEMBERS_2, TEAM_TYPE_2)
        private val TEAM_1: Team =
            Team(
                teamId = TEAM_ID_1,
                teamMembers = TEAM_MEMBERS_1,
                teamName = TEAM_NAME_1,
                teamType = TEAM_TYPE_1,
                year = YEAR_1,
            )
        private val DETAILED_TEAM_1: DetailedTeam =
            DetailedTeam(
                teamId = TEAM_ID_1,
                combinedScore = Score(0, 0, 0),
                juniorScore = Score(0, 0, 0),
            )
        private val DETAILED_TEAM_2 = DetailedTeam(teamId = TEAM_ID_2)

        private fun createTeamEntity(
            teamId: Long,
            year: Int,
            teamName: String,
            teamMembers: List<String>,
            teamType: TeamType,
        ): TeamEntity {
            val defaultScore = ScoreEntity()
            return TeamEntity(
                teamId = teamId,
                year = year,
                teamName = teamName,
                teamMembers = teamMembers,
                teamType = teamType,
                speedTimes = emptyList(),
                score = defaultScore,
                juniorScore = defaultScore,
            )
        }
    }
}
