package com.bnjns.integrations.opsgenie.logs

import com.bnjns.integrations.opsgenie.RequestBuilder
import io.ktor.client.request.*
import java.time.LocalDateTime

/**
 * Builds the request to download a log file.
 *
 * The name of the file can be provided using either a [LocalDateTime] or the included DSL:
 *
 * ```kotlin
 * client.logs.downloadLogFile {
 *     filename {
 *         year = 2023
 *         month = 1
 *         day = 13
 *         hour = 22
 *         minute = 47
 *         second = 30
 *         millisecond = 123
 *     }
 * }
 * ```
 *
 * @see <https://docs.opsgenie.com/docs/logs-api#generate-log-file-download-link>
 */
class DownloadLogFileRequestBuilder : RequestBuilder {
    private lateinit var fileDate: LocalDateTime

    fun filename(date: LocalDateTime) {
        this.fileDate = date
    }

    fun filename(block: FilenameBuilder.() -> Unit) {
        this.fileDate = FilenameBuilder().apply(block).build()
    }

    override fun url(): String = "/v2/logs/download/${fileDate.toMarker()}.json"

    override fun verify() {
        assert(::fileDate.isInitialized) { "you must provide the file date" }
    }

    override fun build(): HttpRequestBuilder.() -> Unit = {}

    class FilenameBuilder {
        var year: Int
        var month: Int
        var day: Int
        var hour: Int
        var minute: Int
        var second: Int
        var millisecond: Int

        init {
            val now = LocalDateTime.now()
            year = now.year
            month = now.monthValue
            day = now.dayOfMonth
            hour = now.hour
            minute = now.minute
            second = now.second
            millisecond = 0
        }

        @Suppress("MagicNumber")
        fun build(): LocalDateTime = LocalDateTime.of(year, month, day, hour, minute, second, millisecond * 1000000)
    }
}
