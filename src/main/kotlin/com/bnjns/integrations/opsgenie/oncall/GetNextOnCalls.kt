package com.bnjns.integrations.opsgenie.oncall

import com.bnjns.integrations.opsgenie.RequestBuilder
import com.bnjns.integrations.opsgenie.schedule.IdentifierType
import com.bnjns.integrations.opsgenie.schedule.Schedule
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.client.request.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GetNextOnCallsRequestBuilder : RequestBuilder {
    private lateinit var scheduleIdentifier: String
    private var scheduleIdentifierType: IdentifierType = IdentifierType.Id
    private var date: ZonedDateTime? = null

    fun id(id: String) {
        scheduleIdentifier = id
        scheduleIdentifierType = IdentifierType.Id
    }

    fun name(name: String) {
        scheduleIdentifier = name
        scheduleIdentifierType = IdentifierType.Name
    }

    fun startingFrom(date: ZonedDateTime) {
        this.date = date
    }

    fun startingNow() {
        date = null
    }

    override fun url(): String = "/v2/schedules/$scheduleIdentifier/next-on-calls"

    override fun verify() {
        assert(::scheduleIdentifier.isInitialized) { "you must provide the schedule name or ID" }
    }

    override fun build(): HttpRequestBuilder.() -> Unit = {
        parameter("scheduleIdentifierType", scheduleIdentifierType.name.lowercase())
        parameter("date", date?.let(DateTimeFormatter.ISO_OFFSET_DATE_TIME::format))
    }
}


sealed class GetNextOnCallsResponse {
    data class Standard(
        @JsonProperty("_parent")
        val parent: Schedule,
        val nextOnCallRecipients: List<OnCallRecipient>,
        val exactNextOnCallRecipients: List<OnCallRecipient>
    )

    data class Flat(
        @JsonProperty("_parent")
        val parent: Schedule,
        val nextOnCallParticipants: List<String>,
        val exactNextOnCallParticipants: List<String>
    ) : GetNextOnCallsResponse()
}
