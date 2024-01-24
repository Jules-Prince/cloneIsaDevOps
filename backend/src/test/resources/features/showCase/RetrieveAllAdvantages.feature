Feature: retrieve all advantages

  Background:
    Given a consumer
    And a advantage repository with 15 advantages

  Scenario: the customer retrieve all advantages
    When the customer retrieve all advantages and the customer has 15 advantages

  Scenario: show the list of advantage by society not empty
    Given an advantage "café offert" by his society named "Boulangerie"
    When the client go to the society list and filter by society "Boulangerie"
    Then the client see only "café offert"
    Then there is only 1 advantage

  Scenario: show the list of advantage by society empty
    Given a society named "Boulangerie" giving 0 advantage
    When the client go to the society list and filter by society "Boulangerie"
    Then there is only 0 advantage