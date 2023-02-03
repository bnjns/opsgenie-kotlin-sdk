package com.bnjns.integrations.opsgenie.auth

/**
 * Common interface for authenticating with Opsgenie.
 *
 * All requests to the Opsgenie API must be authenticated, using any of the supported methods:
 *
 * - [Basic authentication][BasicAuthentication]
 * - [API Key authentication][ApiKeyAuthentication]
 *
 * For each method, authentication is achieved by providing the appropriate value in the `Authorization` header using
 * [getAuthHeader].
 *
 * @see <https://docs.opsgenie.com/docs/authentication>
 */
sealed interface Credentials {
    /**
     * Builds the value authentication header.
     *
     * All requests to Opsgenie must be authenticated using the `Authentication` header, but the value depends on the
     * type of authentication being used.
     */
    fun getAuthHeader(): String
}

