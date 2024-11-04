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

#endif
