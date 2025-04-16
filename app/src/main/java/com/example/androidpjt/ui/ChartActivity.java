package com.example.androidpjt.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidpjt.R;
import com.example.androidpjt.databinding.ActivityChartBinding;
import com.example.androidpjt.util.DialogUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class ChartActivity extends AppCompatActivity {
    ActivityChartBinding binding;
    ArrayList<HashMap<String, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChartBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        data = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("chartScore");
        binding.chartTvName.setText(intent.getStringExtra("name"));

        WebSettings settings = binding.chartWb.getSettings();
        settings.setJavaScriptEnabled(true); // 자바스크립트 허용
        settings.setDomStorageEnabled(true); // 로컬 스토리지 허용
        settings.setDatabaseEnabled(true); // 데이터베이스 허용
        settings.setCacheMode(WebSettings.LOAD_DEFAULT); // 캐시 모드 설정
        settings.setAllowFileAccess(true); // 파일 접근 허용
        settings.setAllowContentAccess(true); // 콘텐츠 접근 허용
        settings.setSupportZoom(true); // 줌 지원
        settings.setBuiltInZoomControls(true); // 내장 줌 컨트롤 허용
        settings.setDisplayZoomControls(false); // 줌 컨트롤 표시 안 함

        binding.chartWb.loadUrl("file:///android_asset/test.html");
        binding.chartWb.loadUrl("javascript:lineChart()");
        binding.chartWb.addJavascriptInterface(new JavascriptTest(), "android");
        binding.chartWb.setWebViewClient(new MyWebClient());



    }

    class JavascriptTest {
        @JavascriptInterface
        public String getChartData() {
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            for (int i = 0; i < data.size(); i++) {
                HashMap<String, String > map = data.get(i);
                String scoreStr = map.get("score");
                int score = Integer.parseInt(scoreStr);

                sb.append("[" + i + "," + score + "]");
                if (i < data.size() - 1)
                    sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        }
    }
    //WebView에서 유저 이벤트 핸들러
    //일반적인 이벤트 핸들러는 보통 인터페이스 구현이지만 함수가 너무 많으며
    //인터페이스 구현이면 쓰지도 않을 함수를 오버라이드 받아야 한다 그래서 클래스로 만들고 필요한것만 오버라이드
    class MyWebClient extends WebViewClient {
        //webView에서 유저가 링크를 클릭했을 때 새로운 html을 띄우려고 하는 순간의 이벤트
        //매개변수 정보로 어느 url로 로딩하려고 하는지 알아내고 못가게 하거나 원하는 데이터를 추가해 로딩함
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            DialogUtil.showToast(ChartActivity.this, request.getUrl().toString());
            return true;
        }
    }
}