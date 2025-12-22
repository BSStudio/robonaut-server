package hu.bsstudio.robonaut.race.skill.timer.model

import hu.bsstudio.robonaut.common.model.TimerAction
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class SkillTimerTest(
  @param:Autowired private val underTest: JacksonTester<SkillTimer>,
) {
  @Test
  internal fun `test serialisation`() {
    val actual = this.underTest.write(MODEL)

    actual.json shouldEqualJson JSON
  }

  @Test
  internal fun `test deserialization`() {
    val actual = this.underTest.parseObject(JSON)

    actual shouldBeEqual MODEL
  }

  private companion object {
    private val MODEL =
      SkillTimer(
        timerAt = 300,
        timerAction = TimerAction.START,
      )
    private val JSON =
      """
      {
          "timerAt": 300,
          "timerAction": "START"
      }
      """.trimIndent()
  }
}
