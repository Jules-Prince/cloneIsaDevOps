Feature: showSocieties

  Background:
    Given a client logged in as "jean@hotmail.fr" with password "mdp"

  Scenario: show the list of society not empty
    Given a partner and his society named "Boulangerie"
    When the client go to the society list
    Then the client see "Boulangerie"
    Then there is 1 society

  Scenario: show the list of society empty
    When the client go to the society list
    Then there is 0 society

  Scenario: show the list of fav societies
    Given a partner and his society named "Boulangerie"
    When the client add "Boulangerie" to his fav list
    When the client go to the fav society list
    Then the client see "Boulangerie" in the fav list
    Then there is 1 fav society

  Scenario: show the list of fav societies empty
    When the client go to the fav society list
    Then there is 0 fav society