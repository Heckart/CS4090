#ifndef APIBACKEND
#define APIBACKEND

#include <cpprest/http_listener.h>
#include <cpprest/json.h>

/**
 * @brief Handles GET requests to collect and return information from a DB
 *
 * @param request The HTTP request object containing the DB request
 *
 * @return Nothing. Responds to the client with the requested information.
 */
void handle_get(web::http::http_request request);

/**
 * @brief Handles POST requests to receive user information in JSON format.
 *
 * @param request The HTTP request object containing the JSON payload.
 *
 * @return Nothing. Responds to the client that info has been received or that
 * an error as occured.
 */
void handle_post(web::http::http_request request);

/**
 * @brief Handles inserting information from an order in to the proper DB
 * tables.
 *
 * @param Includes all of the information taken from the handle_POST function.
 *
 * @return Nothing. Values are inserted into the DB and the result is logged to
 * the console.
 */
void insert_to_db(const std::string &orderID, const std::string &userID,
                  const std::string &addressPrimary,
                  const std::string &addressSecondary, const std::string &city,
                  const std::string &state, const std::string &zipCode,
                  const std::string &businessName,
                  const std::string &businessAddress,
                  const std::string &status);

#endif
