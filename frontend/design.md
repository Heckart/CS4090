# **Order \- Definition**

An order is placed by a user and contains:

- A list of items.  
- The quantity of each item.  
- The price of each item.  
- The total price of the order.  
- The location where the order is placed through.  
- The location where the order is to be delivered.  
- The time when the order is to be fulfilled.  
- The satisfaction rating from the user and shopper.

An order can only be placed at one location. A user can place multiple orders at multiple locations simultaneously.

# **Use Case \- Place an Order**

## **Basic Flow: Create Order**

1. User selects the Create `Order` button.  
2. User is directed to the `Create Order` page.  
3. The user chooses the location where they want their order delivered.  
4. The user selects what business they want their order to be completed through.  
5. User selects items to be added to their order.  
6. Users can select the quantity of each item.  
7. Users can see the running total of their order as they select items.  
8. Once the user is done selecting items they select the `Place Order` button.  
9. The `Place Order` button directs the user to a page where they can see a summary of their order including all items, their quantities, and the individual price of each item and the total cost of the order.  
10. After reviewing their order, the user selects the `Schedule Order` button.  
11. The `Schedule Order` button directs the user to a list of available time slots where "shoppers" can take their order.  
12. The user selects a time slot and receives a confirmation that their order has been placed.  
13. The user is able to view the status of their order elsewhere in the app.  
14. Once the order is completed, the user and the shopper is given the option to rate their satisfaction with the order.

## **Alternate Flows**

#### **Making Changes before placing an order**

Have the ability to modify the order before confirming.


## **Design Requirements**

### **Home Page**

Homepage of the `FarmRoutes` application. A central location for all pages. Display user and information for active and previous orders.

### **Create Order**

The first stage of placing an order. The user selects the location where they want their order delivered. Then they can choose from a list of local businesses to place their order to.

Then the user is directed to a page where the user can choose from a selection of available items for their order. Once they have chosen their order they must review it before going further.

### **Review Order**

When the user reviews their order they are given the opportunity to go over the items in their order and the total price. After they have reviewed their order they will be prompted to schedule when their order is to be placed.

### **Schedule Order**

When the user schedules their order they are given a list of times where `shoppers` will be available to fulfill their order. These times may be days into the future. The `shopper` can set a maximum for how many items they wish to pick up, so if the user's order is too large then they will not be shown the `shopper`'s availability. Once the user selects a time slot they will be directed to the final screen to confirm their order.

### **Confirm Order**

The order confirmation shows the total price, the delivery location, and the time when the order will be fulfilled. This is where the user will be prompted for payment information. After confirming the order it will be saved formally and the user will be given the option to track it.