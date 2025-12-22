package hu.bsstudio.robonaut.team.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class ScoreTest(
  @param:Autowired private val underTest: JacksonTester<Score>,
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

  @Test
  internal fun `test default serialisation`() {
    val actual = this.underTest.write(DEFAULT_MODEL)

    actual.json shouldEqualJson DEFAULT_JSON
  }

  @Test
  internal fun `test default deserialization`() {
    val actual = this.underTest.parseObject(DEFAULT_JSON)

    actual shouldBeEqual DEFAULT_MODEL
  }

  @Test
  internal fun `test empty deserialization`() {
    val actual = this.underTest.parseObject(EMPTY_JSON)

    actual shouldBeEqual DEFAULT_MODEL
  }

  private companion object {
    private val MODEL =
      Score(
        speedScore = 150,
        bestSpeedTime = 42000,
        totalScore = 500,
      )
    private val JSON =
      """
      {
          "speedScore": 150,
          "bestSpeedTime": 42000,
          "totalScore": 500
      }
      """.trimIndent()
    private val DEFAULT_MODEL =
      Score()
    private val DEFAULT_JSON =
      """
      {
          "speedScore": 0,
          "bestSpeedTime": 0,
          "totalScore": 0
      }
      """.trimIndent()
    private const val EMPTY_JSON = "{}"
  }
}
