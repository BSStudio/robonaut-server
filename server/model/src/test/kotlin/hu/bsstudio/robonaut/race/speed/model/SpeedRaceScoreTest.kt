package hu.bsstudio.robonaut.race.speed.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class SpeedRaceScoreTest(
  @param:Autowired private val underTest: JacksonTester<SpeedRaceScore>,
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
      SpeedRaceScore(
        teamId = 15L,
        speedTimes = listOf(52000, 51500, 53000),
      )
    private val JSON =
      """
      {
          "teamId": 15,
          "speedTimes": [52000, 51500, 53000]
      }
      """.trimIndent()
  }
}
