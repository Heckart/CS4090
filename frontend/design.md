# FarmRoutes - Frontend

## CS 4090 - Capstone I - Project

### Order

An order is placed by a user and contains:

- A list of items.
- The quantity of each item.
- The price of each items.
- The total price of the order.
- The location where the order is placed through.
- The location where the order is to be delivered.
- The time when the order is to be fulfilled.
- The satisfaction rating from the user and shopper.

An order can only be placed at one location. A user can place multiple orders at multiple locations simultaneously.

### Use Case - Place an Order

#### Fundamental Steps of the Basic Flow:

1. Select an address to deliver to.
2. Choose a business to order from.
3. Add items to the user's cart.
4. Review the current order's data.
5. Schedule a time for the order to be placed.
6. Checkout and pay for the order.
7. Review the order once completed.

#### Basic Flow: Create Order

1. User selects `Place Order` button.
2. The user is directed to a form to enter where they want their order delivered to.
3. The user is then directed to select which business they want to place their order through.
4. The user then selects from a catalog of items to add to their `cart`, or list of items related to their order. The user can see the item and its price.
5. The user is given the ability to select how many of each item they want to add to their order.
6. The user is shown a running total of the cost of their order.
7. When the user is done selecting items they the can review their order. They are shown information about the delivery location, the business the order is fulfilled through, the items that comprise their order, and the running total.
    - The user while reviewing their order can choose to go back and edit their order.
8. After reviewing and confirming their order, the user can select from a schedule of available jobs to add their order to.
9. The user is then prompted for payment information to checkout.
10. The user is then able to view the status of their order elsewhere in the app.
11. The shopper is notified about the addition to their run.
14. Once the order is completed, the user and the shopper is given the option to rate their satisfaction with the order.

