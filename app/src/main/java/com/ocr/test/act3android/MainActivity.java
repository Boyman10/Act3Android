package com.ocr.test.act3android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * MainActivity launching RSS feeds from several sources concurrently
 * and allowing the user to share some content. Using fragments
 * @author boy
 * @version 1.0.0
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // start the RSS fragment :
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.rssFrg, new RssFragment())
                .commit();
    }
}
