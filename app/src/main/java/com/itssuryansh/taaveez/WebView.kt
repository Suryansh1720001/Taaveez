package com.itssuryansh.taaveez

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.itssuryansh.taaveez.databinding.ActivityWebViewBinding

class WebView : AppCompatActivity() {
    private lateinit var webView: WebView
    private var binding: ActivityWebViewBinding? = null
    private var Link: String? = null

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        Link = intent.getStringExtra(Constants.LINK)
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.webView?.let { webViewSetUp(it) }
        binding?.webView?.apply {
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
            Link?.let { loadUrl(it) }
        }
    }

    private fun webViewSetUp(webView: android.webkit.WebView) {
        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: android.webkit.WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding?.progressBar?.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding?.progressBar?.visibility = View.GONE
            }
        }
    }
}
