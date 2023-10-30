package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {
    private Button doUpdateQueryButton;

    private Button doUpdateButton;

    private String exceptionMessageString = "";

    private EditText queryEditText;

    private TextView originalProductTextView;

    private Button returnButton;

    private EditText newProductNameEditText;

    private EditText newProductAmountEditText;

    private Context c;

    private boolean busy;

    private boolean error;

    private ProgressDialog pd;

    private ListView lv;

    AlertDialog ad;

    private Product p;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_update);

        returnButton = findViewById(R.id.ReturnButton);

        returnButton.setOnClickListener(
                (final View v) -> {
                    switchActivities();
                }

        );


        c = this;
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please Wait.");

        setContentView(R.layout.activity_update);

        doUpdateQueryButton = findViewById(R.id.DoUpdateQueryButton);
        queryEditText = findViewById(R.id.QueryEditText);

        newProductNameEditText = findViewById(R.id.NewProductNameEditText);
        newProductNameEditText.setVisibility(View.GONE);
        newProductAmountEditText = findViewById(R.id.NewProductAmountEditText);
        newProductAmountEditText.setVisibility(View.GONE);
        lv = findViewById(R.id.LV);


        returnButton = findViewById(R.id.ReturnButton);

        originalProductTextView = findViewById(R.id.originalProductTextView);
        originalProductTextView.setVisibility(View.GONE);


        doUpdateButton = findViewById(R.id.DoUpdateButton);
        doUpdateButton.setVisibility(View.GONE);

        doUpdateButton.setOnClickListener(
                (final View v) -> {
                    SQLUpdateTask sqlUpdateTask = new SQLUpdateTask();
                    sqlUpdateTask.execute();
                }
        );

        doUpdateQueryButton.setOnClickListener(
                this::onClick
        );

//        doSelectQueryButton.setClickable(true);

        returnButton.setOnClickListener(
                (final View v) -> {
                    switchActivities();
                }

        );

        ad = new AlertDialog.Builder(this).create();

        ad.setCancelable(false);
        ad.setCanceledOnTouchOutside(false);
        ad.setButton(
                AlertDialog.BUTTON_NEUTRAL, (CharSequence) "Ok",
                (DialogInterface.OnClickListener) (dialog, which) ->

                {

                    queryEditText.setVisibility(View.VISIBLE);
                    originalProductTextView.setVisibility(View.GONE);
                    doUpdateButton.setVisibility(View.GONE);
                    newProductNameEditText.setVisibility(View.GONE);
                    newProductAmountEditText.setVisibility(View.GONE);
                    originalProductTextView.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    doUpdateQueryButton.setVisibility(View.VISIBLE);
                    populateLV();
                    dialog.dismiss();
                });


    }


    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    public Button getDoSelectQueryButton() {
        return getDoSelectQueryButton();
    }

    public void onClick(View v) {
        if (busy) return;
        busy = true;
        pd.show();

        populateLV();
//
    }
//

    private void populateLV() {
        pd.show();
        UpdateActivity.SQLQueryTask sTask = new UpdateActivity.SQLQueryTask();
        Integer[] sarr = new Integer[]{};
        ArrayList<Product> data = new ArrayList<Product>();

        sTask.execute(sarr);
        // Wait for this worker thread’s notification
//                    sTask.wait();



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {


                p = (Product) parent.getItemAtPosition(position);

                originalProductTextView.setVisibility(View.VISIBLE);
                originalProductTextView.setText("Original Product id: "+p.id+"" +
                        "\nOriginal Product name: "+p.name+"" +
                        "\nOriginal Product quantity: "+p.amount);
                queryEditText.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);
                doUpdateQueryButton.setVisibility(View.GONE);
                newProductNameEditText.setVisibility(View.VISIBLE);
                newProductAmountEditText.setVisibility(View.VISIBLE);
                doUpdateButton.setVisibility(View.VISIBLE);
//                populateLV();


                // Wait for this worker thread’s notification
//                    sTask.wait();


//                ad.setMessage("Selected Product's id: " + val);
//                ad.show();
            }


        });

    }

        class SQLQueryTask extends AsyncTask<Integer[], Integer, ArrayList<Product>> {

            public ArrayList<Product> doInBackground(Integer[]... params) {
                ArrayList<Product> result = null;

                JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(c);
                try {
                    result = jdbcDatabaseHelper.doSelect(queryEditText.getText().toString());
                } catch (Exception e) {
                    exceptionMessageString = e.toString();
                    return null;
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<Product> products) {
                super.onPostExecute(products);
                if (products == null) {
                    ad.setMessage("Exception: " + exceptionMessageString);
                    ad.show();
                    busy = false;
                    pd.dismiss();
                    return;
                }


                ProductAdapter pa = new ProductAdapter(c, products);
                lv.setAdapter(pa);

                busy = false;
                pd.dismiss();

            }
        }
//

    class SQLUpdateTask extends AsyncTask<Integer[], Integer, ArrayList<Product>> {

        public ArrayList<Product> doInBackground(Integer[]... params) {
            JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(c);
            String newProductName = newProductNameEditText.getText().toString();
            int newProductAmount;
            try {
                newProductAmount = Integer.parseInt(
                        newProductAmountEditText.getText().toString()
                );
            } catch (NumberFormatException e) {
                newProductAmount = -1;
            }

            try {
                jdbcDatabaseHelper.doUpdate(p.id,
                        newProductName, newProductAmount);
            } catch (Exception e) {
                exceptionMessageString = e.toString();
                error = true;
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products) {
            super.onPostExecute(products);
            if (error) {
                ad.setMessage("Exception: " + exceptionMessageString);
                ad.show();
                busy = false;
                pd.dismiss();
                error = false;
                return;
            }

            ad.setMessage("Product Updated.");
            ad.show();


            busy = false;
            pd.dismiss();



        }
    }

};

