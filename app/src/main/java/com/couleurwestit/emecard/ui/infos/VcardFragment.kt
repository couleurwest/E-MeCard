package com.couleurwestit.emecard.ui.infos

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.couleurwestit.emecard.R
import com.couleurwestit.emecard.data.model.DBUserInfo
import com.couleurwestit.emecard.data.model.UserInfo
import com.couleurwestit.emecard.databinding.FragmentVcardBinding


class VcardFragment : Fragment() {
    private var userInfo: UserInfo? = null
    private var ctx: Context? = null
    private var _binding: FragmentVcardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ctx = this.requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userInfo = DBUserInfo(ctx!!, null).findVCard()!!
        _binding = FragmentVcardBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("ShowToast", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var phone: Array<String> = arrayOf()

        binding.website.text = userInfo?.website
        binding.email.text = userInfo!!.email
        binding.corp.text = userInfo!!.corp

        if (userInfo!!.phone_1.isNotEmpty()) {
            phone = phone.plus(userInfo!!.phone_1)

        }
        if (userInfo!!.phone_2.isNotEmpty()) {
            phone = phone.plus(userInfo!!.phone_2)

        }
        if (phone.size > 0) {
            binding.phone.text = phone.joinToString("/")
        }


        val display_name = userInfo!!.lastname

        if (userInfo!!.firstname.isNotEmpty()) {
            binding.displayName.text = "${userInfo!!.firstname} ${display_name}"
        }//val loadingProgressBar = binding.loading
        binding.fermer?.setOnClickListener {
            // loadingProgressBar.visibility = View.VISIBLE
            val fram = this.parentFragmentManager.beginTransaction()
            fram.replace(R.id.fragment_main, QrcodeFragment("vcard"))
            fram.commit()

        }


    }
}