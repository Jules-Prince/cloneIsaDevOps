Feature: select an advantage

  Background:
    Given  a society with an advantage at 3 points, a consumer with a card at 10 points


  Scenario: select an advantage
    When the consumer select a  advantage
    Then the selected advantage is present in the repository
    Then the selected advantage have state available
    Then the card of consumer have 7 points


  Scenario: select an advantage failure
    When  a advantage at 15 points
    Then the consumer select the advantage with failure