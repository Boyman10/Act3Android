package com.ocr.test.act3android.controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ocr.test.act3android.R;
import com.ocr.test.act3android.model.RSSAdapter;
import com.ocr.test.act3android.model.XMLAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 * Launches the RecyclerView
 */
public class RssFragment extends Fragment {

    private static final String BUNDLE_RSS_FRG = "RSS_FRAGMENT";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * The RecyclerView and its adapter and layout manager
     */
    private RecyclerView mRecyclerView;
    private RSSAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private XMLAsyncTask _task = null,_task1 = null;


    /**
     * Empty class constructor
     */
    public RssFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(BUNDLE_RSS_FRG,"Calling onCreate ---");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(BUNDLE_RSS_FRG,"Calling onCreateView -inflate frgament_rss layout-");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rss, container, false);
    }


    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {

        super.onViewCreated(view,savedInstanceState);

        Log.i(BUNDLE_RSS_FRG,"Calling onViewCreated - REcycler View ");

        mRecyclerView = view.findViewById(R.id.mRecycler);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RSSAdapter((RSSAdapter.URLLoader) getActivity());

        // set default position of recyclerView
        mRecyclerView.setAdapter(mAdapter);

        // ASYNCTASKS NOT PARALLEL HERE
        Log.i(BUNDLE_RSS_FRG,"Launching Async task...");
        _task = new XMLAsyncTask(mAdapter,6);
        Log.i(BUNDLE_RSS_FRG,"task fetching url 1 executed");
        StartAsyncTaskInParallel(_task,"http://www.lemonde.fr/rss/une.xml");

        // Other urls :
        //https://www.melty.fr/actu.rss
        _task1 = new XMLAsyncTask(mAdapter,1);
        Log.i(BUNDLE_RSS_FRG,"task fetching url 2 executed");
        StartAsyncTaskInParallel(_task1,"https://www.melty.fr/actu.rss");


        // adding progress bar callback function to get notified when list fully loaded :
        final ProgressBar progress = (ProgressBar) view.findViewById(R.id.progress);
        // we Observe from the adapter :
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                // we remove the progress bar !
                progress.setVisibility(View.GONE);
            }
        });
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_task != null)
            _task.cancel(true);
        if (_task1 != null)
            _task1.cancel(true);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void StartAsyncTaskInParallel(XMLAsyncTask task,String ... params) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        else
            task.execute(params);
    }



}
