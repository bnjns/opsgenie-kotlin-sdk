package com.bnjns.integrations.opsgenie.auth

import java.nio.charset.Charset
import java.util.*

/**
 * Class to authenticate using an HTTP basic authentication.
 *
 * Use this if you wish to authenticate using a [username] and [password]. When sending in the Authorization header, the
 * username and password are concatenated with a colon and base64-encoded.
 *
 * ```
 * Authorization: Basic base64([username]:[password])
 * ```
 */
class BasicAuthentication(
    private val username: String,
    private val password: String
) : Credentials {
    override fun getAuthHeader(): String =
        "$PREFIX ${ENCODER.encodeToString("$username:$password".toByteArray(Charset.defaultCharset()))}"

    companion object {
        private const val PREFIX = "Basic"
        private val ENCODER = Base64.getEncoder()
    }
}
