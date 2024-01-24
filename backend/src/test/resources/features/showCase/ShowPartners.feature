Feature: show partners

  Background:

  Scenario: show the list of parnter not empty
    Given a partner named "Mairie"
    And a partner named "Coiffeur"
    When I go to the partner list
    Then I see "Mairie"
    And I see "Coiffeur"
    Then there is 2 partner

  Scenario: show the list of society empty
    When I go to the partner list
    Then there is 0 partner