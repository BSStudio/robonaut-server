package hu.bsstudio.robonaut.safetycar.model

import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class SafetyCarOvertakeInformationTest(
  @param:Autowired private val underTest: JacksonTester<SafetyCarOvertakeInformation>,
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
      SafetyCarOvertakeInformation(
        teamId = 77L,
        numberOfOvertakes = 3,
      )
    private val JSON =
      """
      {
          "teamId": 77,
          "numberOfOvertakes": 3
      }
      """.trimIndent()
  }
}
