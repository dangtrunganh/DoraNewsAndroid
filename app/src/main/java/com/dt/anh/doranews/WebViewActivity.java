package com.dt.anh.doranews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.dt.anh.doranews.fragment.DetailNewsInVpFragment;
import com.dt.anh.doranews.util.ConstParamTransfer;

import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initViews();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initViews() {
        webView = findViewById(R.id.web_view_wv);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        toolbar = findViewById(R.id.toolbar_news_wv);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadLink();
    }

    private void loadLink() {
        String urll = "https://baomoi.com/thua-dau-indonesia-hlv-u22-viet-nam-chi-trich-trong-tai/c/29768439.epi";
        Intent intent = getIntent();
        if (intent != null) {
            urll = intent.getStringExtra(DetailNewsInVpFragment.PARAM_URL_NEWS);
            urll = urll.replace("/r/", "/c/");
            if (urll == null) {
                urll = "https://baomoi.com/thua-dau-indonesia-hlv-u22-viet-nam-chi-trich-trong-tai/c/29768439.epi";
            }
        } else {
            Toast.makeText(this, "Error, nothing in intent url news", Toast.LENGTH_SHORT).show();
            return;
        }
        webView.loadUrl(urll);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
