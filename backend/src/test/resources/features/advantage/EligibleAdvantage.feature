Feature: Eligible advantage (VFP)

  Background:
    Given a society "SocietyForTheRich" that propose an advantage "VFPOnly" vfp-only that cost 1 points
    And a society "SocietyForEveryone" that propose an advantage "NotVFPOnly" not vfp-only that cost 1 points
    And a consumer with email "rich@gmail.com" vfp with 20 points on his card
    And a consumer with email "poor@gmail.com" not vfp with 1 points on his card

  Scenario: A consumer can see a vfp-only advantage
    When the consumer with email "rich@gmail.com" wants to see the advantages
    Then the consumer should see the advantage "VFPOnly"
    Then the consumer should see the advantage "NotVFPOnly"

  Scenario: A consumer can not see a vfp-only advantage
    When the consumer with email "poor@gmail.com" wants to see the advantages
    Then the consumer should not see the advantage "VFPOnly"
    Then the consumer should see the advantage "NotVFPOnly"

  Scenario: A consumer can see all because he has enough points
    When the advantage "VFPOnly" is updated to cost 20 points
    When the consumer with email "rich@gmail.com" wants to see the advantages
    Then the consumer should see the advantage "VFPOnly"
    Then the consumer should see the advantage "NotVFPOnly"

  Scenario: A consumer can not see all because he has not enough points
    When the advantage "VFPOnly" is updated to cost 20 points
    When the consumer with email "poor@gmail.com" wants to see the advantages
    Then the consumer should not see the advantage "VFPOnly"
    Then the consumer should see the advantage "NotVFPOnly"