package com.couleurwestit.emecard.ui.infos

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.couleurwestit.emecard.R
import com.couleurwestit.emecard.data.Result
import com.couleurwestit.emecard.data.UserInfoRepository

class UserInfoViewModel(private val userInfoRepository: UserInfoRepository) : ViewModel() {

    private val _userInfoForm = MutableLiveData<UserInfoFormState>()
    val userInfoFormState: LiveData<UserInfoFormState> = _userInfoForm

    private val _userInfoResult = MutableLiveData<UserInfoResult>()
    val carUserResult: LiveData<UserInfoResult> = _userInfoResult

    fun record(
        firstname: String,
        lastname: String,
        corp: String,
        email: String,
        website: String,
        phone1: String,
        phone2: String
    ) {
        val result =
            userInfoRepository.record(firstname, lastname, corp, email, website, phone1, phone2)

        if (result is Result.Success) {
            _userInfoResult.value = UserInfoResult(success = result.data)
        } else {
            _userInfoResult.value = UserInfoResult(error = R.string.no_card)
        }
    }

    fun UserInfoValidation(
        firstname: String,
        lastname: String,
        email: String,
        website: String,
        phone1: String,
        phone2: String
    ) {
        if (isEmpty(firstname) and isEmpty(lastname)) {
            _userInfoForm.value = UserInfoFormState(dnError = R.string.invalid_dn)
        } else if (!isEmailValid(email)) {
            _userInfoForm.value = UserInfoFormState(emailError = R.string.invalid_email)
        } else if (!isPhoneValid(phone1)) {
            _userInfoForm.value = UserInfoFormState(phone1Error = R.string.invalid_phone)
        } else if (!isPhoneValid(phone2)) {
            _userInfoForm.value = UserInfoFormState(phone2Error = R.string.invalid_phone)
        } else if (!isURLValid(website)) {
            _userInfoForm.value = UserInfoFormState(websiteError = R.string.invalid_url)
        } else {
            _userInfoForm.value = UserInfoFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isEmailValid(ch: String): Boolean {
        val vl = ch.replace("\\s", "").trim()
        return if (vl.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(vl).matches()
        } else {
            return false
        }
    }

    private fun isURLValid(ch: String): Boolean {
        val vl = ch.replace("\\s", "").trim()
        return if (vl.isNotEmpty()) Patterns.WEB_URL.matcher(vl).matches() else true
    }

    // A placeholder username validation check
    private fun isEmpty(ch: String): Boolean {
        val vl = ch.replace("\\s\\s+", ("")).trim()
        return vl.isEmpty()
    }

    // A placeholder password validation check
    private fun isPhoneValid(ch: String): Boolean {
        val vl = ch.trim().replace("\\s\\s+", "")
        return if (vl.isNotEmpty()) Patterns.PHONE.matcher(vl).matches() else true
    }
}