package com.couleurwestit.emecard.data

import com.couleurwestit.emecard.data.model.DBUserInfo
import com.couleurwestit.emecard.data.model.UserInfo
import java.io.IOException


import android.content.Context

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class UserInfoDataSource(var context: Context) {

    fun loadVCard(): Result<UserInfo> {
        try {
            // TODO: handle loggedInUser authentication
            val user = DBUserInfo(this.context, null).findVCard()
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun recordVCard( firstname: String, lastname: String, corp: String,  email: String, website: String, phone_1: String, phone_2: String): Result<UserInfo> {
        try {
            // TODO: handle loggedInUser authentication
            val user = DBUserInfo(this.context, null).record(lastname, firstname, corp, email, website, phone_1, phone_2)
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

}