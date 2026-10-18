package com.lovinpdf.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : Activity() {
    private lateinit var root: LinearLayout
    private var webView: WebView? = null
    private var adView: AdView? = null
    private val siteUrl = "https://lovin-pdf.vercel.app/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        showHome()
    }

    private fun showHome() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(16, 24, 39))
            setPadding(dp(22), dp(20), dp(22), dp(12))
        }
        root.addView(TextView(this).apply {
            text = "Lovin-PDF"
            textSize = 30f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, dp(12), 0, dp(8))
        }, matchWrap())
        root.addView(TextView(this).apply {
            text = "Free PDF tools. Right on your phone."
            textSize = 15f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, dp(18))
        }, matchWrap())

        listOf("Merge PDF", "Split PDF", "Rotate PDF", "JPG to PDF",
            "Page Numbers", "Watermark PDF", "Compress PDF").forEach { addToolButton(it) }

        root.addView(TextView(this).apply {
            text = "Tools load from Lovin-PDF online."
            textSize = 12f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, dp(8), 0, dp(8))
        }, matchWrap())

        val banner = AdView(this).apply {
            adUnitId = "ca-app-pub-7792140336662200/6111181924"
            setAdSize(AdSize.BANNER)
        }
        adView = banner
        root.addView(banner, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { gravity = Gravity.CENTER })
        banner.loadAd(AdRequest.Builder().build())
        setContentView(root)
    }

    private fun addToolButton(label: String) {
        val button = Button(this).apply {
            text = label
            textSize = 15f
            isAllCaps = false
            setTextColor(Color.WHITE)
            backgroundTintList = android.content.res.ColorStateList.valueOf(Color.rgb(82, 67, 180))
            setOnClickListener { openWebsite() }
        }
        root.addView(button, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(48)
        ).apply { bottomMargin = dp(6) })
    }

    private fun openWebsite() {
        val wv = WebView(this)
        webView = wv
        wv.settings.javaScriptEnabled = true
        wv.settings.domStorageEnabled = true
        wv.settings.allowFileAccess = false
        wv.settings.allowContentAccess = false
        wv.webChromeClient = WebChromeClient()
        wv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                val host = uri.host ?: return true
                val allowed = host == "lovin-pdf.vercel.app" ||
                    host == "bauval.org" ||
                    host.endsWith("jsdelivr.net") ||
                    host.endsWith("cloudflare.com")
                return !(uri.scheme == "https" && allowed)
            }
        }
        wv.loadUrl(siteUrl)
        setContentView(wv)
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
    }

    override fun onPause() {
        adView?.pause()
        super.onPause()
    }

    override fun onDestroy() {
        adView?.destroy()
        adView = null
        super.onDestroy()
    }

    @Deprecated("Deprecated in Android API")
    override fun onBackPressed() {
        val wv = webView
        if (wv != null && wv.canGoBack()) wv.goBack() else showHome()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
    private fun matchWrap() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )
}
