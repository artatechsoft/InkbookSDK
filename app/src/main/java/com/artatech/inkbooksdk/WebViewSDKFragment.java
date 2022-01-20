package com.artatech.inkbooksdk;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Goltstein on 08/12/2017.
 */

public class WebViewSDKFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    @BindView(R.id.webview)
    WebView mWebview;

    @BindView(R.id.imbtn_refresh)
    ImageButton mRefresh;
    @BindView(R.id.ed_url)
    EditText mUrlEdit;


    public WebViewSDKFragment() {
    }

    public static WebViewSDKFragment newInstance(int sectionNumber) {
        WebViewSDKFragment fragment = new WebViewSDKFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.webviewsdk_fragment, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setUseWideViewPort(false);
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mRefresh.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_close));
                mUrlEdit.setText(url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mRefresh.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_refresh_black_24dp));
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                mRefresh.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_refresh_black_24dp));
                super.onReceivedError(view, request, error);
            }
        });

        mUrlEdit.setText("https://www.artatech.pl/");
        onSearch();
    }

    // PRIME
    // CHROMIUM   USER AGENT: Mozilla/5.0 (Linux;    Android 4.2.2;        Prime    Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Safari/537.36
    //CLASSIC_2
    // CHROMIUM   USER AGENT: Mozilla/5.0 (Linux;    Android 4.2.2;        Classic2 Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Safari/537.36
    //CLASSIC_2
    // DEFAULT    USER AGENT: Mozilla/5.0 (Linux; U; Android 4.2.2; pl-pl; Classic2 Build/JDQ39) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30




    @OnClick(R.id.imbtn_search)
    public void onSearch() {
        mWebview.loadUrl(mUrlEdit.getText().toString());
    }

    @OnClick(R.id.imbtn_refresh)
    public void onRefresh_Stop() {
        if (mRefresh.getDrawable() == getActivity().getResources().getDrawable(R.drawable.ic_close)) {
            mWebview.stopLoading();
        } else {
            mWebview.reload();
        }
    }
}
