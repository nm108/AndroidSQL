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


public class InsertActivity extends AppCompatActivity{
    public static final String PRODUCT_INSERTED_LABEL = "Product Inserted.";
    public static final String PLEASE_WAIT_LABEL = "Please Wait.";
    public static final String OK_LABEL = "Ok";
    public static final String SQL_EXCEPTION_LABEL = "SQL Exception: ";
    public static final String EMPTY_STRING = "";
    public static final String EXCEPTION_LABEL = "Exception: ";
    private Button doInsertQueryButton;

    private Button returnButton;

    private TextView insertTextView;

    private ProgressDialog progressDialog;


    private AlertDialog alertDialog;


    private boolean busy = false;

    private boolean error;


    private EditText productNameEditText;

    private EditText productAmountEditText;

    private Context context = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        prepareProgressDialog();
        prepareAlertDialog();
        prepareView();
    }

    private void prepareView() {
        productNameEditText = findViewById(R.id.InsertProductNameEditText);
        productAmountEditText = findViewById(R.id.InsertProductAmountEditText);
        insertTextView = findViewById(R.id.InsertTextView);
        doInsertQueryButton = findViewById(R.id.DoInsertQueryButton);
        doInsertQueryButton.setOnClickListener(this::doInsert);

        returnButton = findViewById(R.id.InsertReturnButton);
        returnButton.setOnClickListener(
                (final View v) -> {
                    switchActivityToMain();
                }

        );
    }

    private void prepareAlertDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(PRODUCT_INSERTED_LABEL);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, (CharSequence) OK_LABEL,
                (DialogInterface.OnClickListener) (dialog, which) ->

                {
                    dialog.dismiss();
                    if (error) {
                        switchActivityToMain();
                    }
                });
    }

    private void prepareProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(PLEASE_WAIT_LABEL);
    }

    private void switchActivityToMain() {
        Intent switchActivityIntent = new Intent(
                this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    private void doInsert(View v) {
    // we do not want clicks to queue, so we 'swallow' them.
        if (busy) return;
        busy = true;

        try {
            progressDialog.show();
            SqlInsertAsyncTask st = new SqlInsertAsyncTask();
            st.execute();
        } catch (android.database.SQLException e) {
            alertDialog.setMessage(SQL_EXCEPTION_LABEL + e);
            error = true;
            alertDialog.show();

        }
    // switchActivities();
    }

    class SqlInsertAsyncTask extends AsyncTask<Void[], Void, Void> {

        public Void doInBackground(Void[]... params) {
            String productName = productNameEditText.getText().toString();

            if (productName == null || productName.equals(EMPTY_STRING)) {
                productName = "!!!";
            }

            int pamount;
            try {
                pamount = Integer.parseInt(
                        productAmountEditText.getText().toString()
                );
            } catch (Exception e) {
                pamount = -1;
            }

            JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(context);
            try {
                jdbcDatabaseHelper.doInsert(productName, pamount);
            } catch (Exception e) {
                alertDialog.setMessage(EXCEPTION_LABEL +e);
                error = true;
            }
            return null;
        }

        protected void onPostExecute(Void v) {
            // we can accept clicks again
            busy = false;

            progressDialog.dismiss();
            alertDialog.show();
        }

    }


};
