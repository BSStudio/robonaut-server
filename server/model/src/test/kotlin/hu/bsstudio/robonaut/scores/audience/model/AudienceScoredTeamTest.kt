package hu.bsstudio.robonaut.scores.audience.model

import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class AudienceScoredTeamTest(
  @param:Autowired private val underTest: JacksonTester<AudienceScoredTeam>,
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
      AudienceScoredTeam(
        teamId = 33L,
        votes = 1500,
        audienceScore = 50,
      )
    private val JSON =
      """
      {
          "teamId": 33,
          "votes": 1500,
          "audienceScore": 50
      }
      """.trimIndent()
  }
}
