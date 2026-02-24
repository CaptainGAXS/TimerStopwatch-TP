package states;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.stopwatch.*;

@DisplayName("Comprehensive Stopwatch Tests")
class StopwatchComprehensiveTests {

    private Context context;

    @BeforeEach
    void setup() {
        context = new Context();
        AbstractStopwatch.resetInitialValues();
        // Force context to be in Stopwatch mode for testing
        context.currentState = ResetStopwatch.Instance();
    }

    @Test
    @DisplayName("ResetStopwatch -> Run -> Reset")
    void testRunReset() {
        // Start from Reset
        assertSame(ResetStopwatch.Instance(), context.currentState);
        
        // Go to Running
        context.up();
        assertSame(RunningStopwatch.Instance(), context.currentState);
        
        // Tick a few times
        context.tick();
        context.tick();
        assertEquals(2, AbstractStopwatch.getTotalTime());
        
        // Reset via right()
        context.right();
        assertSame(ResetStopwatch.Instance(), context.currentState);
        assertEquals(0, AbstractStopwatch.getTotalTime(), "Total time should be reset on entry to ResetStopwatch");
    }

    @Test
    @DisplayName("Laptime Timeout Transition")
    void testLaptimeTimeout() {
        // Start running
        context.up();
        context.tick(); // totalTime = 1
        
        // Go to Laptime
        context.up();
        assertSame(LaptimeStopwatch.Instance(), context.currentState);
        assertEquals(1, AbstractStopwatch.getLapTime());
        
        // Timeout is 5.
        // Tick 1: timeout=4
        context.tick();
        assertSame(LaptimeStopwatch.Instance(), context.currentState);
        
        // Tick 2: timeout=3
        context.tick();
        
        // Tick 3: timeout=2
        context.tick();
        
        // Tick 4: timeout=1
        context.tick();
        assertSame(LaptimeStopwatch.Instance(), context.currentState);
        
        // Tick 5: timeout=0 -> Transition
        context.tick();
        assertSame(RunningStopwatch.Instance(), context.currentState);
        
        // Verifying totalTime continued strictly
        // 1 (before lap) + 5 (during lap) = 6
        assertEquals(6, AbstractStopwatch.getTotalTime());
    }

    @Test
    @DisplayName("Laptime Unsplit via Button")
    void testLaptimeUnsplit() {
        // Start running
        context.up();
        context.tick(); 
        
        // Laptime
        context.up();
        assertSame(LaptimeStopwatch.Instance(), context.currentState);
        
        // Unsplit via up()
        context.up();
        assertSame(RunningStopwatch.Instance(), context.currentState);
    }

    @Test
    @DisplayName("ResetStopwatch: Right button unused")
    void testResetRight() {
        // Start from Reset
        assertSame(ResetStopwatch.Instance(), context.currentState);
        
        // Right button (unused)
        context.right();
        assertSame(ResetStopwatch.Instance(), context.currentState);
        assertEquals("(unused)", context.getRightText());
    }
}
