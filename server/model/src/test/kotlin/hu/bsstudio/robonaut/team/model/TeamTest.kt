package hu.bsstudio.robonaut.team.model

import hu.bsstudio.robonaut.common.model.TeamType
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.util.Calendar

@JsonTest
class TeamTest(
  @param:Autowired private val underTest: JacksonTester<Team>,
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
      Team(
        teamId = 1L,
        year = 2025,
        teamName = "Robonauts",
        teamMembers = listOf("Alice", "Bob", "Charlie"),
        teamType = TeamType.SENIOR,
      )
    private val JSON =
      """
      {
          "teamId": 1,
          "year": 2025,
          "teamName": "Robonauts",
          "teamMembers": ["Alice", "Bob", "Charlie"],
          "teamType": "SENIOR"
      }
      """.trimIndent()
    private val DEFAULT_MODEL = Team()
    private val DEFAULT_JSON =
      """
      {
          "teamId": 0,
          "year": ${Calendar.getInstance().get(Calendar.YEAR)},
          "teamName": "",
          "teamMembers": [],
          "teamType": "JUNIOR"
      }
      """.trimIndent()
    private const val EMPTY_JSON = "{}"
  }
}
