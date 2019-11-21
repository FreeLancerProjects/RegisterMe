package com.creativeshare.registerme.activities_fragments.activities.activity_video;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


import com.creativeshare.registerme.R;
import com.creativeshare.registerme.language.Language_Helper;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.tags.Tags;
import com.hbb20.CountryCodePicker;

import java.util.Locale;

import io.paperdb.Paper;

public class Detials_Activity extends AppCompatActivity {
    private String videoPath = "";
    private String lang;
    private ImageView im_back;
private WebView webView;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detials);
        initView();
    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        Paper.init(this);
        im_back=findViewById(R.id.arrow);
        webView=findViewById(R.id.webView);
        if(lang.equals("en")){
            im_back.setRotation(180.0f);
        }
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
finish();            }
        });
        videoPath = getIntent().getStringExtra("data");
        setUpWebView();

    }


    private void setUpWebView() {
     webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setPluginState(WebSettings.PluginState.ON);
  webView.getSettings().setBuiltInZoomControls(false);
 webView.loadUrl(Tags.IMAGE_URL+videoPath);
       webView.setWebViewClient(new WebViewClient() {
                                             @Override
                                             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                                 super.onPageStarted(view, url, favicon);
                                             }

                                             @Override
                                             public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                                 super.onReceivedError(view, request, error);
                                               webView.setVisibility(View.INVISIBLE);
                                             }

                                             @Override
                                             public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                                                 super.onReceivedHttpError(view, request, errorResponse);
                                               webView.setVisibility(View.INVISIBLE);
                                             }
                                         }

        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
     webView.onResume();
    }
}
