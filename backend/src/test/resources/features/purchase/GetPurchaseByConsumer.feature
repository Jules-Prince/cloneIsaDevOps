Feature: get all purchase for a consumer
  Background:
    Given a consumer, his card and a society

  Scenario: the consumer make a purchase and get his purchase
    When make a purchase in society
    Then there is one purchase
    Then this is this purchase
