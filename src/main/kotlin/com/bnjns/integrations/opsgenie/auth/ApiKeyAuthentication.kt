package com.bnjns.integrations.opsgenie.auth

/**
 * Class to authenticate using an API key.
 *
 * Use this if you wish to authenticate using a long-lived [API key][key]. The authorization header will be prefixed
 * with "GenieKey":
 *
 * ```
 * Authorization: GenieKey [key]
 * ```
 */
class ApiKeyAuthentication(
    private val key: String
) : Credentials {
    override fun getAuthHeader(): String = "$PREFIX $key"

    companion object {
        private const val PREFIX = "GenieKey"
    }
}
