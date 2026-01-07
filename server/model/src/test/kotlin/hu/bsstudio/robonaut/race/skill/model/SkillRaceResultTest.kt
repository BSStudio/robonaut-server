package hu.bsstudio.robonaut.race.skill.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class SkillRaceResultTest(
  @param:Autowired private val underTest: JacksonTester<SkillRaceResult>,
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
      SkillRaceResult(
        teamId = 42L,
        skillScore = 100,
      )
    private val JSON =
      """
      {
          "teamId": 42,
          "skillScore": 100
      }
      """.trimIndent()
  }
}
