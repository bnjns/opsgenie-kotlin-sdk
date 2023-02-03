package com.bnjns.integrations.opsgenie

import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldHaveKey
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf

class StandardApiResponseDeserializationTests : FunSpec() {
    data class Example(val key: String)

    init {
        context("a standard response") {
            test("should be deserialisable") {
                val json = """
                {
                    "data": {
                        "key": "value"
                    },
                    "took": 0.1,
                    "requestId": "7f0alpde-3c67-455f-97ec-24754432d413",
                    "result": "The request was processed"
                }
            """.trimIndent()

                val response = objectMapper.readValue<ApiResponse.Standard<Example>>(json)

                response.data.key shouldBe "value"
                response.took shouldBe 0.1
                response.requestId shouldBe "7f0alpde-3c67-455f-97ec-24754432d413"
                response.result shouldBe "The request was processed"
            }

            test("missing the result field should still be deserialisable") {
                val json = """
                {
                    "data": {
                        "key": "value"
                    },
                    "took": 0.1,
                    "requestId": "7f0alpde-3c67-455f-97ec-24754432d413"
                }
            """.trimIndent()

                val response = objectMapper.readValue<ApiResponse.Standard<Example>>(json)

                response.result.shouldBeNull()
            }

            test("should be deserialisable to a map") {
                val json = """
                {
                    "data": {
                        "key": "value"
                    },
                    "took": 0.1,
                    "requestId": "7f0alpde-3c67-455f-97ec-24754432d413"
                }
            """.trimIndent()

                val response = objectMapper.readValue<ApiResponse.Standard<Map<String, Any>>>(json)

                response.data.shouldHaveKey("key")
                response.data["key"].shouldBeTypeOf<String>()
                response.data["key"] shouldBe "value"
            }

            test("should be deserialisable to a list") {
                val json = """
                {
                    "data": [
                        "Item 1",
                        "Item 2"
                    ],
                    "took": 0.1,
                    "requestId": "7f0alpde-3c67-455f-97ec-24754432d413"
                }
            """.trimIndent()

                val response = objectMapper.readValue<ApiResponse.Standard<List<String>>>(json)

                response.data.shouldHaveSize(2)
                response.data shouldContainExactly listOf("Item 1", "Item 2")
            }
        }

        context("a paginated response") {
            test("should be deserialisable") {
                val json = """
                {
                    "data": [
                      {
                        "key": "value 1"
                      },
                      {
                        "key": "value 2"
                      }
                    ],
                    "took": 0.1,
                    "requestId": "7f0alpde-3c67-455f-97ec-24754432d413",
                    "result": "The request was processed",
                    "paging": {
                        "first": "http://localhost/first",
                        "last": "http://localhost/last",
                        "next": "http://localhost/next"
                    }
                }
            """.trimIndent()

                val response = objectMapper.readValue<ApiResponse.Paginated<Example>>(json)
                response.data.shouldHaveSize(2)
                response.data[0].key shouldBe "value 1"
                response.data[1].key shouldBe "value 2"
                response.took shouldBe 0.1
                response.requestId shouldBe "7f0alpde-3c67-455f-97ec-24754432d413"
                response.result shouldBe "The request was processed"
                response.paging.first shouldBe "http://localhost/first"
                response.paging.last shouldBe "http://localhost/last"
                response.paging.next shouldBe "http://localhost/next"
            }

            test("missing the result field should still be deserialisable") {
                val json = """
                {
                    "data": [
                      {
                        "key": "value 1"
                      },
                      {
                        "key": "value 2"
                      }
                    ],
                    "took": 0.1,
                    "requestId": "7f0alpde-3c67-455f-97ec-24754432d413",
                    "paging": {
                        "first": "http://localhost/first",
                        "last": "http://localhost/last",
                        "next": "http://localhost/next"
                    }
                }
            """.trimIndent()

                val response = objectMapper.readValue<ApiResponse.Paginated<Example>>(json)

                response.result.shouldBeNull()
            }

            test("no next page should still be deserialsable") {
                val json = """
                {
                    "data": [
                      {
                        "key": "value 1"
                      },
                      {
                        "key": "value 2"
                      }
                    ],
                    "took": 0.1,
                    "requestId": "7f0alpde-3c67-455f-97ec-24754432d413",
                    "paging": {
                        "first": "http://localhost/first",
                        "last": "http://localhost/last"
                    }
                }
            """.trimIndent()

                val response = objectMapper.readValue<ApiResponse.Paginated<Example>>(json)

                response.paging.next.shouldBeNull()
            }
        }

        context("an error response") {
            test("should be deserialisable") {
                val json = """
                    {
                        "message": "The error message",
                        "took": 0.1,
                        "requestId": "7f0alpde-3c67-455f-97ec-24754432d413"
                    }
                """.trimIndent()

                val response = objectMapper.readValue<ApiResponse.Error>(json)

                response.message shouldBe "The error message"
                response.took shouldBe 0.1
                response.requestId shouldBe "7f0alpde-3c67-455f-97ec-24754432d413"
                response.result.shouldBeNull()
            }
        }
    }
}
