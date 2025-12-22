package hu.bsstudio.robonaut.scores.qualification.model

import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class QualifiedTeamTest(
  @param:Autowired private val underTest: JacksonTester<QualifiedTeam>,
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
      QualifiedTeam(
        teamId = 55L,
        qualificationScore = 200,
      )
    private val JSON =
      """
      {
          "teamId": 55,
          "qualificationScore": 200
      }
      """.trimIndent()
  }
}
