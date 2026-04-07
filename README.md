# Shopping Cart Application with Database Localization

This project extends the original Shopping Cart Application by:
- loading UI localization strings from a MariaDB database instead of ResourceBundle properties files
- saving shopping cart summary data into the `cart_records` table
- saving cart item details into the `cart_items` table with a foreign key relationship

## Technologies
- Java 
- JavaFX
- Maven
- MariaDB
- Docker
- Jenkins

## Database Setup

Create the database:

```sql
CREATE DATABASE IF NOT EXISTS shopping_cart_localization
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

Execute the following files:

database/schema.sql
database/localization_data.sql

This will create:

cart_records
cart_items
localization_strings

## Run the project

```bash
mvn clean javafx:run


### 7. Features
```md
## Features
- Add items to shopping cart
- Calculate total price
- Save cart summary to database
- Save cart items to database
- Switch UI language dynamically from database values