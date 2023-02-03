package com.bnjns.integrations.opsgenie.account

import com.bnjns.integrations.opsgenie.applyBuilder
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.http.*

class GetAccountInfoBuilderTests : BehaviorSpec() {
    init {
        Given("a configured builder") {
            val builder = GetAccountInfoRequestBuilder()

            When("verifying the config should not throw an exception") {
                Then("an exception should not be thrown") {
                    shouldNotThrow<AssertionError> {
                        builder.verify()
                    }
                }
            }

            When("configuring the http request") {
                val httpRequest = HttpRequestBuilder().applyBuilder(builder).build()

                Then("the URL should be configured") {
                    httpRequest.url.encodedPath shouldBe "/v2/account"
                }
                Then("the method should be configured") {
                    httpRequest.method shouldBe HttpMethod.Get
                }
            }
        }
    }
}
