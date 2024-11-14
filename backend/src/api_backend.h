#ifndef APIBACKEND
#define APIBACKEND

#include <cpprest/http_listener.h>
#include <cpprest/json.h>

/**
 * @brief Utility function to generate a UUID for orderID.
 *
 * @return A 37 character UUID as a string.
 */
std::string generate_uuid();

/**
 * @brief Main listener function that routes requests.
 *
 * @param request The HTTP object containing the JSON payload.
 *
 * @return Nothing.
 */
void handle_request(web::http::http_request request);

/**
 * @brief Handles POST requests in JSON format to start an order.
 *
 * @param request The HTTP request object containing the JSON payload.
 *
 * @return Nothing. Responds to the client with an orderID and business info or
 * that an error as occured.
 */
void handle_start_order(web::http::http_request request);

/**
 * @brief Handles POST requests in JSON format to receive avaialable groceries.
 *
 * @param requst The HTTP request constaining the JSON payload.
 *
 * @return Nothing. Responds to the client with an list of groceries avaialable
 * at the requested business.
 */
void handle_select_business(web::http::http_request request);

/**
 * @brief Handles inserting information from an order in to the proper DB
 * tables.
 *
 * @param userID The userID of the new order maker as supplied by POST request.
 *
 * @return The UUID generated for use as an orderID.
 */
std::string insert_order_to_db(const std::string &userID);

/**
 * @brief Returns a list of business and their information from the business
 * info table.
 *
 * @return A JSON collection of businesses in the DB.
 */
web::json::value fetch_businesses_from_db();

/**
 * @brief Returns a list of items sold by a business.
 *
 * @return A JSON collection of items.
 */
web::json::value fetch_items_from_db(const std::string &businessID);

#endif
