package com.bnjns.integrations.opsgenie

import com.bnjns.integrations.opsgenie.account.AccountApi
import com.bnjns.integrations.opsgenie.auth.ApiKeyAuthentication
import com.bnjns.integrations.opsgenie.auth.BasicAuthentication
import com.bnjns.integrations.opsgenie.auth.Credentials
import com.bnjns.integrations.opsgenie.logs.LogsApi
import com.bnjns.integrations.opsgenie.oncall.OnCallApi
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.util.*
import mu.KotlinLogging

private val kLogger = KotlinLogging.logger(OpsgenieClient::class.java.name)

class OpsgenieClient(
    baseUrl: String,
    private val credentials: Credentials
) {
    private val httpClient: HttpClient = HttpClient(CIO) {
        expectSuccess = true

        Charsets {
            register(Charsets.UTF_8)
        }

        install(DefaultRequest) {
            url(baseUrl)
            header(HttpHeaders.Authorization, credentials.getAuthHeader())
            headers.appendIfNameAbsent(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    kLogger.trace { message }
                }
            }
        }

        install(ContentNegotiation) {
            jackson {
                configure()
            }
        }
    }

    val account = AccountApi(this.httpClient)
    val logs = LogsApi(this.httpClient)
    val oncall = OnCallApi(this.httpClient)

    class Builder {
        private var baseUrl: String = "https://api.opsgenie.com"
        private lateinit var creds: Credentials

        /**
         * Sets the base URL of the client to use the EU Opsgenie API; otherwise the US API is used.
         */
        fun eu() {
            baseUrl = "https://api.eu.opsgenie.com"
        }

        /**
         * Configures the credentials the client will use to authenticate with Opsgenie. The [block] must return a valid
         * credentials instance, using either one of the DSL methods or by instantiating the class explicitly.
         *
         * To use API key authentication:
         *
         * ```kotlin
         * credentials {
         *     apiKey("...")
         * }
         *
         * credentials {
         *     ApiKeyAuthentication("...")
         * }
         * ```
         *
         * To use basic authentication:
         *
         * ```kotlin
         * credentials {
         *     basic(username = "...", password = "...")
         * }
         *
         * credentials {
         *     BasicAuthentication(username = "...", password = "...")
         * }
         * ```
         *
         * @see Credentials
         * @see CredentialsConfig
         */
        fun credentials(block: CredentialsConfig.() -> Credentials) {
            creds = block(CredentialsConfig())
        }

        /**
         * Build a [client][OpsgenieClient] using the configured properties.
         */
        fun build(): OpsgenieClient {
            assert(::creds.isInitialized) { "you must configure either basic or API key credentials" }

            return OpsgenieClient(
                baseUrl = baseUrl,
                credentials = creds
            )
        }

        class CredentialsConfig {
            /**
             * Builds a [BasicAuthentication] class using the given [username] and [password].
             *
             * @see BasicAuthentication
             */
            fun basic(username: String, password: String) =
                BasicAuthentication(username = username, password = password)

            /**
             * Builds an [ApiKeyAuthentication] class using the given API [key].
             *
             * @see ApiKeyAuthentication
             */
            fun apiKey(key: String) = ApiKeyAuthentication(key = key)
        }
    }
}

/**
 * Creates a new [Opsgenie client][OpsgenieClient], using the provided [block] to configure the client.
 */
fun opsgenieClient(block: OpsgenieClient.Builder.() -> Unit) =
    OpsgenieClient.Builder()
        .also(block)
        .build()
