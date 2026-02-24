package states;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import states.stopwatch.AbstractStopwatch;
import states.timer.AbstractTimer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StopwatchStepDefs {

    @Before
    public void setUp() {
        AbstractStopwatch.resetInitialValues();
        AbstractTimer.resetInitialValues();
    }

    @Given("a new context in stopwatch mode")
    public void aNewContextInStopwatchMode() {
        AbstractStopwatch.resetInitialValues();
        Context context = new Context();
        context.currentState = AbstractStopwatch.Instance();
        SharedContext.context = context;
    }

    @Then("the mode is {string}")
    public void theModeIs(String expected) {
        assertEquals(expected, SharedContext.context.getModeText());
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
        // up() : ResetStopwatch → RunningStopwatch
        SharedContext.context.up();
    }

    @When("I tick once")
    public void iTickOnce() {
        SharedContext.context.tick();
    }

    @When("I tick {int} times")
    public void iTickNTimes(int n) {
        for (int i = 0; i < n; i++) SharedContext.context.tick();
    }

    @When("I press reset")
    public void iPressReset() {
        // right() : RunningStopwatch → ResetStopwatch
        SharedContext.context.right();
    }

    @When("I press split")
    public void iPressSplit() {
        // up() : RunningStopwatch → LaptimeStopwatch
        SharedContext.context.up();
    }

    @When("I press unsplit")
    public void iPressUnsplit() {
        // up() : LaptimeStopwatch → RunningStopwatch
        SharedContext.context.up();
    }

    @When("I switch mode")
    public void iSwitchMode() {
        // left() : exits current composite state and enters history state of the other mode
        SharedContext.context.left();
    }
}
