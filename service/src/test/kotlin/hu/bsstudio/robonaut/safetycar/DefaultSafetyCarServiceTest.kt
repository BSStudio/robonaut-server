package hu.bsstudio.robonaut.safetycar

import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation
import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class DefaultSafetyCarServiceTest {

    @MockK
    private lateinit var mockRepository: TeamRepository

    @MockK
    private lateinit var mockMapper: TeamModelEntityMapper

    private lateinit var underTest: DefaultSafetyCarService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        underTest = DefaultSafetyCarService(mockRepository)
        underTest.mapper = mockMapper
    }

    @Test
    fun shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdatedWhenSafetyCarWasFollowed() {
        val foundTeamEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)
        val updatedTeamEntity = TeamEntity(safetyCarWasFollowed = SAFETY_CAR_FOLLOWED)
        every { mockRepository.save(updatedTeamEntity) } returns Mono.just(updatedTeamEntity)
        val detailedTeam = DetailedTeam()
        every { mockMapper.toModel(updatedTeamEntity) } returns detailedTeam

        val result = underTest.safetyCarWasFollowed(FOLLOW_INFORMATION)

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyWhenEntityWasNotFoundWhenSafetyCarWasFollowed() {
        val foundTeamEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)
        val updatedTeamEntity = TeamEntity()
        updatedTeamEntity.safetyCarWasFollowed = SAFETY_CAR_FOLLOWED
        every { mockRepository.save(updatedTeamEntity) } returns Mono.empty()

        val result = underTest.safetyCarWasFollowed(FOLLOW_INFORMATION)

        StepVerifier.create(result)
            .verifyComplete()
    }

    @Test
    fun shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdatedWhenSafetyCarWasOvertaken() {
        val foundTeamEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)
        val updatedTeamEntity = TeamEntity()
        updatedTeamEntity.numberOfOvertakes = NUMBER_OF_OVERTAKES
        every { mockRepository.save(updatedTeamEntity) } returns Mono.just(updatedTeamEntity)
        val detailedTeam = DetailedTeam()
        every { mockMapper.toModel(updatedTeamEntity) } returns detailedTeam

        val result = underTest.safetyCarWasOvertaken(SAFETY_CAR_OVERTAKE_INFORMATION)

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyWhenEntityWasNotFoundWhenSafetyCarWasOvertaken() {
        val foundTeamEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)
        val updatedTeamEntity = TeamEntity()
        updatedTeamEntity.numberOfOvertakes = NUMBER_OF_OVERTAKES
        every { mockRepository.save(updatedTeamEntity) } returns Mono.empty()
        val result = underTest.safetyCarWasOvertaken(SAFETY_CAR_OVERTAKE_INFORMATION)
        StepVerifier.create(result)
            .verifyComplete()
    }

    companion object {
        private const val TEAM_ID: Long = 42
        private const val SAFETY_CAR_FOLLOWED = true
        private const val NUMBER_OF_OVERTAKES = 5
        private val FOLLOW_INFORMATION = SafetyCarFollowInformation(TEAM_ID, SAFETY_CAR_FOLLOWED)
        private val SAFETY_CAR_OVERTAKE_INFORMATION = SafetyCarOvertakeInformation(TEAM_ID, NUMBER_OF_OVERTAKES)
    }
}
