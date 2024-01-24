Feature: update advantage informations

  Background:
    Given a society called "Café du coin"
    And the society "Café du coin" propose an advantage "Café offert" with initial price 2€, price 0€, point 2, that is not a vfp advantage, start validity 25-02-2023 at 8h00 and end validity 27-02-2023 at 9h00


  Scenario: update society's informations
    When the society update the advantage "Café presque offert" with inital price 3€, with price 1€, point 1, that is a vfp advantage, start validity 26-02-2023 at 10h00 and end validity 28-02-2023 at 13h00
    Then the advantage "Café presque offert" should have inital price 3€, price 1€, point 1, that is a vfp advantage, start validity 26-02-2023 at 10h00 and end validity 28-02-2023 at 13h00