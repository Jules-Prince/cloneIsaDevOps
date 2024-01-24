Feature: consumer retry a card

  Background:
    Given  a new consumer


  Scenario: create a card
    When the consumer retry a  card
    Then the card is present in the repository

  Scenario: create a card failure
    When the consumer retry a  card
    Then the consumer retry new a  card have failure