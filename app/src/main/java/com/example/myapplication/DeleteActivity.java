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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;

public class DeleteActivity extends AppCompatActivity {

    private Button doDeleteQueryButton;

    private Button doQueryButton;

    private Button returnButton;

    private String productIdToDelete;

    private AlertDialog dad;

    private AlertDialog exceptionAd;


    private Context c;


    private ProgressDialog pd;

private boolean error = false;
    private boolean busy = false;

private EditText deleteQueryEditText;

    private ListView lv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        c = this;
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please Wait.");

        setContentView(R.layout.activity_delete);
        doQueryButton = findViewById(R.id.DeleteDoSelectQueryButton);
        deleteQueryEditText = findViewById(R.id.DeleteQueryEditText);

        lv = findViewById(R.id.DeleteLV);

        returnButton = findViewById(R.id.DeleteReturnButton);


        doQueryButton.setOnClickListener(
                this::onClick
        );

        returnButton.setOnClickListener(
                (final View v) -> {
                    switchActivities();
                }

        );

//        doSelectQueryButton.setClickable(true);

        returnButton.setOnClickListener(
                (final View v) -> {
                    switchActivities();
                }

        );

        exceptionAd = new AlertDialog.Builder(this).create();
        exceptionAd.setTitle("Exception Occured");
        exceptionAd.setCancelable(false);
        exceptionAd.setCanceledOnTouchOutside(false);
        exceptionAd.setMessage("Exception: ");
        exceptionAd.setButton(
                AlertDialog.BUTTON_NEUTRAL, (CharSequence)  "Ok",
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                }
        );


    }


    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    private void onClick(View v) {

populateLV();



        dad = new AlertDialog.Builder( this ).create();
        dad.setTitle("Do you want to Delete a Product?");
        dad.setCancelable(false);
        dad.setCanceledOnTouchOutside(false);
        dad.setButton(
                AlertDialog.BUTTON_POSITIVE, (CharSequence) "Delete",
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    try {
                        new JDBCDatabaseHelper().doDelete(productIdToDelete);
                        populateLV();
                        dialog.dismiss();
                    } catch (Exception e) {
                        dialog.dismiss();
                        exceptionAd.setMessage("Exception: "+e);
                        exceptionAd.show();
                    }

                });
        dad.setButton(
                AlertDialog.BUTTON_NEGATIVE, "Cancel",
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                });


    }

    void populateLV() {
        if (busy) return;
        busy = true;
        pd.show();

        DeleteActivity.SQLTask sTask = new DeleteActivity.SQLTask();
        Integer[] sarr = new Integer[]{};
        ArrayList<Product> data = new ArrayList<Product>();



        try {

            sTask.execute(sarr);
        } catch (Exception e) {
            exceptionAd.setMessage("Exception: "+e);
            exceptionAd.show();
            return;
        }

        JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper();






     ////        Thread t = new ThreadQuerySQL();
////        t.run();

//


    }



    class SQLTask extends AsyncTask<Integer[], Integer, ArrayList<Product>> {

        public ArrayList<Product> doInBackground(Integer[]... params) {
            ArrayList<Product> result = null;

            JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper();
            try {
                result = jdbcDatabaseHelper.doSelect(deleteQueryEditText.getText().toString());

            } catch (Exception e) {
                error = true;
                exceptionAd.setMessage("Exception: "+e);

            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products) {
            super.onPostExecute(products);
            if (error) {
                exceptionAd.show();
                error = false;
                pd.dismiss();
                doQueryButton.setVisibility(View.VISIBLE);
                busy = false;
                return;
            }
            ProductAdapter pa = new ProductAdapter(c, products);
            lv.setAdapter(pa);
            busy = false;
            pd.dismiss();


            ProductAdapter adapter = new ProductAdapter(c, products);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v, int position,
                                        long id) {
                    Product p = (Product) parent.getItemAtPosition(position);
                    productIdToDelete = p.id;
                    dad.setMessage("Product Name: "+p.name+"\nProduct Quantity: "+p.amount);
                    dad.show();


                }
            });
            lv.setAdapter(adapter);
            doQueryButton.setVisibility(View.VISIBLE);

        }

    }

}

