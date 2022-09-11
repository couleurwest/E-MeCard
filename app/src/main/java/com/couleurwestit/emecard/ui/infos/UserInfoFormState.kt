package com.couleurwestit.emecard.ui.infos

/**
 * Data validation state of the login form.
 */
data class UserInfoFormState(
    val dnError: Int? = null,
    val lastameError: Int? = null,
    val emailError: Int? = null,
    val websiteError: Int? = null,
    val phone1Error: Int? = null,
    val phone2Error: Int? = null,
    val isDataValid: Boolean = false
)