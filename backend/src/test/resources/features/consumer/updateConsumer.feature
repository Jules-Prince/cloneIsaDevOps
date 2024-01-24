Feature: updateConsumer

  Background:
    Given a consumer with email "cathy@gmail.com" and password "123456" and credit card number "1234567891234567" and licence plate "CA-123-TY"

  Scenario: the consumer update his informations
    When the consumer update his email with "eugene@savoie.com" and password with "98765" and credit card number with "1000000000000000" and licence plate with "EU-123-GN"
    Then the consumer informations are : email "eugene@savoie.com" and password "98765" and credit card number "1000000000000000" and licence plate "EU-123-GN"