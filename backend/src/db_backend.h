#ifndef DBBACKEND
#define DBBACKEND
#include <cpprest/json.h>

/**
 * @brief Utility function to generate a UUID for orderID.
 *
 * @return A 37 character UUID as a string.
 */
std::string generate_uuid();

/**
 * @brief Handles inserting information from an order in to the proper DB
 * tables.
 *
 * @param userID The userID of the new order maker as supplied by POST request.
 *
 * @param addressInfo The address for the delivery of the order as supplied by
 * POST request.
 *
 * @return The UUID generated for use as an orderID.
 */
std::string insert_order_to_db(const std::string &userID,
                               const web::json::value &addressInfo);

/**
 * @brief Finds and returns a list of business and their information from the
 * business info table.
 *
 * @return A JSON collection of businesses in the DB.
 */
web::json::value fetch_businesses_from_db();

/**
 * @brief Finds and returns a list of items sold by a business from the Items
 * table.
 *
 * @return A JSON collection of items.
 */
web::json::value fetch_items_from_db(const std::string &businessID);

/**
 * @brief Finds and returns a list of shoppers from the JobBoard table.
 *
 * @return A JSON collection of shoppers.
 */
web::json::value fetch_shoppers_from_db();

#endif
