# Shopping Cart Application with Database Localization
## Overview

This project extends the original Shopping Cart Application by:

loading UI localization strings from a MariaDB database instead of ResourceBundle property files
saving shopping cart summary data into the cart_records table
saving individual cart items into the cart_items table with a foreign key relationship

The application supports multiple languages dynamically using data stored in the database.

## Technologies
- Java 21
- JavaFX
- Maven
- MariaDB
- Docker
- Jenkins

## Project Structure

shopping-cart/
├── src/
├── database/
│ ├── schema.sql
│ └── localization_data.sql
├── Dockerfile
├── Jenkinsfile
├── pom.xml
└── README.md

## Database Setup
1. Create the database

CREATE DATABASE IF NOT EXISTS shopping_cart_localization
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

2. Run the SQL files

Execute these files:

database/schema.sql
database/localization_data.sql

This will create:

cart_records
cart_items
localization_strings
Configuration

Update database credentials in:

src/main/java/com/example/shoppingcart/util/DatabaseConnection.java

Example:

private static final String URL = "jdbc:mariadb://localhost:3306/shopping_cart_localization";
private static final String USER = "shopping_user";
private static final String PASSWORD = "1234";

Run the project

mvn clean javafx:run

## Features
Add items to the shopping cart
Calculate total price
Save cart summary into the database
Save cart items to the database
Switch UI language dynamically from database values
Supports multiple languages:
  . English
  . Finnish
  . Swedish
  . Japanese
  . Arabic

## How it works
UI text is loaded from the localization_strings table
Language selection updates the UI dynamically
Cart data is stored using CartService
Database connection is handled via DatabaseConnection

## Docker
docker build -t shopping-cart .

## Jenkins
Pipeline stages:

Checkout
Build
Test
Package
Docker build
Submission Evidence


## Notes
ResourceBundle is no longer used
all UI text comes from database
make sure MariaDB is running before starting