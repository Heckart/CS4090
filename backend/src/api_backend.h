#ifndef APIBACKEND
#define APIBACKEND

#include <cpprest/http_listener.h>

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
 * @brief Handles POST requests in JSON format to get available groceries.
 *
 * @param request The HTTP request constaining the JSON payload.
 *
 * @return Nothing. Responds to the client with a list of groceries avaialable
 * at the requested business.
 */
void handle_select_business(web::http::http_request request);

/**
 * @brief Handles POST requests in JSON format to get available shoppers.
 *
 * @param request The HTTP request containing the JSON payload.
 *
 * @return Nothing. Responds to the client with a list of shoppers.
 */
void handle_select_shopper(web::http::http_request request);

/**
 * @brief Handles POST requests in JSON format to get a shopper confirmation.
 *
 * @param request The HTTP request containing the JSON payload.
 *
 * @return Nothing. Responds to the client with an OK.
 */
void handle_shopper_confirm(web::http::http_request request);

/**
 * @brief Handles POST  requests in JSON format to confirm an order.
 *
 * @param request The HTTP request containing the JSON payload.
 *
 * @param Nothing. Responds to the client with an OK.
 */
void handle_checkout(web::http::http_request request);
#endif
