package com.bnjns.integrations.opsgenie.account

import com.bnjns.integrations.opsgenie.standard
import io.ktor.client.*

/**
 * @see <https://docs.opsgenie.com/docs/account-api>
 */
class AccountApi(
    private val httpClient: HttpClient
) {
    /**
     * Fetches the information about the account, including the plan/licence, that the authenticated user is a member
     * of.
     *
     * @see <https://docs.opsgenie.com/docs/account-api#get-account-info>
     */
    suspend fun getAccountInfo(): AccountInfoResponse =
        httpClient.standard<GetAccountInfoRequestBuilder, AccountInfoResponse>(block = {})
}

data class AccountInfoResponse(
    val name: String,
    val userCount: Long,
    val plan: AccountPlan
)

data class AccountPlan(
    val name: String,
    val maxUserCount: Long,
    val isYearly: Boolean
)
