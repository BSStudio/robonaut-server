package hu.bsstudio.robonaut.race.speed.timer.model

import hu.bsstudio.robonaut.common.model.TimerAction
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class SpeedTimerTest(
  @param:Autowired private val underTest: JacksonTester<SpeedTimer>,
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
      SpeedTimer(
        timerAt = 180,
        timerAction = TimerAction.STOP,
      )
    private val JSON =
      """
      {
          "timerAt": 180,
          "timerAction": "STOP"
      }
      """.trimIndent()
  }
}
