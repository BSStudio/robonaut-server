package hu.bsstudio.robonaut.scores.endresult.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class EndResultedTeamTest(
  @param:Autowired private val underTest: JacksonTester<EndResultedTeam>,
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
      EndResultedTeam(
        teamId = 21L,
        totalScore = 850,
      )
    private val JSON =
      """
      {
          "teamId": 21,
          "totalScore": 850
      }
      """.trimIndent()
  }
}
