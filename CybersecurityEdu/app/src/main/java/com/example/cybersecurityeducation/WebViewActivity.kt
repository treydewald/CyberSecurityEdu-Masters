package com.example.cybersecurityeducation

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi

class WebViewActivity : AppCompatActivity() {

    lateinit var webview : WebView
    //val url2 = "https://www.youtube.com/"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val url = intent.getStringExtra("URL_WEB_VIEW").toString()

        webview = findViewById(R.id.webView)
        webViewSetup(url)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun webViewSetup(param : String){
        webview.webViewClient = WebViewClient()
        webview.apply {
            loadUrl(param)
            settings.javaScriptEnabled = true
            settings.builtInZoomControls = true
            settings.safeBrowsingEnabled = true
        }

    }

    override fun onBackPressed() {
        if(webview.canGoBack()){
            webview.goBack()
        }else{
            super.onBackPressed()
        }
    }

}