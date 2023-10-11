package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.internal.VisibilityAwareImageButton;

import java.sql.SQLException;
import java.util.ArrayList;


public class InsertActivity extends AppCompatActivity{
    private Button doInsertQueryButton;

    private Button returnButton;

    private TextView itv;


private EditText productNameEditText;

private EditText productAmountEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        productNameEditText = findViewById(R.id.InsertProductNameEditText);
        productAmountEditText = findViewById(R.id.InsertProductAmountEditText);
        itv = findViewById(R.id.InsertTextView);
        doInsertQueryButton = findViewById(R.id.DoInsertQueryButton);
        returnButton = findViewById(R.id.InsertReturnButton);
        doInsertQueryButton.setOnClickListener(this::onClick);
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

    doInsertQueryButton.setVisibility(View.GONE);
    try {
        doInsert();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    switchActivities();
    }



private void doInsert() throws SQLException {
    String productName = productNameEditText.getText().toString();

    if (productName == null || productName.equals("")) {
        productName = "-=- -=-";
    }

    int pamount;
    try {
        pamount = Integer.parseInt(
                productAmountEditText.getText().toString()
        );}
    catch (Exception e) {
        pamount = -1;
    }

    JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper();
    jdbcDatabaseHelper.doInsert(productName, pamount);

}
};
