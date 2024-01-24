Feature:purchase with card in society


Background:
    Given  customer with a loyalty card and a balance of 26.32 euros and a point balance of 15 points


Scenario: Purchase for an amount of 19.99 euros
When the customer goes to the cashier to pay an amount of 19.99 euros
Then the customer's balance increases to 6.329999923706055 euros
Then its number of points is 24 points

Scenario: Purchase for an amount of 120.50 euros
Then the customer goes to the cashier to pay an amount of 120.50 euros have failure
Then the customer's balance is still at 26.31999969482422 euros
Then its number of points is 15 points