package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.List;


/**
 * Entry point for application.
 */
public class SelectActivity extends AppCompatActivity {
    private Button doSelectQueryButton;
    private EditText userNameEditText;
    private ProgressDialog progressDialog;
    private ListView lv;

    private Button returnButton;

    private String userName = "aw108";

    private String exceptionMessageString = "";

    ArrayList<Product> selectList;

    private AlertDialog ad;

    private ProgressDialog pd;

    private boolean busy = false;

    private Context c;
    private View v;
    QueryForUserDataAsyncTask at;

    protected void onCreate(Bundle savedInstanceState) {
        c = this;
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please Wait.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        doSelectQueryButton = findViewById(R.id.DoSelectQueryButton);
        userNameEditText = findViewById(R.id.UserNameEditText);

        lv = findViewById(R.id.LV);


        returnButton = findViewById(R.id.ReturnButton);

        doSelectQueryButton.setOnClickListener(
                this::onClick
        );

//        doSelectQueryButton.setClickable(true);

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

    public Button getDoSelectQueryButton() {
        return getDoSelectQueryButton();
    }

    public void onClick(View v) {
        if (busy) return;
        busy = true;
        pd.show();
////        Thread t = new ThreadQuerySQL();
////        t.run();
//
        SQLTask sTask = new SQLTask();
        Integer[] sarr = new Integer[]{};
        ArrayList<Product> data = new ArrayList<Product>();
        try {
            sTask.execute(sarr);
            // Wait for this worker thread’s notification
//                    sTask.wait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ad = new AlertDialog.Builder(this).create();
        ad.setTitle("Selected Product");
        ad.setCancelable(false);
        ad.setCanceledOnTouchOutside(false);
        ad.setButton(
                AlertDialog.BUTTON_NEUTRAL, (CharSequence) "Ok",
                (DialogInterface.OnClickListener) (dialog, which) ->

                {
                    dialog.dismiss();
                });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      public void onItemClick(AdapterView<?> parent, View v, int position,
                                                              long id) {


                                          Product p = (Product) parent.getItemAtPosition(position);
                                          String val = p.id;
                                          ad.setMessage("Selected Product's id: " + val);
                                          ad.show();
                                      }
                                  });

//


                                  }




    class SQLTask extends AsyncTask<Integer[], Integer, ArrayList<Product>> {

        public ArrayList<Product> doInBackground(Integer[]... params) {
            ArrayList<Product> result = null;

            JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper();
            try {
                result = jdbcDatabaseHelper.doSelect(userNameEditText.getText().toString());
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
                ad.setMessage("Exception: "+exceptionMessageString);
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


}