package hu.bsstudio.robonaut.race.skill

import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.race.skill.model.GateInformation
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class DefaultSkillRaceServiceTest {
    @MockK
    private lateinit var mockRepository: TeamRepository

    @MockK
    private lateinit var mockMapper: TeamModelEntityMapper

    private lateinit var underTest: DefaultSkillRaceService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = DefaultSkillRaceService(mockRepository)
        underTest = DefaultSkillRaceService(mockRepository, mockMapper)
    }

    @Test
    internal fun `should return DetailedTeam when entity was found and successfully was updated`() {
        val foundTeamEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)
        val updatedTeamEntity = TeamEntity()
        updatedTeamEntity.skillScore = TOTAL_SKILL_SCORE
        every { mockRepository.save(updatedTeamEntity) } returns Mono.just(updatedTeamEntity)
        val detailedTeam = DetailedTeam()
        every { mockMapper.toModel(updatedTeamEntity) } returns detailedTeam

        val result = underTest.updateSkillRaceResultOnGate(GATE_INFO)

        StepVerifier
            .create(result)
            .expectNext(detailedTeam)
            .verifyComplete()
    }

    @Test
    internal fun `should return empty when entity was not found`() {
        every { mockRepository.findById(TEAM_ID) } returns Mono.empty()

        val result = underTest.updateSkillRaceResultOnGate(GATE_INFO)

        StepVerifier
            .create(result)
            .verifyComplete()
    }

    @Test
    internal fun `should return DetailedTeam when entity was found and successfully was updated when RaceResult was submitted`() {
        val foundTeamEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)
        val updatedTeamEntity = TeamEntity()
        updatedTeamEntity.skillScore = SKILL_SCORE
        every { mockRepository.save(updatedTeamEntity) } returns Mono.just(updatedTeamEntity)
        val detailedTeam = DetailedTeam()
        every { mockMapper.toModel(updatedTeamEntity) } returns detailedTeam

        val result = underTest.updateSkillRaceResult(SKILL_RACE_RESULT)

        StepVerifier
            .create(result)
            .expectNext(detailedTeam)
            .verifyComplete()
    }

    @Test
    internal fun `should return empty when entity was not found when RaceResult was submitted`() {
        every { mockRepository.findById(TEAM_ID) } returns Mono.empty()

        val result = underTest.updateSkillRaceResult(SKILL_RACE_RESULT)

        StepVerifier
            .create(result)
            .verifyComplete()
    }

    companion object {
        private const val TEAM_ID: Long = 2020
        private const val BONUS_TIME = 42
        private const val TIME_LEFT = 4321
        private const val SKILL_SCORE = 1234
        private const val TOTAL_SKILL_SCORE = 9999
        private val GATE_INFO = GateInformation(TEAM_ID, BONUS_TIME, TIME_LEFT, SKILL_SCORE, TOTAL_SKILL_SCORE)
        private val SKILL_RACE_RESULT = SkillRaceResult(TEAM_ID, SKILL_SCORE)
    }
}
