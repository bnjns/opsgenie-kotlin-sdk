package com.bnjns.integrations.opsgenie.logs

import com.bnjns.integrations.opsgenie.PaginatedItems
import com.bnjns.integrations.opsgenie.doRequest
import com.bnjns.integrations.opsgenie.paginated
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.time.LocalDateTime

/**
 * The Logs API is only available for accounts with an Enterprise licence.
 *
 * @see <https://docs.opsgenie.com/docs/logs-api>
 */
class LogsApi(
    private val httpClient: HttpClient
) {
    private val logFileClient: HttpClient = HttpClient(CIO)

    /**
     * List the log files available for download.
     *
     * Up to 1000 files are returned in a response; use the marker in the response in the next request to fetch the next
     * batch of log files, until the data field is empty.
     *
     * @see <https://docs.opsgenie.com/docs/logs-api#list-log-files>
     */
    suspend fun listLogFiles(block: ListLogFilesRequestBuilder.() -> Unit): PaginatedItems<LogFileDetails> =
        httpClient.paginated(block)

    /**
     * Download a specified log file.
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
     * @see [DownloadLogFileRequestBuilder]
     * @see [API documentation](https://docs.opsgenie.com/docs/logs-api#generate-log-file-download-link)
     * @return the contents of the log file
     */
    suspend fun downloadLogFile(block: DownloadLogFileRequestBuilder.() -> Unit): String = httpClient.doRequest(block)
        .bodyAsText()
        .let { logFileUrl -> logFileClient.get(logFileUrl) }
        .bodyAsText()
}
