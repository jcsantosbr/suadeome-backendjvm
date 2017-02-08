package runner

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber::class)

@CucumberOptions(
        format = arrayOf("pretty", "html:target/cucumber"),
        features = arrayOf("src/test/kotlin/integration/com/jcs/suadeome/features"),
        glue = arrayOf("integration.com.jcs.suadeome.steps")
)
class CucumberRunnerTest {
}


