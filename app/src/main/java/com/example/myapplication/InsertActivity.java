package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

    private ProgressDialog pd;


    private AlertDialog ad;


    private boolean busy = false;


private EditText productNameEditText;

private EditText productAmountEditText;

private Context c = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please Wait.");

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

        ad = new AlertDialog.Builder(this).create();
        ad.setTitle("Product Inserted");
        ad.setCancelable(false);
        ad.setCanceledOnTouchOutside(false);
        ad.setButton(
                AlertDialog.BUTTON_NEUTRAL, (CharSequence) "Ok",
                (DialogInterface.OnClickListener) (dialog, which) ->

                {
                    dialog.dismiss();
                    switchActivities();
                });
    }




private void switchActivities() {

        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

private void onClick(View v) {
    if (busy) return;
    busy = true;
    try {
        pd.show();

        SQLTask st = new SQLTask();
        st.execute();
    } catch (android.database.SQLException e) {
        ad.setMessage("SQL Exception: " + e);
        ad.show();

    }
    // switchActivities();
    }

    class SQLTask extends AsyncTask<Void[], Void, Void> {

        public Void doInBackground(Void[]... params) {
            String productName = productNameEditText.getText().toString();

            if (productName == null || productName.equals("")) {
                productName = "!!!";
            }

            int pamount;
            try {
                pamount = Integer.parseInt(
                        productAmountEditText.getText().toString()
                );}
            catch (Exception e) {
                pamount = -1;
            }

            JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(c);
            try {
                jdbcDatabaseHelper.doInsert(productName, pamount);
            } catch (Exception e) {
                ad.setMessage("Exception: "+e);
            }
            return null;
        }

        protected void onPostExecute(Void v) {

            busy = false;

            pd.dismiss();
            ad.show();
        }

    }


};
