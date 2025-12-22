package hu.bsstudio.robonaut.race.speed.model

import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class SpeedRaceResultTest(
  @param:Autowired private val underTest: JacksonTester<SpeedRaceResult>,
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
      SpeedRaceResult(
        teamId = 7L,
        speedScore = 250,
        bestSpeedTime = 45000,
        speedTimes = listOf(45000, 47000, 46500),
      )
    private val JSON =
      """
      {
          "teamId": 7,
          "speedScore": 250,
          "bestSpeedTime": 45000,
          "speedTimes": [45000, 47000, 46500]
      }
      """.trimIndent()
  }
}
