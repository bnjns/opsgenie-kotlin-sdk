package com.bnjns.integrations.opsgenie

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper

fun ObjectMapper.configure() {
    enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}
