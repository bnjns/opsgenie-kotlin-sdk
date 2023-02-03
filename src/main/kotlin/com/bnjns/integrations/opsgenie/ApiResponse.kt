package com.bnjns.integrations.opsgenie

/**
 * This class represents the body of a response from the Opsgenie API. Each response includes a [requestId] which can be
 * provided when contacting Opsgenie for assistance, details of how long the request [took] to process in seconds, and
 * an optional human-friendly message that represents the [result].
 *
 * @property requestId the unique ID of the request, which can be provided if contacting Opsgenie for assistance
 * @property took how long the request took to serve, in seconds
 * @property result an optional human-friendly message that represents the status of the request
 * @see [API documentation](https://docs.opsgenie.com/docs/response)
 */
sealed class ApiResponse {
    abstract val requestId: String
    abstract val took: Double
    abstract val result: String?

    /**
     * A standard response for JSON objects, or unpaginated lists of objects.
     *
     * `Map<String, Any>` can be used instead of a data class to deserialise any response.
     *
     * @param T the type of the response data
     * @property data the entity data
     * @property requestId the unique ID of the request, which can be provided if contacting Opsgenie for assistance
     * @property took how long the request took to serve, in seconds
     * @property result an optional human-friendly message that represents the status of the request
     * @see [API documentation](https://docs.opsgenie.com/docs/response#response)
     */
    data class Standard<T>(
        val data: T,
        override val requestId: String,
        override val took: Double,
        override val result: String? = null
    ) : ApiResponse()

    /**
     * A paginated response which, in addition to the fields in the [standard][Standard] response, includes the
     * [paging] information as URLs of the first, last, and next pages (if applicable).
     *
     * @param T the type of each item in the response data
     * @property data the list of entities
     * @property requestId the unique ID of the request, which can be provided if contacting Opsgenie for assistance
     * @property took how long the request took to serve, in seconds
     * @property result an optional human-friendly message that represents the status of the request
     * @property paging the URLs of the first, last and next pages of results, if applicable
     * @see [API documentation](https://docs.opsgenie.com/docs/pagination)
     */
    data class Paginated<T>(
        val data: List<T>,
        override val result: String? = null,
        override val requestId: String,
        override val took: Double,
        val paging: Paging
    ) : ApiResponse() {
        data class Paging(
            val next: String? = null,
            val first: String,
            val last: String
        )
    }

    /**
     * @property requestId the unique ID of the request, which can be provided if contacting Opsgenie for assistance
     * @property took how long the request took to serve, in seconds
     * @property result an optional human-friendly message that represents the status of the request
     * @property message the details of the error
     */
    data class Error(
        val message: String,
        override val requestId: String,
        override val took: Double,
        override val result: String? = null
    ) : ApiResponse()
}
