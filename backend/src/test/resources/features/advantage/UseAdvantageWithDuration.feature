Feature: use an advantage with a duration

  Background:
    Given a society "EnviBus"
    And an advantage "Un tickt de métro gratuit par jour" from the society "EnviBus"
    And a customer "John Doe" who has selected the advantage "Un tickt de métro gratuit par jour" from the society "EnviBus"

  Scenario: the customer use the advantage for the first time
    Given the customer never used his advantage
    When the customer use his advantage 1 time
    Then the customer has used his advantage 1 time

  Scenario: the customer use the advantage for the second time of the day
    Given the customer never used his advantage
    When the customer use his advantage 1 time
    Then the customer has used his advantage 1 time
    Then the customer cannot use his advantage anymore today

  Scenario: the customer use the advantage but the advantage is not available anymore
    Given the advantage is expired
    Then the customer cannot use his advantage

  Scenario: the advantage is now illimited
    Given the customer never used his advantage
    Given the advantage is now illimited
    When the customer use his advantage 15 time
    Then the customer has used his advantage 15 time