package com.couleurwestit.emecard.ui.infos

import com.couleurwestit.emecard.data.model.UserInfo

/**
 * Authentication result : success (user details) or error message.
 */
data class UserInfoResult(
    val success: UserInfo? = null,
    val error: Int? = null
)