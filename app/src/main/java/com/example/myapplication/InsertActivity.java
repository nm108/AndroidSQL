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
    public static final String PRODUCT_INSERTED_LABEL = "Product Inserted";
    private Button doInsertQueryButton;

    private Button returnButton;

    private TextView itv;

    private ProgressDialog pd;


    private AlertDialog ad;


    private boolean busy = false;

    private boolean error;


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
                    switchActivityToMain();
                }

        );

        ad = new AlertDialog.Builder(this).create();
        ad.setTitle(PRODUCT_INSERTED_LABEL);
        ad.setCancelable(false);
        ad.setCanceledOnTouchOutside(false);
        ad.setButton(
                AlertDialog.BUTTON_NEUTRAL, (CharSequence) "Ok",
                (DialogInterface.OnClickListener) (dialog, which) ->

                {
                    dialog.dismiss();
                    if (error) {
                        switchActivityToMain();
                    }
                });
    }

    private void switchActivityToMain() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    private void onClick(View v) {
    // we do not want clicks to queue, so we 'swallow' them.
        if (busy) return;
        busy = true;

        try {
            pd.show();
            SqlInsertAsyncTask st = new SqlInsertAsyncTask();
            st.execute();
        } catch (android.database.SQLException e) {
            ad.setMessage("SQL Exception: " + e);
            error = true;
            ad.show();

        }
    // switchActivities();
    }

    class SqlInsertAsyncTask extends AsyncTask<Void[], Void, Void> {

        public Void doInBackground(Void[]... params) {
            String productName = productNameEditText.getText().toString();

            if (productName == null || productName.equals("")) {
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

            JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(c);
            try {
                jdbcDatabaseHelper.doInsert(productName, pamount);
            } catch (Exception e) {
                ad.setMessage("Exception: "+e);
                error = true;
            }
            return null;
        }

        protected void onPostExecute(Void v) {
            // we can accept clicks again
            busy = false;

            pd.dismiss();
            ad.show();
        }

    }


};
