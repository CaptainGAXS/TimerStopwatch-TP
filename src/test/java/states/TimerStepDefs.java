package states;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import states.timer.AbstractTimer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimerStepDefs {

    @Given("a new context in timer mode")
    public void aNewContextInTimerMode() {
        AbstractTimer.resetInitialValues();
        // Context() démarre en mode timer (IdleTimer) par défaut
        SharedContext.context = new Context();
    }

    @Then("the timer is {int}")
    public void theTimerIs(int expected) {
        assertEquals(expected, AbstractTimer.getTimer());
    }

    @Then("the mem timer is {int}")
    public void theMemTimerIs(int expected) {
        assertEquals(expected, AbstractTimer.getMemTimer());
    }

    @Then("the timer is ringing")
    public void theTimerIsRinging() {
        assertTrue(AbstractTimer.isRinging(), "Le timer devrait être en train de sonner");
    }

    @When("I press set")
    public void iPressSet() {
        // right() : IdleTimer → SetTimer
        SharedContext.context.right();
    }

    @When("I press up {int} times")
    public void iPressUpNTimes(int n) {
        // up() en SetTimer : incrémente memTimer de 5 à chaque appui
        for (int i = 0; i < n; i++) SharedContext.context.up();
    }

    @When("I press left")
    public void iPressLeft() {
        // left() en SetTimer : remet memTimer à 0 (comportement "inner first")
        SharedContext.context.left();
    }

    @When("I press done")
    public void iPressDone() {
        // right() : SetTimer → IdleTimer
        SharedContext.context.right();
    }

    @When("I press start")
    public void iPressStart() {
        // up() : IdleTimer → RunningTimer (seulement si memTimer > 0)
        SharedContext.context.up();
    }

    @When("I press pause")
    public void iPressTimerPause() {
        // up() : RunningTimer → PausedTimer
        SharedContext.context.up();
    }

    @When("I press resume")
    public void iPressResume() {
        // up() : PausedTimer → RunningTimer
        SharedContext.context.up();
    }

    @When("I press stop")
    public void iPressStop() {
        // right() : ActiveTimer → IdleTimer
        SharedContext.context.right();
    }
}
