package hu.bsstudio.robonaut.race.skill.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class GateInformationTest(
  @param:Autowired private val underTest: JacksonTester<GateInformation>,
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
      GateInformation(
        teamId = 123L,
        bonusTime = 456,
        timeLeft = 420,
        skillScore = 5,
        totalSkillScore = 15,
      )
    private val JSON =
      """
      {
          "teamId": 123,
          "bonusTime": 456,
          "timeLeft": 420,
          "skillScore": 5,
          "totalSkillScore": 15
      }
      """.trimIndent()
  }
}
