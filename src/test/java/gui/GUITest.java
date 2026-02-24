package gui;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Comprehensive GUI Integration Tests")
class GUITest extends TestGUIAbstract {

    @Test
    @DisplayName("Test Interaction via Buttons (Timer)")
    void testTimerInteraction() {
        // Initial state: IdleTimer
        g.updateUI(c);
        assertEquals("run", g.b2.getText());
        assertEquals("set", g.b3.getText());

        // Click 'set' (b3) -> SetTimer
        // We simulate the button click, which should trigger the action listener
        // The action listener updates the context state.
        // Then we must manually call updateUI to reflect the state in the GUI (model-view separation)
        
        // Note: HeadlessGUI adds action listeners that call c.left(), c.up(), c.right()
        // But verifying doClick() works requires that the listener is properly attached.
        
        // However, Swing components might not trigger listeners synchronously in headless or without a full event queue setup
        // But usually doClick() works for basic action listeners.
        
        // Let's try simulating the button click
        // To be safe and since we are testing the logic connected to the GUI:
        // Start: IdleTimer
        
        // Verify wiring:
        // b3 -> set -> SetTimer
        g.b3.doClick(); // Should call c.right()
        g.updateUI(c); 
        
        assertEquals("SetTimer", g.myText3.getText());
        assertEquals("inc 5", g.b2.getText());

        // b2 -> inc 5
        g.b2.doClick(); // Should call c.up()
        g.updateUI(c);
        assertEquals("memTimer = 5", g.myText1.getText());
        
        // b1 -> reset (in SetTimer)
        g.b1.doClick(); // Should call c.left()
        g.updateUI(c);
        assertEquals("memTimer = 0", g.myText1.getText());
        
        // b3 -> done -> IdleTimer
        g.b3.doClick(); // Should call c.right()
        g.updateUI(c);
        assertEquals("IdleTimer", g.myText3.getText());
    }

    @Test
    @DisplayName("Test Stopwatch Interaction and Laptime Labels")
    void testStopwatchInteraction() {
        // Switch to Stopwatch: b1 (change mode)
        g.b1.doClick();
        g.updateUI(c);
        assertEquals("ResetStopwatch", g.myText3.getText());
        assertEquals("run", g.b2.getText());

        // Run: b2
        g.b2.doClick();
        g.updateUI(c);
        assertEquals("RunningStopwatch", g.myText3.getText());
        assertEquals("split", g.b2.getText());
        assertEquals("reset", g.b3.getText());

        // Split: b2 (Laptime)
        g.b2.doClick();
        g.updateUI(c);
        assertEquals("LaptimeStopwatch", g.myText3.getText());
        assertEquals("unsplit", g.b2.getText());
        assertEquals("(unused)", g.b3.getText()); // Right button is unused in Laptime
        
        // Check Laptime state labels specifically (was missing)
        assertEquals("stopwatch", g.myText2.getText());
        // In LaptimeStopwatch, display string is "lapTime = ..."
        assertEquals("lapTime = 0", g.myText1.getText()); 

        // Unsplit: b2
        g.b2.doClick();
        g.updateUI(c);
        assertEquals("RunningStopwatch", g.myText3.getText());
        
        // Reset: b3
        g.b3.doClick();
        g.updateUI(c);
        assertEquals("ResetStopwatch", g.myText3.getText());
    }

    @Test
    @DisplayName("Test Timer Running and Ringing Scenario")
    void testTimerRunningScenario() {
        // Initial state: IdleTimer
        g.updateUI(c);
        
        // Go to SetTimer
        g.b3.doClick(); 
        g.updateUI(c);
        
        // Increment by 5
        g.b2.doClick();
        g.updateUI(c);
        assertEquals("memTimer = 5", g.myText1.getText());
        
        // Done (Idle)
        g.b3.doClick();
        g.updateUI(c);
        assertEquals("IdleTimer", g.myText3.getText());
        assertEquals("run", g.b2.getText());
        
        // Run (RunningTimer)
        g.b2.doClick();
        g.updateUI(c);
        assertEquals("RunningTimer", g.myText3.getText());
        assertEquals("stop", g.b3.getText()); // Running uses "stop"
        
        // Pause (PausedTimer)
        g.b2.doClick();
        g.updateUI(c);
        assertEquals("PausedTimer", g.myText3.getText());
        assertEquals("run", g.b2.getText());
        assertEquals("reset", g.b3.getText()); // Paused uses "reset" (inherited from ActiveTimer)
        
        // Resume (Running)
        g.b2.doClick();
        g.updateUI(c);
        assertEquals("RunningTimer", g.myText3.getText());
        
        // Tick down to 0
        // timer=5. layout: 5->4->3->2->1->Ringing(0)?
        // RunningTimer.doIt(): timer--; if timer<=0 ...
        // 5 -> 4
        c.tick(); 
        g.updateUI(c);
        assertEquals("timer = 4", g.myText1.getText());
        
        c.tick(); // 3
        c.tick(); // 2
        c.tick(); // 1
        c.tick(); // 0 -> Ringing
        
        g.updateUI(c);
        assertEquals("RingingTimer", g.myText3.getText());
        assertEquals("reset", g.b3.getText());
        
        // Reset (Idle)
        g.b3.doClick();
        g.updateUI(c);
        assertEquals("IdleTimer", g.myText3.getText());
    }
}
