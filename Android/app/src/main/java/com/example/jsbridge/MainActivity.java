package com.example.jsbridge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText editText;
    private Button showBtn;
    private Button refreshBtn;
    private MainActivity self = this;
    // H5 页面的地址
    private String webHost = "http://192.168.147.241:8080";
    private String webLink = webHost + "?timestamp=" + new Date().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        editText = findViewById(R.id.editText);
        showBtn = findViewById(R.id.showBtn);
        refreshBtn = findViewById(R.id.refreshBtn);

        webView.loadUrl(webLink);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (!message.startsWith("jsbridge://")) {
                    return super.onJsAlert(view, url, message, result);
                }

                String text = message.substring(message.indexOf("=") + 1);
                self.showNativeDialog(text);

                result.confirm();
                return true;
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputValue = editText.getText().toString();
                self.showWebDialog(inputValue);
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(webLink);
            }
        });
    }

    private void showWebDialog(String text) {
        String jsCode = String.format("window.showWebDialog('%s')", text);
        webView.evaluateJavascript(jsCode, null);
    }

    private void showNativeDialog(String text) {
        new AlertDialog.Builder(this).setMessage(text).create().show();
    }
}
