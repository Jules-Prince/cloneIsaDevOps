Feature: Get And Reload Balance

  Background:
    Given a consumer logged in with email "hilona@gmail.com" and password "hilona" and credit card "1234567890123456" and balance 0

  Scenario: get balance
    When the consumer wants to see his balance
    Then the consumer should see his balance at 0

  Scenario: reload balance
    When the consumer wants to reload his balance with 10
    Then the consumer should see his balance at 10

  Scenario: reload balance with nothing
    When the consumer wants to reload his balance with 0
    Then the consumer should see his balance at 0

  Scenario: reload balance with more than limit
    When the consumer wants to reload his balance with 110
    Then the consumer should see his balance at 0

