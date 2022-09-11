package com.couleurwestit.emecard

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.couleurwestit.emecard.data.model.DBUserInfo

class WebsiteActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website)


        val userInfo = DBUserInfo(this.applicationContext, null).findVCard()

        if ((userInfo == null)  || userInfo.website.isEmpty()) {
                this.finishAffinity()
                Toast.makeText(this.applicationContext, "Pas de site web", Toast.LENGTH_LONG).show()
        }else{
            val url = userInfo.website
            this.title =if (userInfo.corp.isEmpty()) userInfo.website else userInfo.corp

            val webView = this.findViewById<WebView>(R.id.website)

            webView.settings.javaScriptEnabled = true

            webView.webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null) {
                        view?.loadUrl(url)
                    }
                    return true
                }
            }
            webView.loadUrl(url)
        }
    }
}