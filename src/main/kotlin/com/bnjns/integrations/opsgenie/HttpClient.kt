package com.bnjns.integrations.opsgenie

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.reflect.full.createInstance

suspend inline fun <reified B : RequestBuilder> HttpClient.doRequest(
    block: B.() -> Unit,
    noinline additionalConfig: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val builder = B::class.createInstance()
        .apply(block)
        .also { it.verify() }

    return this.request {
        applyBuilder(builder, additionalConfig)
    }
}

fun <B : RequestBuilder> HttpRequestBuilder.applyBuilder(
    builder: B,
    additionalConfig: HttpRequestBuilder.() -> Unit = {}
): HttpRequestBuilder = apply {
    apply(builder.build())
    apply(additionalConfig)
    url(builder.url())
    method = builder.method
}

suspend inline fun <reified B : RequestBuilder, reified R> HttpClient.standard(
    block: B.() -> Unit,
    noinline additionalConfig: HttpRequestBuilder.() -> Unit = {}
): R = doRequest(block = block, additionalConfig = additionalConfig)
    .body<ApiResponse.Standard<R>>()
    .data

suspend inline fun <reified B : RequestBuilder, reified R> HttpClient.paginated(
    noinline block: B.() -> Unit,
    noinline additionalConfig: HttpRequestBuilder.() -> Unit = {}
): PaginatedItems<R> = doRequest(block = block, additionalConfig = additionalConfig)
    .body<ApiResponse.Paginated<R>>()
    .let { response ->
        PaginatedItems(
            httpClient = this,
            items = response.data,
            paging = response.paging
        )
    }
