package com.ocr.test.act3android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ocr.test.act3android.controller.ViewFragment;

/**
 * Web View Activity for mobile purpose - using a fragment
 * @author : boy
 * @version : 1.0.1
 */
public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        // retrieve intent to set the title :
        //setTitle(getIntent().getStringExtra("title"));

        ViewFragment fr = ViewFragment.create(getIntent().getStringExtra("title"),getIntent().getStringExtra("link"));

        // start the RSS fragment :
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.viewFrg, fr)
                .commit();


    }
}
