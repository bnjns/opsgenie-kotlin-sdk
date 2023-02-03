package com.bnjns.integrations.opsgenie.account

import com.bnjns.integrations.opsgenie.RequestBuilder
import io.ktor.client.request.*

/**
 * Builds the request for the Get Account Info API.
 *
 * @see <https://docs.opsgenie.com/docs/account-api#get-account-info>
 */
class GetAccountInfoRequestBuilder : RequestBuilder {
    override fun url(): String = "/v2/account"

    override fun verify() = Unit

    override fun build(): HttpRequestBuilder.() -> Unit = {}
}
