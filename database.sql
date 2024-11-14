-- Step 1: Insert a user
INSERT INTO Users (User ID, password, firstName, lastName) 
VALUES ('101', 'password', 'John', 'Doe');

-- Step 2: Insert a business
INSERT INTO BusinessInfo (BusinessID, addressprimary, addresssecondary, city, state, zip_code) 
VALUES ('B001', '123 Main St', 'Suite 100', 'Anytown', 'CA', '90210');

-- Step 3: Insert an item associated with the business
INSERT INTO Items (UPC, BusinessID, name, price) 
VALUES ('UPC001', 'B001', 'Widget', 19.99);

-- Step 4: Insert a shopper into JobBoard
INSERT INTO JobBoard (ShopperID, fulfillmenttime) 
VALUES ('101', '2023-10-01 12:00:00');

-- Step 5: Insert an order associated with the user, business, and shopper
INSERT INTO Orders (OrderID, UserID, BusinessID, ShopperID, Status, fulfillmentTime, addressprimary, addresssecondary, city, state, zip_code) 
VALUES ('O001', '101', 'B001', '101', 'Pending', 30, '123 Main St', 'Suite 100', 'Anytown', 'CA', '90210');

-- Step 6: Insert an order item that references the order and the item
INSERT INTO OrderItems (OrderID, UPC, quantity) 
VALUES ('O001', 'UPC001', 2);
