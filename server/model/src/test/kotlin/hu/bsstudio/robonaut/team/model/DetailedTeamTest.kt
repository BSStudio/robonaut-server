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
class DetailedTeamTest(
  @param:Autowired private val underTest: JacksonTester<DetailedTeam>,
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
      DetailedTeam(
        teamId = 10L,
        year = 2025,
        teamName = "Speed Demons",
        teamMembers = listOf("John", "Jane"),
        teamType = TeamType.JUNIOR,
        skillScore = 75,
        numberOfOvertakes = 2,
        safetyCarWasFollowed = true,
        speedTimes = listOf(48000, 49000, 47500),
        votes = 2500,
        audienceScore = 80,
        qualificationScore = 150,
        combinedScore =
          Score(
            speedScore = 200,
            bestSpeedTime = 47500,
            totalScore = 600,
          ),
        juniorScore =
          Score(
            speedScore = 180,
            bestSpeedTime = 47500,
            totalScore = 550,
          ),
      )
    private val JSON =
      """
      {
          "teamId": 10,
          "year": 2025,
          "teamName": "Speed Demons",
          "teamMembers": ["John", "Jane"],
          "teamType": "JUNIOR",
          "skillScore": 75,
          "numberOfOvertakes": 2,
          "safetyCarWasFollowed": true,
          "speedTimes": [48000, 49000, 47500],
          "votes": 2500,
          "audienceScore": 80,
          "qualificationScore": 150,
          "combinedScore": {
              "speedScore": 200,
              "bestSpeedTime": 47500,
              "totalScore": 600
          },
          "juniorScore": {
              "speedScore": 180,
              "bestSpeedTime": 47500,
              "totalScore": 550
          }
      }
      """.trimIndent()
    private val DEFAULT_MODEL = DetailedTeam()
    private val DEFAULT_JSON =
      """
      {
          "teamId": 0,
          "year": ${Calendar.getInstance().get(Calendar.YEAR)},
          "teamName": "",
          "teamMembers": [],
          "teamType": "JUNIOR",
          "skillScore": 0,
          "numberOfOvertakes": 0,
          "safetyCarWasFollowed": false,
          "speedTimes": [],
          "votes": 0,
          "audienceScore": 0,
          "qualificationScore": 0,
          "combinedScore": {
              "speedScore": 0,
              "bestSpeedTime": 0,
              "totalScore": 0
          },
          "juniorScore": {
              "speedScore": 0,
              "bestSpeedTime": 0,
              "totalScore": 0
          }
      }
      """.trimIndent()
    private const val EMPTY_JSON = "{}"
  }
}
