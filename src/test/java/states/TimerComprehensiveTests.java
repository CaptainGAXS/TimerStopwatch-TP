package states;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.timer.*;

@DisplayName("Comprehensive Timer Tests")
class TimerComprehensiveTests {

    private Context context;

    @BeforeEach
    void setup() {
        context = new Context();
        AbstractTimer.resetInitialValues();
        // Start in timer mode (default)
    }

    @Test
    @DisplayName("IdleTimer: Run with 0 memTimer does nothing")
    void testIdleRunZero() {
        assertEquals(0, AbstractTimer.getMemTimer());
        assertSame(IdleTimer.Instance(), context.currentState);
        
        context.up(); // Attempt to run
        
        assertSame(IdleTimer.Instance(), context.currentState);
    }

    @Test
    @DisplayName("SetTimer: Increment and Reset")
    void testSetTimerLogic() {
        context.right(); // Go to SetTimer
        assertSame(SetTimer.Instance(), context.currentState);
        
        // Test Increment (up)
        context.up(); // +5
        assertEquals(5, AbstractTimer.getMemTimer());
        
        context.up(); // +5 -> 10
        assertEquals(10, AbstractTimer.getMemTimer());
        
        // Test manual increment via tick (doIt)
        context.tick(); // +1 -> 11
        assertEquals(11, AbstractTimer.getMemTimer());
        
        // Test Reset (left) - specific to SetTimer
        context.left(); // Reset to 0
        assertEquals(0, AbstractTimer.getMemTimer());
        assertSame(SetTimer.Instance(), context.currentState); // Should stay in SetTimer
    }

    @Test
    @DisplayName("RunningTimer: Stop")
    void testRunningStop() {
        // Setup timer to 10
        context.right(); // SetTimer
        context.up(); // 5
        context.up(); // 10
        context.right(); // IdleTimer
        
        // Run
        context.up();
        assertSame(RunningTimer.Instance(), context.currentState);
        
        // Stop (right)
        context.right();
        assertSame(IdleTimer.Instance(), context.currentState);
        // Timer should not reset, but we are back in Idle
    }

    @Test
    @DisplayName("RingingTimer: Stop")
    void testRingingStop() {
         // Setup timer to 1
        context.right(); // SetTimer
        context.tick(); // 1 via tick
        context.right(); // IdleTimer
        
        // Run
        context.up();
        assertSame(RunningTimer.Instance(), context.currentState);
        
        // Tick -> 0 -> Ringing
        context.tick();
        assertSame(RingingTimer.Instance(), context.currentState);
        assertTrue(AbstractTimer.isRinging());
        
        // Stop (right)
        context.right();
        assertSame(IdleTimer.Instance(), context.currentState);
        assertFalse(AbstractTimer.isRinging());
    }

    @Test
    @DisplayName("RingingTimer: Up button unused")
    void testRingingUp() {
         // Setup timer to 1
        context.right(); // SetTimer
        context.tick(); // 1 via tick
        context.right(); // IdleTimer
        
        // Run
        context.up();
        assertSame(RunningTimer.Instance(), context.currentState);
        
        // Tick -> 0 -> Ringing
        context.tick();
        assertSame(RingingTimer.Instance(), context.currentState);
        
        // Up (unused)
        context.up();
        assertSame(RingingTimer.Instance(), context.currentState);
        assertEquals("(unused)", context.getUpText());
    }
}
