Feature: Stopwatch and Timer behaviour
  As a user
  I want to use the stopwatch and the timer
  So that I can measure and count down time

  # ===================== STOPWATCH =====================

  Scenario: Initial state of the stopwatch
    Given a new context in stopwatch mode
    Then the mode is "stopwatch"
    And the total time is 0
    And the lap time is 0

  Scenario: Stopwatch increments after one tick
    Given a new context in stopwatch mode
    When I start the stopwatch
    And I tick once
    Then the total time is 1

  Scenario: Stopwatch increments correctly after multiple ticks
    Given a new context in stopwatch mode
    When I start the stopwatch
    And I tick 5 times
    Then the total time is 5

  Scenario: Stopwatch resets after pressing reset
    Given a new context in stopwatch mode
    When I start the stopwatch
    And I tick once
    And I press reset
    Then the total time is 0
    And the lap time is 0

  Scenario: Stopwatch records lap time on split
    Given a new context in stopwatch mode
    When I start the stopwatch
    And I tick 3 times
    And I press split
    Then the lap time is 3
    And the total time is 3

  Scenario: Total time continues incrementing during lap display
    Given a new context in stopwatch mode
    When I start the stopwatch
    And I tick 3 times
    And I press split
    And I tick once
    Then the total time is 4
    And the lap time is 3

  Scenario: Lap display auto-returns to running after 5 ticks
    Given a new context in stopwatch mode
    When I start the stopwatch
    And I tick 3 times
    And I press split
    And I tick 5 times
    Then the total time is 8
    And the lap time is 3

  Scenario: Unsplit returns to running stopwatch
    Given a new context in stopwatch mode
    When I start the stopwatch
    And I tick 3 times
    And I press split
    And I press unsplit
    Then the mode is "stopwatch"
    And the total time is 3

  Scenario: Switch from stopwatch to timer mode
    Given a new context in stopwatch mode
    When I switch mode
    Then the mode is "timer"

  Scenario: History state preserved when switching back to stopwatch
    Given a new context in stopwatch mode
    When I start the stopwatch
    And I tick 3 times
    And I switch mode
    Then the mode is "timer"
    When I switch mode
    Then the mode is "stopwatch"
    And the total time is 3

  # ===================== TIMER =====================

  Scenario: Initial state of the timer
    Given a new context in timer mode
    Then the mode is "timer"
    And the mem timer is 0
    And the timer is 0

  Scenario: Enter set mode via right button
    Given a new context in timer mode
    When I press set
    Then the mode is "timer"
    And the mem timer is 0

  Scenario: Set timer increments memory by 5 each press
    Given a new context in timer mode
    When I press set
    And I press up 3 times
    Then the mem timer is 15

  Scenario: Reset memory in set mode via left button
    Given a new context in timer mode
    When I press set
    And I press up 3 times
    And I press left
    Then the mem timer is 0

  Scenario: Done button exits set mode and returns to idle
    Given a new context in timer mode
    When I press set
    And I press up 2 times
    And I press done
    Then the mode is "timer"
    And the mem timer is 10

  Scenario: Timer does not start when mem timer is zero
    Given a new context in timer mode
    When I press start
    Then the mode is "timer"
    And the timer is 0

  Scenario: Timer starts running when mem timer is positive
    Given a new context in timer mode
    When I press set
    And I press up 1 times
    And I press done
    And I press start
    Then the mode is "timer"
    And the timer is 5

  Scenario: Running timer decrements on each tick
    Given a new context in timer mode
    When I press set
    And I press up 1 times
    And I press done
    And I press start
    And I tick once
    Then the timer is 4

  Scenario: Running timer decrements correctly after multiple ticks
    Given a new context in timer mode
    When I press set
    And I press up 1 times
    And I press done
    And I press start
    And I tick 3 times
    Then the timer is 2

  Scenario: Running timer can be paused
    Given a new context in timer mode
    When I press set
    And I press up 1 times
    And I press done
    And I press start
    And I tick once
    And I press pause
    Then the mode is "timer"
    And the timer is 4

  Scenario: Paused timer does not decrement on tick
    Given a new context in timer mode
    When I press set
    And I press up 1 times
    And I press done
    And I press start
    And I tick once
    And I press pause
    And I tick once
    Then the timer is 4

  Scenario: Paused timer resumes and decrements again
    Given a new context in timer mode
    When I press set
    And I press up 1 times
    And I press done
    And I press start
    And I tick once
    And I press pause
    And I press resume
    And I tick once
    Then the timer is 3

  Scenario: Running timer rings when it reaches zero
    Given a new context in timer mode
    When I press set
    And I press up 1 times
    And I press done
    And I press start
    And I tick 5 times
    Then the timer is ringing

  Scenario: Stopping a running timer returns to idle with mem timer preserved
    Given a new context in timer mode
    When I press set
    And I press up 1 times
    And I press done
    And I press start
    And I tick once
    And I press stop
    Then the mode is "timer"
    And the mem timer is 5

  Scenario: Switch from timer to stopwatch mode
    Given a new context in timer mode
    When I switch mode
    Then the mode is "stopwatch"
