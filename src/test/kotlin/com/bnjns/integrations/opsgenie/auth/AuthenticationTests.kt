package com.bnjns.integrations.opsgenie.auth

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class AuthenticationTests : FunSpec() {
    init {
        test("basic authentication should set the correct header") {
            val username = "username"
            val password = "password"

            val auth = BasicAuthentication(username = username, password = password)
            val authHeader = auth.getAuthHeader()

            authHeader shouldBe "Basic dXNlcm5hbWU6cGFzc3dvcmQ="
        }

        test("api key authentication should set the correct header") {
            val apiKey = "api-key-value"

            val auth = ApiKeyAuthentication(key = apiKey)
            val authHeader = auth.getAuthHeader()

            authHeader shouldBe "GenieKey api-key-value"
        }
    }
}
