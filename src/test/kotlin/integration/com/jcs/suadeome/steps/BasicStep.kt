package integration.com.jcs.suadeome.steps

import cucumber.api.DataTable
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

class BasicStep {

    @Given("^the following users already registered$")
    fun the_following_users_already_registered(arg1: DataTable) {
    }

    @Given("^the following professionals already registered$")
    fun the_following_professionals_already_registered(arg1: DataTable) {
    }

    @Given("^the current user is \"([^\"]*)\"$")
    fun the_current_user_is(arg1: String) {

    }

    @When("^the user requests \"([^\"]*)\"$")
    fun the_user_requests(arg1: String) {

    }

    @Then("^the response should be:$")
    fun the_response_should_be(arg1: String) {

    }

    @Given("^the following services already registered$")
    fun the_following_services_already_registered(arg1: DataTable) {

    }

    @When("^the user posts the following command \"([^\"]*)\"$")
    fun the_user_posts_the_following_command(arg1: String, arg2: String) {

    }

}
