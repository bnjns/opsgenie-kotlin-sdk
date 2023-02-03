package com.bnjns.integrations.opsgenie.oncall

import com.bnjns.integrations.opsgenie.standard
import io.ktor.client.*
import io.ktor.client.request.*

class OnCallApi(
    private val httpClient: HttpClient
) {
    suspend fun getOnCalls(block: GetOnCallsRequestBuilder.() -> Unit): GetOnCallsResponse.Standard =
        httpClient.standard(block) {
            parameter("flat", false)
        }

    suspend fun getOnCallsFlat(block: GetOnCallsRequestBuilder.() -> Unit): GetOnCallsResponse.Flat =
        httpClient.standard(block) {
            parameter("flat", true)
        }

    suspend fun getNextOnCalls(block: GetNextOnCallsRequestBuilder.() -> Unit): GetNextOnCallsResponse.Standard =
        httpClient.standard(block) {
            parameter("flat", false)
        }

    suspend fun getNextOnCallsFlat(block: GetNextOnCallsRequestBuilder.() -> Unit): GetNextOnCallsResponse.Flat =
        httpClient.standard(block) {
            parameter("flat", true)
        }
}
