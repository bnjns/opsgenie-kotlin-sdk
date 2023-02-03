package com.bnjns.integrations.opsgenie.oncall

data class OnCallParticipant(
    val type: Type,
    val id: String,
    val name: String,
    val escalationTime: Int? = null,
    val notifyType: NotifyType? = null,
    val onCallParticipants: List<OnCallParticipant>? = null
) {
    enum class Type { Team, Escalation, Schedule, User }
    enum class NotifyType { Default, Next, Previous }
}

data class OnCallRecipient(
    val type: OnCallParticipant.Type,
    val id: String,
    val name: String,
    val forwardedFrom: OnCallRecipient? = null
)
