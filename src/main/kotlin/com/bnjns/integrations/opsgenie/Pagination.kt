package com.bnjns.integrations.opsgenie

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.net.URL

data class PaginatedItems<T>(
    val httpClient: HttpClient,
    val items: List<T>,
    val paging: ApiResponse.Paginated.Paging
)

/**
 * Fetch the specified page of items from the API.
 *
 * There is no need to rebuild the original request, as the paginated response contains the full URL of the specified
 * pages, including any query parameters from the original request.
 *
 * @param T the type of each item in the response
 * @param url the URL of the page to fetch
 * @return the items in the specified page
 */
suspend inline fun <reified T> PaginatedItems<T>.goToPage(url: String): PaginatedItems<T> {
    val response = httpClient.get(URL(url))
        .body<ApiResponse.Paginated<T>>()

    return copy(
        items = response.data,
        paging = response.paging
    )
}

/**
 * Fetch the next page of items, if applicable.
 *
 * @param T the type of each item in the response
 * @return the items in the next page, or null if there are no more pages
 */
@Suppress("UNUSED")
suspend inline fun <reified T> PaginatedItems<T>.nextPage(): PaginatedItems<T>? = paging.next?.let { goToPage(it) }

/**
 * Fetch the first page of items.
 *
 * @param T the type of each item in the response
 * @return the items in the first page
 */
@Suppress("UNUSED")
suspend inline fun <reified T> PaginatedItems<T>.firstPage(): PaginatedItems<T> = goToPage(paging.first)

/**
 * Fetch the last page of items.
 *
 * @param T the type of each item in the response
 * @return the items in the first page
 */
@Suppress("UNUSED")
suspend inline fun <reified T> PaginatedItems<T>.lastPage(): PaginatedItems<T> = goToPage(paging.last)
