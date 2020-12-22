package hu.bsstudio;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:", plugin = {"pretty", "html:build/reports/cucumber/index.html"})
public class RunCucumberTest {
}
