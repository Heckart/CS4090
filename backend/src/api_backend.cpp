#include "api_backend.h"
#include "db_backend.h"

#include <cppconn/prepared_statement.h>
#include <cpprest/http_listener.h>
#include <cpprest/json.h>

#include <iostream>

void handle_request(web::http::http_request request) {
  std::cout << "LOG: Received request\n";
  auto path = web::uri::decode(request.relative_uri().path());

  if (path == U("/startOrder") &&
      request.method() == web::http::methods::POST) {
    handle_start_order(request);
  } else if (path == U("/selectBusiness") &&
             request.method() == web::http::methods::POST) {
    handle_select_business(request);
  } else if (path == U("/selectShopper") &&
             request.method() == web::http::methods::POST) {
    handle_select_shopper(request);
  } else if (path == U("/confirmShopper") &&
             request.method() == web::http::methods::POST) {
    handle_shopper_confirm(request);
  } else if (path == U("/checkout") &&
             request.method() == web::http::methods::POST) {
    handle_checkout(request);
  } else {
    request.reply(web::http::status_codes::NotFound, U("Endpoint not found."));
  }
}

void handle_start_order(web::http::http_request request) {
  request.extract_json()
      .then([=](web::json::value jsonObject) {
        try {

          /** Example of what JSON object strcuture this is looking for:
              {
                "userID": "01272"
                "addressInfo": {
                    addressPrimary: "123 Market Street",
                    addressSecondary: "Apt 301",
                    city: "Rolla",
                    state: "MO",
                    zipCode: "65401"
                    }
               }
           */

          std::string userID = utility::conversions::to_utf8string(
              jsonObject[U("userID")].as_string());
          web::json::value addressInfo = jsonObject[U("addressInfo")];

          std::cout << "LOG: handle_start_order recevied userID: " << userID
                    << std::endl;
          // Insert the order into the database and retrieve the generated
          // orderID
          std::string orderID = insert_order_to_db(userID, addressInfo);

          // Fetch available businesses from the database
          web::json::value businessInfo = fetch_businesses_from_db();

          // Construct response JSON
          web::json::value response;
          response[U("orderID")] = web::json::value::string(
              utility::conversions::to_string_t(orderID));
          response[U("businessInfo")] = businessInfo;

          // Send response to the client

          /** Example of what JSON object structure this replies with:
              {
               "orderID": "NCS-263-A",
               "businessInfo": [
                  {
                    "businessID": "12345",
                    "businessName": "Example Business",
                    "addressInfo": {
                                     "addressPrimary": "123 Main St",
                                     "addressSecondary": "Suite 100",
                                     "city": "Springfield",
                                     "state": "IL",
                                     "zipCode": "62701"
                                     }
                  },
                  {
                    "businessID": "67890",
                    "businessName": "Another Business",
                    "addressInfo": {
                                     "addressPrimary": "456 Elm St",
                                     "addressSecondary": "",
                                     "city": "Greenfield",
                                     "state": "WI",
                                     "zipCode": "53220"
                                   }
                  }
              ]
              }
           */

          request.reply(web::http::status_codes::OK, response);
        } catch (const std::exception &e) {
          request.reply(web::http::status_codes::BadRequest,
                        U("Invalid data."));
        }
      })
      .wait();
}

void handle_select_business(web::http::http_request request) {
  request.extract_json()
      .then([=](web::json::value jsonObject) {
        try {

          /** Example of what JSON object structure this is looking for:
              {
                "userID": "38284",
                "orderID": "38DBC-23S",
                "businessID": "28382"
              }
           */

          // Extract userID, orderID, and businessID from the request JSON
          std::string userID = utility::conversions::to_utf8string(
              jsonObject[U("userID")].as_string());
          std::string orderID = utility::conversions::to_utf8string(
              jsonObject[U("orderID")].as_string());
          std::string businessID = utility::conversions::to_utf8string(
              jsonObject[U("businessID")].as_string());

          std::cout << "LOG: handle_select_business recevied userID " << userID
                    << std::endl;
          std::cout << "LOG: handle_select_business received orderID "
                    << orderID << std::endl;
          std::cout << "LOG: handle_select_business received businessID "
                    << businessID << std::endl;

          // TODO: validate orderID exists?

          // Fetch available items for the selected business from the database
          web::json::value itemsArray = fetch_items_from_db(businessID);

          // Construct response JSON
          web::json::value response;
          response[U("businessID")] = web::json::value::string(
              utility::conversions::to_string_t(businessID));
          response[U("items")] = itemsArray;

          // Send response to the client

          /** Example of what JSON object structure this replies with:
              {
                "businessID": "29432",
                 "items": [
                  {
                    "upc": 123456789012,
                    "name": "Widget A",
                    "price": 19.99
                  },
                  {
                    "upc": 987654321098,
                    "name": "Widget B",
                    "price": 29.99
                  }
                 ]
               }
           */

          request.reply(web::http::status_codes::OK, response);
        } catch (const std::exception &e) {
          request.reply(web::http::status_codes::BadRequest,
                        U("Invalid data."));
        }
      })
      .wait();
}

