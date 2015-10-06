package com.camon.locationtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.sql.Date;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName();
    private TextView tv;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "TODO : firebase로 업로드 되도록", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tv = (TextView) findViewById(R.id.status);
        initDB();
        showLocations();
        Log.i(TAG, "started");
    }

    public void initDB() {
        // Obtain a Realm instance
        realm = Realm.getInstance(this);
    }

    private void showLocations() {
        RealmResults<MyLocation> results = realm.where(MyLocation.class).findAll();
        StringBuilder sb = new StringBuilder();

        for (MyLocation myLocation : results) {
            DateTime dt = new DateTime(myLocation.getTime());
            sb.append("\n")
                    .append(dt)
                    .append(", ")
                    .append(myLocation.getLat())
                    .append(", ")
                    .append(myLocation.getLng());
        }

        showStatus("\n" + sb.toString());
        showStatus("\ncount: " + results.size());
    }

    private void showStatus(String msg) {
        tv.append(msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_clear) {
            realm.beginTransaction();
            realm.allObjects(MyLocation.class).clear();
            realm.commitTransaction();
        }

        if (id == R.id.action_start_service) {
            Intent i = new Intent(this, LocationTrackerService.class);
            this.startService(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
