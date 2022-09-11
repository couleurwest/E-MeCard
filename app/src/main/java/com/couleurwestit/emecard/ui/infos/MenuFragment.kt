package com.couleurwestit.emecard.ui.infos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.couleurwestit.emecard.R
import com.couleurwestit.emecard.data.model.DBUserInfo
import com.couleurwestit.emecard.data.model.UserInfo
import com.couleurwestit.emecard.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private lateinit var ctx: Context
    private lateinit var userInfo: UserInfo
    private lateinit var frgmanager: FragmentManager
    private var _binding: FragmentMenuBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        ctx = this.requireContext()

        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        userInfo = DBUserInfo(ctx, null).findVCard()!!
        frgmanager = this.parentFragmentManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val qrcode_vcard = binding.qrcodeVcard
        val qrcode_uri = binding.qrcodeUri
        val fragment_ecard = binding.fragmentEcard
        val fragment_ending = binding.fragmentEnding

        val loadingProgressBar = binding.loading

        qrcode_vcard.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE

            val fram = frgmanager.beginTransaction()
            fram.replace(R.id.fragment_main, QrcodeFragment("vcard"))
            fram.commit()

        }
        qrcode_uri.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            val fram = frgmanager.beginTransaction()
            fram.replace(R.id.fragment_main, QrcodeFragment("uri"))
            fram.commit()
        }
        fragment_ecard.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            val fram = frgmanager.beginTransaction()
            fram.replace(R.id.fragment_main, UserInfoFragment())
            fram.commit()
        }
        fragment_ending.setOnClickListener {
            this.activity?.finishAfterTransition()
        }
    }


}