package com.ocr.test.act3android.controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.ocr.test.act3android.R;


/**
 * A fragment to display the content of URL in a webview - being called by RSS Fragment
 * A simple {@link Fragment} subclass.
 */
public class ViewFragment extends Fragment {

    private static final String BUNDLE_WEB_FRG = "WEB_FRAGMENT";

    // Define url to be opened in webview
    private String mUrl = "https://r-h-m.net";


    public ViewFragment() {
        // Required empty public constructor
    }

    /**
     * Static create method to return a ViewFragment passing a link into it
     * @param link
     * @return {@link ViewFragment}
     */
    public static ViewFragment create(String title,String link) {

        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("link",link);
        ViewFragment vF = new ViewFragment();
        vF.setArguments(args);

        return vF;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // being called after onAttach activity and before onCreateview
        super.onCreate(savedInstanceState);

        /**
         * Enable menu actionbar fragment :
         */
        setHasOptionsMenu(true);

        Log.i(BUNDLE_WEB_FRG,"onCreate Fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view, container, false);
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {

        super.onViewCreated(view,savedInstanceState);

        Log.i(BUNDLE_WEB_FRG,"Calling onViewCreated - WEB View ");
        WebView webView = (WebView) view.findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);

        // Passing URL instead :
        webView.loadUrl(getArguments().getString("link"));

        // Allow clicking on links and opening these links right into the webview :
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        // set current title in actionbar
        getActivity().setTitle(getArguments().getString("title"));

        Log.i(BUNDLE_WEB_FRG,"End of onViewCreated - WEB View ");

    }

    /**
     * Overriding onCreateOptionsMenu to allow the use of a menu shared among interfaces (tab or mobile...)
     * To be used in fragments
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu,menu);
    }

    /**
     * Here wee implement the event to track the click on the menu
     * Works similarly as activities
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;

        switch(item.getItemId()) {

            case R.id.action_share :
                    /*DO SHARE*/
                Log.i(BUNDLE_WEB_FRG,"Sharing something..." + getArguments().getString("link"));
                myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                myIntent.putExtra(Intent.EXTRA_TEXT, getArguments().getString("link"));

                startActivity(Intent.createChooser(myIntent, "Share now !"));
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
