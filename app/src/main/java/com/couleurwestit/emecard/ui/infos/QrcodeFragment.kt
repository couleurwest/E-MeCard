package com.couleurwestit.emecard.ui.infos

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.couleurwestit.emecard.R
import com.couleurwestit.emecard.WebsiteActivity
import com.couleurwestit.emecard.data.model.DBUserInfo
import com.couleurwestit.emecard.data.model.UserInfo
import com.couleurwestit.emecard.databinding.FragmentQrcodeuriBinding
import io.github.g0dkar.qrcode.QRCode
import java.io.ByteArrayOutputStream


class QrcodeFragment(val mode: String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var userInfo: UserInfo? = null
    private var ctx: Context? = null
    private var _binding: FragmentQrcodeuriBinding? = null

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
        _binding = FragmentQrcodeuriBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val qrcode = binding.qrcode
        if (mode == "vcard") {
            var dat: Array<String> = arrayOf("EMAIL:${userInfo!!.email}")

            if (userInfo!!.firstname.isNullOrEmpty())
                dat= dat.plus("N:${userInfo?.lastname}")
            else
                dat= dat.plus("N:${userInfo?.lastname},${userInfo?.firstname}")

            if (!userInfo!!.corp.isNullOrEmpty()) {
                dat= dat.plus("NOTE:${userInfo!!.corp}")
            }
            if (!userInfo!!.phone_1.isNullOrEmpty()) {
                dat= dat.plus("TEL:${userInfo!!.phone_1}")
            }
            if (!userInfo!!.phone_2.isNullOrEmpty()) {
                dat= dat.plus("TEL:${userInfo!!.phone_2}")
            }
            if (!userInfo!!.website.isNullOrEmpty()) {
                dat= dat.plus("URL:${userInfo!!.website}")
            }
            val msg = "MECARD:" + dat.joinToString(";")

            val imageOut = ByteArrayOutputStream()
            QRCode(msg).render(cellSize = 40, margin = 25).writeImage(imageOut)
            val imageBytes = imageOut.toByteArray()
            val image: Drawable = BitmapDrawable(
                resources,
                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            )
            qrcode.setImageDrawable(image)

        } else if (mode == "uri") {
            if (!userInfo!!.website.isNullOrEmpty()) {
                val imageOut = ByteArrayOutputStream()
                QRCode(userInfo!!.website).render(cellSize = 40, margin = 25).writeImage(imageOut)
                val imageBytes = imageOut.toByteArray()
                val image: Drawable = BitmapDrawable(
                    resources,
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                )
                qrcode.setImageDrawable(image)
            } else {
                val fram = this.parentFragmentManager.beginTransaction()
                fram.replace(R.id.fragment_main, MenuFragment())
                fram.commit()
                Toast.makeText(context, "Vous n'avez pas saisie de site web", Toast.LENGTH_LONG)
                    .show()
            }


        }
        //val loadingProgressBar = binding.loading
        qrcode.setOnClickListener {
            val fram = this.parentFragmentManager.beginTransaction()
            if (mode == "vcard") {
                fram.replace(R.id.fragment_main, VcardFragment())
            } else if (mode == "uri") {
                val intent = Intent(ctx, WebsiteActivity::class.java)
                startActivity(intent)
            } else
                fram.replace(R.id.fragment_main, MenuFragment())
            fram.commit()

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment QrcodeFragment.
         */
        private const val ARG_MODE = "vcard"

        @JvmStatic
        fun newInstance(param1: String) =
            QrcodeFragment(param1).apply {
                arguments = Bundle().apply {
                    putString(ARG_MODE, param1)
                }
            }
    }
}