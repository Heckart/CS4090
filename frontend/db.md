# FarmRoutes - Database Schema

## CS 4090 - Capstone I - Project

### Tables

#### Users

```sql
CREATE TABLE "Users" (
    "UserID" TEXT UNIQUE NOT NULL,
    "PwdHash" TEXT NOT NULL,
    "PrimAddr" TEXT NOT NULL,
    PRIMARY KEY("UserID")
);
```

#### Orders

```sql
CREATE TABLE "Orders" (
    "OrderID" TEXT UNIQUE NOT NULL,
    "UserID" TEXT NOT NULL REFERENCES Users("UserID") ON DELETE RESTRICT,
    "ShopperID" TEXT NOT NULL REFERENCES Users("UserID") ON DELETE RESTRICT,
    "Status" TEXT,
    PRIMARY KEY("OrderID")
);
```

#### OrderContents

```sql
CREATE TABLE "OrderContents" (
    "OrderID" TEXT NOT NULL REFERENCES Orders("OrderID") ON DELETE RESTRICT,
    "ItemUPC" INTEGER NOT NULL REFERENCES Items("UPC") ON DELETE RESTRICT,
    "Quantity" INTEGER NOT NULL,
    PRIMARY KEY("OrderID", "ItemUPC")
);
```

#### Items

```sql
CREATE TABLE "Items" (
    "UPC" INTEGER UNIQUE NOT NULL, -- Universal Product Code
    "Name" TEXT NOT NULL,
    "Price" REAL NOT NULL,
    PRIMARY KEY("UPC")
);
```

#### Reviews

```sql
CREATE TABLE "Reviews" (
    "OrderID" TEXT UNIQUE NOT NULL REFERENCES Orders("OrderID") ON DELETE RESTRICT,
    "ReviewerID" TEXT NOT NULL REFERENCES Users("UserID") ON DELETE RESTRICT,
    "Rating" INTEGER NOT NULL CHECK (Rating >= 1 AND Rating <= 5),
    "Comments" TEXT,
    PRIMARY KEY("OrderID", "ReviewerID")
);
```