package hu.bsstudio.robonaut.race.skill.timer

import hu.bsstudio.robonaut.common.model.TimerAction
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class DefaultSkillTimerServiceTest {
  private lateinit var underTest: DefaultSkillTimerService

  @BeforeEach
  internal fun setUp() {
    underTest = DefaultSkillTimerService()
  }

  @Test
  internal fun `should return SkillTimer when timer is updated`() {
    Mono
      .just(SKILL_TIMER)
      .flatMap(underTest::updateTimer)
      .let(StepVerifier::create)
      .expectNext(SKILL_TIMER)
      .verifyComplete()
  }

  companion object {
    private val SKILL_TIMER = SkillTimer(2020, TimerAction.START)
  }
}
