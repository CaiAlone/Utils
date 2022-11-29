package com.cj.splicing.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cj.splicing.R;
import com.cj.splicing.utils.collector.ActivityCollector;
import com.gyf.barlibrary.ImmersionBar;

import java.io.BufferedInputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CaiJing on 2022/1/6
 */

public class WebViewActivity extends AppCompatActivity {
    protected ImmersionBar mImmersionBar;
    private WebView web;
    private static final String APP_CACHE_DIRNAME = "/webcache"; // web缓存目录
    @BindView(R.id.iv_quit)
    ImageView iv_quit;
    @BindView(R.id.tv_title)
    TextView tv_title;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        if (mImmersionBar != null) {
            mImmersionBar.destroy();  //销毁
        }
    }

    /**
     * 初始化
     */
    private void init(){
        /**
         *   须沉浸式时，若状态栏背景为标题栏相同颜色，
         *   使用标题栏paddingTop 25 达到效果
         */
        if (isImmerseBar()) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar.init();
        }
        darkImmerseFontColor();

        if(getIntent().hasExtra("title")){
            tv_title.setText(getIntent().getStringExtra("title"));
        }

        iv_quit.setOnClickListener(v->{
            finish();
        });
    }

    //打开 关闭沉浸式的方法  默认打开
    public boolean isImmerseBar() {
        return true;
    }

    //状态栏字体颜色为暗灰色
    public void darkImmerseFontColor() {
        if (mImmersionBar != null) {
            mImmersionBar.statusBarDarkFont(true, 0.2f).init();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        init();
        web=findViewById(R.id.WebView);
        Intent intent=getIntent();
        if(intent!=null) {
            web.loadUrl(intent.getStringExtra("url"));
        }
        initViews();
    }


    protected void initViews() {
        String url=null;
        if(getIntent().hasExtra("user_protocol")){
            web.loadDataWithBaseURL(null,getIntent().getStringExtra("user_protocol"), "text/html" , "utf-8", null);//加载html数据
        }else  if(getIntent().hasExtra("privacy")){
            web.loadDataWithBaseURL(null,getIntent().getStringExtra("privacy"), "text/html" , "utf-8", null);//加载html数据
        }
        else {
            web.getSettings().setDefaultTextEncodingName("utf-8");
            url = getIntent().getStringExtra("url");
            web.loadUrl(url);
        }
        web.clearCache(true);
        web.clearHistory();
        web.getSettings().setSavePassword(false);
        //设置缓存模式
        web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        web.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        web.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
        // 设置数据库缓存路径
        web.getSettings().setAppCachePath(cacheDirPath);
        web.getSettings().setAppCacheEnabled(true);
        web.removeJavascriptInterface("searchBoxjavaBridge_");//解决 CVE-2014-1939 漏洞
        web.removeJavascriptInterface("accessibility");//解决  CVE-2014-7224漏洞
        web.removeJavascriptInterface("accessibilityTraversal");//解决  CVE-2014-7224漏洞
        WebSettings setting = web.getSettings();
        //settings.setLoadWithOverviewMode(true);
        // 禁用 file 协议；
        if (url==null||url.startsWith("file://")) {
            setting.setJavaScriptEnabled(false);
        } else {
            setting.setJavaScriptEnabled(true);
        }
        setting.setAllowFileAccess(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setting.setAllowFileAccessFromFileURLs(false);
            setting.setAllowUniversalAccessFromFileURLs(false);
        }
        setting.setDomStorageEnabled(true);
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            setting.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

        }
        setting.setDatabaseEnabled(true);

        setting.setAllowContentAccess(false);//是否允许在WebView中访问内容URL（Content Url），默认允许。
        setting.setGeolocationEnabled(false);//定位是否可用，默认为true

        String dir = getDir("database", Context.MODE_PRIVATE).getPath();
        setting.setDatabasePath(dir);
        setting.setGeolocationDatabasePath(dir);

        setting.setAppCacheEnabled(true);
        String cacheDir = getDir("cache", Context.MODE_PRIVATE).getPath();
        setting.setAppCachePath(cacheDir);
        setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        setting.setAppCacheMaxSize(1024 * 1024 * 10);

        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setTextZoom(150);
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);
        setting.setDisplayZoomControls(false);
        web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        web.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        web.setBackgroundResource(R.color.white);
        WebChromeClient webChromeClient = new WebChromeClient() {
            boolean changeLayoutParam = false;

            @Override
            public void onCloseWindow(WebView window) {
                //TODO something
                super.onCloseWindow(window);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog,
                                          boolean isUserGesture, Message resultMsg) {
                //TODO something
                WebView newWebView = new WebView(view.getContext());
                newWebView.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Uri uri = Uri.parse(url);
                        uri.getQueryParameter("price"); //获取数据
                        return true;
                    }
                });
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
            }

            public void onHideCustomView() {

            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 90 && !changeLayoutParam) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) web.getLayoutParams();
                    params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                    web.setLayoutParams(params);
                    changeLayoutParam = true;
                }
            }
        };
        web.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        web.setWebChromeClient(webChromeClient);
        web.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(
                    WebView view, WebResourceRequest request) {
                if (!request.isForMainFrame() && request.getUrl().getPath().endsWith("/favicon.ico")) {
                    try {
                        return new WebResourceResponse("image/png",
                                null, new BufferedInputStream(view.getContext().getAssets().open("empty_favicon.ico")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url
            ) {
                if (url.toLowerCase().contains("/favicon.ico")) {
                    try {
                        return new WebResourceResponse("image/png",
                                null, new BufferedInputStream(view.getContext().getAssets().open("empty_favicon.ico")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (url.contains("[tag]")) {
                    String localPath = url.replaceFirst("^http.*[tag]\\]", "");
                    try {
                        InputStream is = getApplicationContext().getAssets().open(localPath);
                        String mimeType = "text/javascript";
                        if (localPath.endsWith("css")) {
                            mimeType = "text/css";
                        }
                        return new WebResourceResponse(mimeType, "UTF-8", is);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    return null;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (web != null) {
                    web.getSettings().setLoadsImagesAutomatically(true);

                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("rechage")){
                    //startActivity(new Intent(WebViewActivity.this,Top_Uping_Activity.class).putExtra("mic","mic"));
                    finish();
                }
                else {
                    view.loadUrl(url);
                }

                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                web.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                if (!request.isForMainFrame() && request.getUrl().getPath().endsWith("/favicon.ico")) {
                } else { // TODO: 具体可根据返回状态码做相应处理
//                    web.loadUrl("file:///android_asset/load_fail.html");
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                web.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                web.setVisibility(View.INVISIBLE);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }
        });
        //   web.getSettings().setLoadsImagesAutomatically(false);
        if (Build.VERSION.SDK_INT >= 19) {
            web.getSettings().setLoadsImagesAutomatically(true);
        } else {
            web.getSettings().setLoadsImagesAutomatically(false);
        }
    }

    @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
            web.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}