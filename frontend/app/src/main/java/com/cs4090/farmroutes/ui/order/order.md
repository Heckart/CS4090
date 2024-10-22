# Order

## Placing an Order

### Steps

1. Select an address to deliver to.
2. Choose a business to order from.
3. Add items to the user's cart.
4. Review the current order's data.
5. Schedule a time for the order to be placed.
6. Checkout and pay for the order.

## TODO

### For each step/screen of the order creation process:

- Create a form which takes in input from the user.
- Handle the values of the form in each screen's view model.
- When advancing to the next step, save the data from the form to the `OrderRepository`
- Handle any logic for going backwards and choosing a different option in a previous screen.
  - i.e. When a user selects a different business to order from.
    - Warn the user their current items will be deleted, and then clear their items.
- Construct unit tests for each screen.