Feature: consumer get his number of point in this card

  Background:
    Given  a new consumer with a card ,an another consumer without card


  Scenario: consumer get his number of point in this card
    Then the number of point is 0

  Scenario: consumer get his number of point in this card
    When the consumer make a purchase at 10 price
    Then the number of point is 5

  Scenario:  a consumer without card get his number of point in this card failure
    Then have failure