Feature: add and delete a favorite society
  Background:
    Given a consumer and a society

  Scenario: the consumer add a favorite society
    When the consumer add the society to his favorite society
    Then the consumer has 1 favorite society
    Then the consumer favorite society is the society given

  Scenario:
    Given the consumer add the society to his favorite society
    When the consumer delete the society from his favorite society
    Then the consumer has 0 favorite society