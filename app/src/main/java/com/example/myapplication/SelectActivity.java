package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.example.myapplication.R;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Entry point for application.
 */
public class SelectActivity extends AppCompatActivity {
    private Button doSelectQueryButton;
    private EditText userNameEditText;

    private ListView lv;

    private Button returnButton;

    private String userName = "aw108";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        doSelectQueryButton = findViewById(R.id.DoSelectQueryButton);
        userNameEditText = findViewById(R.id.UserNameEditText);

        lv = findViewById(R.id.LV);


        returnButton = findViewById(R.id.ReturnButton);

        doSelectQueryButton.setOnClickListener(
            (final View v) -> {
                JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper();
                 List selectList = null;
                try {
                    selectList = jdbcDatabaseHelper.doSelect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
//                String listString = String.join(", ", selectList);

//                ArrayList arrList = new ArrayList();
//                arrList.add("aaaaa");
//                arrList.add("bbb");
//                arrList.add(123);arrList.add("aaaaa");
//                arrList.add("bbb");
//                arrList.add(123);arrList.add("aaaaa");
//                arrList.add("bbb");
//                arrList.add(123);arrList.add("aaaaa");
//                arrList.add("bbb");
//                arrList.add(123);arrList.add("aaaaa");
//                arrList.add("bbb");
//                arrList.add(123);arrList.add("aaaaa");
//                arrList.add("bbb");
//                arrList.add(123);


                ArrayAdapter aa = new ArrayAdapter(this, R.layout.simple_list_item, selectList);

//
//                System.out.println("sl="+selectList);
                lv.setAdapter(aa);
//
// //               mListView.setAdapter(mAdapter);
//
////                lv.set
//                userNameEditText.setText(listString);
            }
        );

        returnButton.setOnClickListener(
                (final View v) -> {
                    switchActivities();
                }

        );


    }


    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }
}
