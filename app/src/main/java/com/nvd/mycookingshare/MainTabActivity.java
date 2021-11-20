package com.nvd.mycookingshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

public class MainTabActivity extends TabActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);
        Intent iget = getIntent();
        String userId = iget.getStringExtra("userid");
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost); // initiate TabHost
        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab

        spec = tabHost.newTabSpec("home"); // Create a new TabSpec using tab host
        spec.setIndicator("Trang chủ"); // set the “HOME” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, AllRecipesActivity.class);
        intent.putExtra("userid",userId);
        spec.setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs

        Intent intent1;
        spec = tabHost.newTabSpec("Personal"); // Create a new TabSpec using tab host
        spec.setIndicator("Cá nhân"); // set the “CONTACT” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent1 = new Intent(this, PersonalActivity.class);
        intent1.putExtra("userid",userId);
        spec.setContent(intent1);
        tabHost.addTab(spec);


        //set tab which one you want to open first time 0 or 1 or 2
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // display the name of the tab whenever a tab is changed
                //Toast.makeText(getApplicationContext(), tabId, Toast.LENGTH_SHORT).show();
            }
        });

    }
}