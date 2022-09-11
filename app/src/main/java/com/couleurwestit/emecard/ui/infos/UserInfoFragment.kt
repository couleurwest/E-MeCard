package com.couleurwestit.emecard.ui.infos

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.couleurwestit.emecard.R
import com.couleurwestit.emecard.data.model.DBUserInfo
import com.couleurwestit.emecard.data.model.UserInfo
import com.couleurwestit.emecard.databinding.FragmentUserinfoBinding
import java.util.*


class UserInfoFragment : Fragment() {

    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var ctx: Context
    private var _binding: FragmentUserinfoBinding? = null
    private lateinit var frgmanager: FragmentManager

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ctx = this.requireContext()
        _binding = FragmentUserinfoBinding.inflate(inflater, container, false)

        frgmanager = this.parentFragmentManager

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userInfo = DBUserInfo(ctx, null).findVCard()

        userInfoViewModel = ViewModelProvider(this, UserInfoViewModelFactory(ctx))
            .get(UserInfoViewModel::class.java)

        val firstnameEditText = binding.firstname
        val lastnameEditText = binding.lastname
        val corpEditText = binding.corp
        val emailEditText = binding.email
        val phone1EditText = binding.phone1
        val phone2EditText = binding.phone2
        val websiteEditText = binding.website

        val recordButton = binding.record

        val loadingProgressBar = binding.loading
        if (userInfo != null) {
            firstnameEditText.setText(userInfo.firstname)
            lastnameEditText.setText(userInfo.lastname)
            corpEditText.setText(userInfo.corp)
            emailEditText.setText(userInfo.email)
            phone1EditText.setText(userInfo.phone_1)
            phone2EditText.setText(userInfo.phone_2)
            websiteEditText.setText(userInfo.website)

        }

        recordButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            var firstname = firstnameEditText.text.toString()
            firstname = firstname.replace("\\s\\s*", "-").trim().lowercase()
            firstname = firstname.replaceFirstChar { it.titlecase(Locale.getDefault()) }
            val lastname = lastnameEditText.text.toString()
            val corp = corpEditText.text.toString()
            val email = emailEditText.text.toString()
            var website = websiteEditText.text.toString()
            if (!website.startsWith("http"))
                website = "https://${website}"

            val phone1 = phone1EditText.text.toString()
            val phone2 = phone2EditText.text.toString()

            userInfoViewModel.record(
                firstname,
                lastname.replace("\\s\\s*", "-").trim().uppercase(),
                corp.replace("\\s\\s+", " ").trim().uppercase(),
                email.replace("\\s", "").lowercase(),
                website.replace("\\s", "").lowercase(),
                phone1.replace("\\s\\s+", " ").trim(),
                phone2.replace("\\s\\s+", " ").trim()
            )

            val fram = frgmanager.beginTransaction()
            fram.replace(R.id.fragment_main, MenuFragment())
            fram.commit()
        }

        userInfoViewModel.userInfoFormState.observe(viewLifecycleOwner,
            Observer { cardInfoFormState ->
                if (cardInfoFormState == null) {
                    return@Observer
                }
                recordButton.isEnabled = cardInfoFormState.isDataValid
                cardInfoFormState.emailError?.let {
                    emailEditText.error = getString(it)
                }
                cardInfoFormState.websiteError?.let {
                    websiteEditText.error = getString(it)
                }
                cardInfoFormState.dnError?.let {
                    firstnameEditText.error = getString(it)
                    lastnameEditText.error = getString(it)
                }
                cardInfoFormState.phone1Error?.let {
                    phone1EditText.error = getString(it)
                }
                cardInfoFormState.phone2Error?.let {
                    phone2EditText.error = getString(it)
                }
            })

        userInfoViewModel.carUserResult.observe(viewLifecycleOwner,
            Observer { carUserResult ->
                carUserResult ?: return@Observer

                loadingProgressBar.visibility = View.GONE

                carUserResult.error?.let {
                    showLoginFailed(it)
                }
                carUserResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                userInfoViewModel.UserInfoValidation(
                    firstnameEditText.text.toString(),
                    lastnameEditText.text.toString(),
                    emailEditText.text.toString(),
                    websiteEditText.text.toString(),
                    phone1EditText.text.toString(),
                    phone2EditText.text.toString()
                )
            }
        }
        firstnameEditText.addTextChangedListener(afterTextChangedListener)
        lastnameEditText.addTextChangedListener(afterTextChangedListener)
        corpEditText.addTextChangedListener(afterTextChangedListener)
        emailEditText.addTextChangedListener(afterTextChangedListener)
        websiteEditText.addTextChangedListener(afterTextChangedListener)
        phone1EditText.addTextChangedListener(afterTextChangedListener)
        phone2EditText.addTextChangedListener(afterTextChangedListener)

    }

    private fun updateUiWithUser(model: UserInfo) {
        val welcome = getString(R.string.welcome) + model.lastname
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}