void handle_select_shopper(web::http::http_request request) {
  request.extract_json()
      .then([=](web::json::value jsonObject) {
        try {

          /** Example of the JSON object structure this is looking for:
              {
                "userID": "84828",
                "orderID": "ABC-3284"
              }
           */

          std::string userID = utility::conversions::to_utf8string(
              jsonObject[U("userID")].as_string());
          std::string orderID = utility::conversions::to_utf8string(
              jsonObject[U("orderID")].as_string());

          std::cout << "LOG: handle_select_shopper received userID: " << userID
                    << std::endl;
          std::cout << "LOG: handle_select_shopper received orderID: "
                    << orderID << std::endl;

          // TODO: validate orderID exists?

          // Fetch available shoppers from the database
          web::json::value shoppersArray = fetch_shoppers_from_db();

          // Construct response JSON
          web::json::value response;
          response[U("shoppers")] = shoppersArray;

          // Send response to the client

          /** Example of the JSON object structure this replies with:
             {
               "shoppers": [
                             {
                               "shopperID": "s123",
                               "firstName": "Alice",
                               "lastName": "Smith",
                               "fulfillmentTime": "2023-11-14 10:30:00"
                             },
                             {
                               "shopperID": "s456",
                               "firstName": "Bob",
                               "lastName": "Johnson",
                               "fulfillmentTime": "2023-11-14 11:45:00"
                             }
                            ]
            }
           */

          request.reply(web::http::status_codes::OK, response);

        } catch (const std::exception &e) {
          request.reply(web::http::status_codes::BadRequest,
                        U("Invalid data."));
        }
      })
      .wait();
}

void handle_shopper_confirm(web::http::http_request request) {
  request.extract_json()
      .then([=](web::json::value jsonObject) {
        try {

          /** Example of the JSON object structure this is looking for:
              {
                "userID": "84828",
                "orderID": "ABC-3284",
                "shopperID": "30543"
              }
           */

          std::string userID = utility::conversions::to_utf8string(
              jsonObject[U("userID")].as_string());
          std::string orderID = utility::conversions::to_utf8string(
              jsonObject[U("orderID")].as_string());
          std::string shopperID = utility::conversions::to_utf8string(
              jsonObject[U("shopperID")].as_string());

          std::cout << "LOG: handle_shopper_confirm received userID: " << userID
                    << std::endl;
          std::cout << "LOG: handle_shopper_confirm recevied orderID: "
                    << orderID << std::endl;
          std::cout << "LOG: handle_shopper_confirm received shopperID: "
                    << shopperID << std::endl;

          // TODO: validate orderID exists?

          // Update the order's shopper in the DB
          insert_shopper_to_order(orderID, shopperID);

          /**
           * Replies with an HTTP 200 OK Code, no JSON object
           */

          // Send response to the client
          request.reply(web::http::status_codes::OK);

        } catch (const std::exception &e) {
          request.reply(web::http::status_codes::BadRequest,
                        U("Invalid Data."));
        }
      })
      .wait();
}

void handle_checkout(web::http::http_request request) {
  request.extract_json()
      .then([=](web::json::value jsonObject) {
        try {
          /** Example of the JSON object structure this is looking for:
              {
                "userID": "29453",
                "orderID": "BCF-453",
                "items": [
                           {
                             "upc": 924292,
                             "quantity": 2
                           },
                           {
                             "upc": 230934,
                             "quantity": 1
                           }
                         ]
              }
           */

          std::string userID = utility::conversions::to_utf8string(
              jsonObject[U("userID")].as_string());
          std::string orderID = utility::conversions::to_utf8string(
              jsonObject[U("orderID")].as_string());
          web::json::value items = jsonObject[U("items")];

          std::cout << "LOG: handle_checkout recevied userID: " << userID
                    << std::endl;
          std::cout << "LOG: handle_checkout received orderID: " << orderID
                    << std::endl;

          // TODO: validate orderID exists?

          // Update the order's items in the DB.
          insert_items_to_order(orderID, items);

          /**
           * Replies with an HTTP 200 OK Code, no JSON object
           */

          // Send response to the client
          request.reply(web::http::status_codes::OK);

        } catch (const std::exception &e) {
          request.reply(web::http::status_codes::BadRequest,
                        U("Invalid Data."));
        }
      })
      .wait();
}
