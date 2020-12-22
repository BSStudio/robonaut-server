package hu.bsstudio.stepdefinitions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

public class Smoke {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private HttpStatus statusCode;

    @When("user calls actuator endpoint")
    public void userCallsActuatorEndpoint() {
        final var url = "http://localhost:" + port + "/actuator/health";
        final var responseEntity = testRestTemplate.getForEntity(url, String.class);
        statusCode = responseEntity.getStatusCode();
    }

    @Then("response status is ok")
    public void responseStatusIsOk() {
        assertThat(statusCode, is(equalTo(HttpStatus.OK)));
    }

}
