Feature: Smoke test

  Scenario: Validate app start
    When user calls actuator endpoint
    Then response status is ok
