package com.jonh.easyenglish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GrammarView extends AppCompatActivity {

    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_view);
        myWebView = (WebView)findViewById(R.id.grammarViewer);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        //CREAR CARPETA app/src/main/assets
        myWebView.loadUrl("file:///android_asset/test.html");
    }
}
