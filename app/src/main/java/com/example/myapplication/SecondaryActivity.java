package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Entry point for application.
 */
public class SecondaryActivity extends AppCompatActivity {

    private Button switchActivitiesButton;

    private ListView lv;

    private String[] myStringArray = new String[] {
            "1", "2", "Three"
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        switchActivitiesButton = findViewById(R.id.SwitchActivitiesButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, myStringArray);

        ListView lv = (ListView) findViewById(R.id.SimpleListView);

        lv.setAdapter(adapter);

        // Create a message handling object as an anonymous class.
        AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Log.d("click", "onItemClick: ");
            }
        };

        lv.setOnItemClickListener(messageClickedHandler);


        switchActivitiesButton.setOnClickListener(
                (final View v) -> {
                    switchActivities();
                });
    }

    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, DatabaseQueryActivity.class);
        startActivity(switchActivityIntent);
    }
}
