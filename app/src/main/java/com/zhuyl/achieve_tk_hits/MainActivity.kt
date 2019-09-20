package com.zhuyl.achieve_tk_hits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.webkit.WebView
import android.webkit.WebViewClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton.setOnClickListener {
            if (addressEditText.text.isNullOrBlank()) {
                Toast.makeText(this, "请输入地址", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (timesEditText.text.isNullOrBlank()) {
                Toast.makeText(this, "请输入次数", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Observable.interval(1, SECONDS)
                .takeUntil { second -> second > timesEditText.text.toString().toLong() }
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    numberText.text = "已完成 $it 次"
                    loadWebView()
                }, {
                    Toast.makeText(this, "错误", Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun loadWebView() {
        previewWebView.loadUrl(addressEditText.text.toString())
        val webSettings = previewWebView.settings
        webSettings.javaScriptEnabled = true
        previewWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
    }
}
