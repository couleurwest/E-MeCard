package com.couleurwestit.emecard.ui.infos

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.couleurwestit.emecard.data.UserInfoDataSource
import com.couleurwestit.emecard.data.UserInfoRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class UserInfoViewModelFactory(val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            return UserInfoViewModel(
                userInfoRepository = UserInfoRepository(dataSource = UserInfoDataSource(context))
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}