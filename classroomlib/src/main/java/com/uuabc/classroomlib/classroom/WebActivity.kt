package com.uuabc.classroomlib.classroom

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.uuabc.classroomlib.R
import com.uuabc.classroomlib.common.BaseAdapterScreenActivity
import kotlinx.android.synthetic.main.activity_class_room_webview.*

fun Context.launchUuabcRoomWeb(url: String?, title: String? = null) {
    if (TextUtils.isEmpty(url))
        return
    startActivity(Intent(this, WebActivity::class.java).putExtra("url", url).putExtra("title", title))
}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.initSettings() {
    settings.apply {
        mediaPlaybackRequiresUserGesture = false
        javaScriptEnabled = true
        allowFileAccess = true
        setAppCacheEnabled(true)
        domStorageEnabled = true
        databaseEnabled = true
        savePassword = false
        userAgentString = "$userAgentString; uuabcAndroid"
    }
    webViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return false
        }
    }
}

class WebActivity : BaseAdapterScreenActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        isFullScreen = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_room_webview)

        val url = intent.getStringExtra("url")
        back.setOnClickListener { onBackPressed() }
        val titleString = intent.getStringExtra("title")
        webView.initSettings()
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                tvTitle.text = if (titleString.isNullOrBlank()) title else titleString
            }

            override fun getDefaultVideoPoster(): Bitmap? {
                return try {
                    BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_room_sdk_defaut_card)
                } catch (e: Exception) {
                    super.getDefaultVideoPoster()
                }
            }
        }
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack())
            webView.goBack()
        else
            super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }
}