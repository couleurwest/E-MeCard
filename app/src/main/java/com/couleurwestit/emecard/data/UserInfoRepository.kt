package com.couleurwestit.emecard.data

import com.couleurwestit.emecard.data.model.UserInfo

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class UserInfoRepository(var dataSource: UserInfoDataSource) {
    // in-memory cache of the loggedInUser object
    var user: UserInfo? = null
        private set
    val setted: Boolean
        get() = user != null

    init {
        val result = this.dataSource.loadVCard()
        if (result is Result.Success) {
            result.data?.let { setCardUser(it) }
        }
    }

    fun record(
        firstname: String,
        lastname: String,
        corp: String,
        email: String,
        website: String,
        phone1: String,
        phone2: String
    ): Result<UserInfo> {
        // handle login
        val result =
            this.dataSource.recordVCard(firstname, lastname, corp, email, website, phone1, phone2)
        if (result is Result.Success) {
            result.data?.let { setCardUser(it) }
        }

        return result
    }

    private fun setCardUser(userInfo: UserInfo) {
        this.user = userInfo

    }
}