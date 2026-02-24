Feature: Stopwatch behaviour
  As a user
  I want to use the stopwatch
  So that I can measure elapsed time

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

  Scenario: Stopwatch resets after pressing reset
    Given a new context in stopwatch mode
    When I start the stopwatch
    And I tick once
    And I press reset
    Then the total time is 0
    And the lap time is 0
