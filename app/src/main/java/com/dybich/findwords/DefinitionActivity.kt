package com.dybich.findwords

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.material.progressindicator.LinearProgressIndicator

class DefinitionActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.definition_layout)

        val word : String = intent.getStringExtra("WORD").toString()


        webView = findViewById(R.id.definition_WV)
        webView.settings.javaScriptEnabled = true

        webView.loadUrl("https://sjp.pl/${word}")
    }

}