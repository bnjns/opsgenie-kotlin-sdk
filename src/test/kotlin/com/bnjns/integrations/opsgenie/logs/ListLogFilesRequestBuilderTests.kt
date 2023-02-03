package com.bnjns.integrations.opsgenie.logs

import com.bnjns.integrations.opsgenie.applyBuilder
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.http.*
import java.time.LocalDateTime

class ListLogFilesRequestBuilderTests : BehaviorSpec() {
    init {
        Given("a configured builder") {
            val builder = makeBuilder(
                markerDate = LocalDateTime.of(2023, 1, 10, 10, 0, 0),
                limit = 50
            )

            When("verifying the config") {
                Then("no exception should be thrown") {
                    shouldNotThrow<AssertionError> {
                        builder.verify()
                    }
                }
            }

            When("building the http request") {
                val httpRequest = HttpRequestBuilder().applyBuilder(builder).build()

                Then("the URL should be set") {
                    httpRequest.url.encodedPath shouldBe "/v2/logs/list/2023-01-10-10-00-00_000"
                }
                Then("the method should be set") {
                    httpRequest.method shouldBe HttpMethod.Get
                }
                Then("the limit parameter should be set") {
                    httpRequest.url.parameters["limit"] shouldBe "50"
                }
            }
        }

        Given("a builder with a non-zero nano second value") {
            val builder = makeBuilder(
                markerDate = LocalDateTime.of(2023, 1, 10, 10, 0, 0, 123000000)
            )

            When("building the http request") {
                val httpRequest = HttpRequestBuilder().applyBuilder(builder).build()

                Then("the URL should be set") {
                    httpRequest.url.encodedPath shouldBe "/v2/logs/list/2023-01-10-10-00-00_123"
                }
            }
        }

        Given("a builder with no date configured") {
            val builder = makeBuilder(markerDate = null)

            When("verifying the config") {
                val exception = shouldThrow<AssertionError> {
                    builder.verify()
                }

                Then("an assertion error should be thrown") {
                    exception.message shouldBe "you must provide the marker date"
                }
            }
        }

        Given("a builder with no limit set") {
            val builder = makeBuilder(limit = null)

            When("verifying the config") {
                Then("no exception should be thrown") {
                    shouldNotThrow<AssertionError> {
                        builder.verify()
                    }
                }
            }

            When("building the http request") {
                val httpRequest = HttpRequestBuilder().applyBuilder(builder).build()

                Then("there should be no limit parameter") {
                    httpRequest.url.parameters["limit"].shouldBeNull()
                }
            }
        }

        Given("a builder with too small a limit") {
            val builder = makeBuilder(limit = 0)

            When("verifying the config") {
                val exception = shouldThrow<AssertionError> {
                    builder.verify()
                }

                Then("an assertion error should be thrown") {
                    exception.message shouldBe "limit must be greater than 0"
                }
            }
        }

        Given("a builder with too large a limit") {
            val builder = makeBuilder(limit = 1001)

            When("verifying the config") {
                val exception = shouldThrow<AssertionError> {
                    builder.verify()
                }

                Then("an assertion error should be thrown") {
                    exception.message shouldBe "limit cannot be more than 1000"
                }
            }
        }
    }

    private fun makeBuilder(
        markerDate: LocalDateTime? = LocalDateTime.now(),
        limit: Int? = 50
    ): ListLogFilesRequestBuilder = ListLogFilesRequestBuilder().apply {
        markerDate?.also { this.markerDate(it) }
        limit?.also { this.limit(it) }
    }
}
