package com.example.news_cast_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible

/* Activity to display articles in a webview */
class WebviewArticle : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_article)
        /* Creating a progress bar so that the user can see the resource is loading in a browser */
        val progressBar = findViewById<ProgressBar>(R.id.prgB)
        progressBar.isVisible = true

        val articleWebView = findViewById(R.id.webview) as WebView
        /* Splitting url to handle urls which do not have an https format */
        val strs = intent.getStringExtra("url").toString().split("//").toTypedArray()

        /*Creating the web client */
        articleWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, weburl: String) {
                Log.d("webview", "done loading")
                progressBar.isVisible = false
            }
        }
        /* Loading an https resource */
        articleWebView.loadUrl("https://"+strs[1].toString())
    }
}