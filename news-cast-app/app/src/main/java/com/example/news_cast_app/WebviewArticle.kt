package com.example.news_cast_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible

class WebviewArticle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_article)
        val progressBar = findViewById<ProgressBar>(R.id.prgB)
        progressBar.isVisible = true
        val articleWebView = findViewById(R.id.webview) as WebView
        val strs = intent.getStringExtra("url").toString().split("//").toTypedArray()

        Log.d("url", strs[1].toString())

        articleWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, weburl: String) {
                Log.d("webview", "done loading")
                progressBar.isVisible = false
            }
        }

        articleWebView.loadUrl("https://"+strs[1].toString())
    }
}