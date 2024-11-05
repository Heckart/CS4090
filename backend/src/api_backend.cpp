#include "api_backend.h"

#include <cppconn/prepared_statement.h>
#include <cpprest/http_listener.h>
#include <cpprest/json.h>
#include <mysql_connection.h>
#include <mysql_driver.h>

#include <iostream>

void handle_get(web::http::http_request request) {
  // creating a dummy object for now
  // TODO: update this once we know whats needed
  web::json::value userInfo;
  userInfo[U("name")] = web::json::value::string(U("2% Milk"));
  userInfo[U("price")] = web::json::value::number(3);

  request.reply(web::http::status_codes::OK, userInfo);
}

void handle_post(web::http::http_request request) {
  request.extract_json()
      .then([=](web::json::value jsonObject) {
        try {
          // Extract data from the request body
          std::string orderID = jsonObject[U("orderID")].as_string();
          std::string userID = jsonObject[U("userID")].as_string();
          // deliveryAddress: AddressInformation class
          std::string addressPrimary =
              jsonObject[U("addressPrimary")].as_string();
          std::string addressSecondary =
              jsonObject[U("addressSecondary")].as_string();
          std::string city = jsonObject[U("city")].as_string();
          std::string state =
              jsonObject[U("state")].as_string(); // I believe this can just be
                                                  // a 2 letter string here
          std::string zipCode = jsonObject[U("zipCode")].as_string();
          // businessInfo: businessInformation class
          std::string businessName =
              jsonObject[U("businessInformation")].as_string();
          std::string businessAddress =
              jsonObject[U("businessAddress")].as_string();
          // status: OrderStatus class (enum of statuses)
          std::string status =
              jsonObject[U("status")]
                  .as_string(); // ORDERING, CONFIMRED, DISPATCHED, DELIVERED,
                                // CANCELLED
                                // items: Hash map of <UPC (int), OrderItem>
                                // TODO: how to hande??
          // timeSlot: orderTimeSlot class (constains shopper class and date)
          std::string shopperID = jsonObject[U("shopperID")].as_string();
          std::string firstName = jsonObject[U("firstName")].as_string();
          std::string lastName = jsonObject[U("lastName")].as_string();
          // TODO: hanlde date somehow
          // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.js/-date/

          // log it...probably can delete
          std::cout << L"found orderID: " << orderID << std::endl;

          // Respond to the client
          request.reply(web::http::status_codes::OK, U("User info received."));

          // TODO: here the new data needs to be added to the database
          insert_to_db(orderID, userID, addressPrimary, addressSecondary, city,
                       state, zipCode, businessName, businessAddress, status,
                       shopperID, firstName, lastName);

        } catch (const std::exception &e) {
          request.reply(web::http::status_codes::BadRequest,
                        U("Something went wrong, perhaps invalid data?"));
        }
      })
      .wait();
}

void insert_to_db(const std::string &orderID, const std::string &userID,
                  const std::string &addressPrimary,
                  const std::string &addressSecondary, const std::string &city,
                  const std::string &state, const std::string &zipCode,
                  const std::string &businessName,
                  const std::string &businessAddress, const std::string &status,
                  const std::string &shopperID, const std::string &firstName,
                  const std::string &lastName) {
  try {
    sql::mysql::MySQL_Driver *driver;
    sql::Connection *conn;

    // Connect to the database
    driver = sql::mysql::get_mysql_driver_instance();
    conn = driver->connect("tcp://localhost:3306", "username", "password");
    conn->setSchema("api_database");

    // Create a prepared statement to insert data
    std::unique_ptr<sql::PreparedStatement> pstmt(conn->prepareStatement(
        "INSERT INTO Orders (OrderID, UserID, Status) VALUES (?, ?, ?)"));

    pstmt->setString(1, orderID); // Bind values
    pstmt->setString(2, userID);
    pstmt->setString(3, status);

    pstmt->executeUpdate(); // Execute the insertion

    std::cout << "Order data inserted successfully!" << std::endl;

    std::unique_ptr<sql::PreparedStatement> pstmt2(
        conn->prepareStatement("INSERT INTO location (city, state, address, "
                               "zip_code) VALUES (?, ?, ?, ?)"));

    pstmt2->setString(1, city); // Bind values
    pstmt2->setString(2, state);
    pstmt2->setString(3, addressPrimary);
    pstmt2->setString(4, zipCode);

    pstmt2->executeUpdate(); // Execute the insertion

    std::cout << "location data inserted successfully!" << std::endl;

    // TODO: addressSecondary, businessName, businesAddress, shopperID,
    // firstName, and lastName are unused. Further, we realy need to iron out
    // how exactly we are going to appropriately handle the data we get from an
    // order request.

    delete conn; // Clean up connection
  } catch (sql::SQLException &e) {
    std::cerr << "SQL Exception: " << e.what() << std::endl;
  }
}
