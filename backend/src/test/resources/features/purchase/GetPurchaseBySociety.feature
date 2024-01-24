Feature: get all purchase for a society
  Background:
    Given 2 consumers, his cards and a society

  Scenario: the consumers make a purchase and the society get her purchases
    When the consumers make a purchase in society
    Then there is 2 purchase for the society
    Then this is this purchase  for the society