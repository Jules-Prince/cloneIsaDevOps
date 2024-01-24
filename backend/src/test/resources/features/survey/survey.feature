Feature: all about survey

  Background:
    Given a partner
    And a client logged in as "paul"
    And a client logged in as "pauline"

      #TODO: fix this test, pb delete
    #Scenario: The partner create the survey and send it to the clients
      When the partner create a survey called "satisfaction on the service" with description "please answer this survey" and 2 questions
      And he send it to the clients
      Then the clients can see the survey


      #TODO: fix this test, pb delete
    # Scenario: The client answer the survey and the partner analyse it
      When the partner create a survey called "satisfaction on the service" with description "please answer this survey" and 2 questions
      And the client "paul" answer the survey "satisfaction on the service" with "UNSASTIFED" for the question 1 and "SASTIFIED" for the question 2
      Then the partner can analyse the survey and see that the satisfaction for the survey is 3


