package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Entry point for application.
 */
public class SelectActivity extends AppCompatActivity {
    private Button doSelectQueryButton;
    private EditText userNameEditText;

    private ListView lv;

    private Button returnButton;

    private String userName = "aw108";

    private AlertDialog ad;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        doSelectQueryButton = findViewById(R.id.DoSelectQueryButton);
        userNameEditText = findViewById(R.id.UserNameEditText);

        lv = findViewById(R.id.LV);


        returnButton = findViewById(R.id.ReturnButton);

        doSelectQueryButton.setOnClickListener(
                this::onClick
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

    private void onClick(View v) {
        JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper();
        ArrayList selectList = null;

        try {
            selectList = (ArrayList) jdbcDatabaseHelper.doSelect(userNameEditText.getText().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ad = new AlertDialog.Builder( this ).create();
        ad.setTitle("title");
        ad.setMessage("valueToDisplay: value");
        ad.setButton(
                AlertDialog.BUTTON_POSITIVE, (CharSequence) "Ok",
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                });
        ad.setButton(
                AlertDialog.BUTTON_NEGATIVE, "Cancel",
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                });

        ProductAdapter adapter = new ProductAdapter(this, selectList);

      lv.setOnItemClickListener(new OnItemClickListener() {

                                    public void onItemClick(AdapterView<?> parent, View v, int position,
                                                            long id) {
                                        Product p = (Product) parent.getItemAtPosition(position);
                                        String val = p.id;
                                        ad.setMessage("id: "+val);
                                        ad.show();


                                    }
                                });
        lv.setAdapter(adapter);
    }
}
