package com.bnjns.integrations.opsgenie

import io.ktor.client.request.*
import io.ktor.http.*

/**
 *
 */
interface RequestBuilder {
    val method: HttpMethod
        get() = HttpMethod.Get

    /**
     * The URL of the API to call, not including query parameters.
     */
    fun url(): String

    /**
     * Used to verify the request before sending it to the Opsgenie API.
     *
     * - Required properties have been set
     * - Properties are configured within predetermined boundaries (eg, min/max value)
     *
     * Misconfigurations should throw an [AssertionError] with a meaningful error message.
     */
    fun verify()

    /**
     * Creates the function to configure the HTTP request, using the builder's properties.
     */
    fun build(): HttpRequestBuilder.() -> Unit
}
