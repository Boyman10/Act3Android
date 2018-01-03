package com.ocr.test.act3android;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ocr.test.act3android.controller.RssFragment;
import com.ocr.test.act3android.controller.ViewFragment;
import com.ocr.test.act3android.model.RSSAdapter;

/**
 * MainActivity launching RSS feeds from several sources concurrently
 * and allowing the user to share some content. Using fragments
 * @author boy
 * @version 1.0.0
 */
public class MainActivity extends AppCompatActivity implements RssFragment.OnFragmentInteractionListener,RSSAdapter.URLLoader {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setTitle("Welcome here dude");

        // start the RSS fragment :
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.rssFrg, new RssFragment())
                .commit();
    }

    /**
     * Method from RssFragment to listen to fragment and pass interaction to activity
     * @param uri
     */
    @Override
    public void onFragmentInteraction(Uri uri) {


    }


    /**
     * Loading the content into the webview
     * inside the dewdicated fragment if exists or the other activity
     * @param title
     * @param link
     */
    @Override
    public void load(String title, String link){

        // Check whether fragment exists or not
        if(findViewById(R.id.viewFrg) != null) {

            ViewFragment frg = ViewFragment.create(title,link);

            // start the View fragment :
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.viewFrg, frg)
                    .addToBackStack(null)
                    .commit();

        } else {

            // Now use activity to display the webview
            Intent intent = new Intent(this,ViewActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("link",link);
            startActivity(intent);
        }
    }

    /**
     * Case the user needs to go backward
     */
    @Override
    public void onBackPressed() {

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                getSupportFragmentManager().popBackStack();

            } else {

                super.onBackPressed();
            }

    }


}
