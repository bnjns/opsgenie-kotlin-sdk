package com.bnjns.integrations.opsgenie

import com.fasterxml.jackson.databind.ObjectMapper

val objectMapper: ObjectMapper = ObjectMapper().findAndRegisterModules()
//    .also { it.configure() }
