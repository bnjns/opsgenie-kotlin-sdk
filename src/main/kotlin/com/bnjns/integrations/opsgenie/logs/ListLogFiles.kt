package com.bnjns.integrations.opsgenie.logs

import com.bnjns.integrations.opsgenie.Constants
import com.bnjns.integrations.opsgenie.RequestBuilder
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.ktor.client.request.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

val markerPattern: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss_SSS")


class ListLogFilesRequestBuilder : RequestBuilder {
    private lateinit var marker: LocalDateTime
    private var limit: Int? = null

    fun markerDate(marker: LocalDateTime) {
        this.marker = marker
    }

    fun limit(limit: Int) {
        this.limit = limit
    }

    override fun url(): String = "/v2/logs/list/${marker.toMarker()}"

    override fun verify() {
        assert(::marker.isInitialized) { "you must provide the marker date" }

        limit?.run {
            assert(this > 0) { "limit must be greater than 0" }
            assert(this <= Constants.MaxLimit) { "limit cannot be more than 1000" }
        }
    }

    override fun build(): HttpRequestBuilder.() -> Unit = {
        parameter("limit", limit)
    }
}


data class LogFileDetails(
    val filename: String,
    @JsonDeserialize(using = DateDeserializer::class)
    val date: LocalDateTime,
    val size: Long
) {
    class DateDeserializer : JsonDeserializer<LocalDateTime>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDateTime = LocalDateTime
            .ofInstant(Instant.ofEpochMilli(parser.valueAsLong), TimeZone.getDefault().toZoneId())

    }
}

fun LocalDateTime.toMarker(): String = markerPattern.format(this)
