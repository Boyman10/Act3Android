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
 * Activities that contain this fragment must implement the
 * {@link RssFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Launches the RecyclerView
 * Use the {@link RssFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RssFragment extends Fragment {

    private static final String BUNDLE_RSS_FRG = "RSS_FRAGMENT";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RssFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RssFragment newInstance(String param1, String param2) {
        RssFragment fragment = new RssFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


            /**
             * Enable menu actionbar fragment :
             */
            setHasOptionsMenu(true);
        }
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_task != null)
            _task.cancel(true);
        if (_task1 != null)
            _task1.cancel(true);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void StartAsyncTaskInParallel(XMLAsyncTask task,String ... params) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        else
            task.execute(params);
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
                Log.i(BUNDLE_RSS_FRG,"Sharing something...");
                myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                myIntent.putExtra(Intent.EXTRA_TEXT, MY_URL);

                startActivity(Intent.createChooser(myIntent, "Share now !"));
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
