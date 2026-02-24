package states;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import states.stopwatch.AbstractStopwatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StopwatchStepDefs {

    private Context context;

    @Before
    public void setUp() {
        AbstractStopwatch.resetInitialValues();
    }

    @Given("a new context in stopwatch mode")
    public void aNewContextInStopwatchMode() {
        AbstractStopwatch.resetInitialValues();
        context = new Context();
        context.currentState = AbstractStopwatch.Instance();
    }

    @Then("the mode is {string}")
    public void theModeIs(String expected) {
        assertEquals(expected, context.getModeText());
    }

    @Then("the total time is {int}")
    public void theTotalTimeIs(int expected) {
        assertEquals(expected, AbstractStopwatch.getTotalTime());
    }

    @Then("the lap time is {int}")
    public void theLapTimeIs(int expected) {
        assertEquals(expected, AbstractStopwatch.getLapTime());
    }

    @When("I start the stopwatch")
    public void iStartTheStopwatch() {
        // up() from ResetStopwatch → RunningStopwatch
        context.up();
    }

    @When("I tick once")
    public void iTickOnce() {
        context.tick();
    }

    @When("I press reset")
    public void iPressReset() {
        // right() from RunningStopwatch → ResetStopwatch
        context.right();
    }
}
