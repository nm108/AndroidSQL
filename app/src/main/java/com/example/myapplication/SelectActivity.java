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
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Entry point for application.
 */
public class SelectActivity extends AppCompatActivity {


    /* State */

    public static final String PRODUCT_INSERTED_LABEL = "Product Inserted";
    public static final String OK_LABEL = "Ok";
    public static final String PLEASE_WAIT_LABEL = "Please Wait.";
    private Button doSelectSelectQueryButton;
    private EditText selectQueryEditText;
    private ProgressDialog progressDialog;
    private ListView productsListView;

    private Button returnButton;

    private String exceptionMessageString = "";

    private AlertDialog alertDialog;

    private boolean busy = false;

    private Context c;
    private View v;


    /* Accessors */

    public Button getDoSelectSelectQueryButton() {
        return doSelectSelectQueryButton;
    }

    public void setDoSelectSelectQueryButton(Button doSelectSelectQueryButton) {
        this.doSelectSelectQueryButton = doSelectSelectQueryButton;
    }

    public EditText getSelectQueryEditText() {
        return selectQueryEditText;
    }

    public void setSelectQueryEditText(EditText selectQueryEditText) {
        this.selectQueryEditText = selectQueryEditText;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public ListView getProductsListView() {
        return productsListView;
    }

    public void setProductsListView(ListView productsListView) {
        this.productsListView = productsListView;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public void setReturnButton(Button returnButton) {
        this.returnButton = returnButton;
    }

    public String getExceptionMessageString() {
        return exceptionMessageString;
    }

    public void setExceptionMessageString(String exceptionMessageString) {
        this.exceptionMessageString = exceptionMessageString;
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public void setAlertDialog(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public View getV() {
        return v;
    }

    public void setV(View v) {
        this.v = v;
    }


    /* Methods */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;

        prepareView();
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
                    switchActivityToMain();
                });
    }

    private void prepareView() {
        setContentView(R.layout.activity_select);
        doSelectSelectQueryButton = findViewById(R.id.DoSelectQueryButton);
        selectQueryEditText = findViewById(R.id.UserNameEditText);
        productsListView = findViewById(R.id.LV);
        returnButton = findViewById(R.id.ReturnButton);

        doSelectSelectQueryButton.setOnClickListener(
                this::selectQueryClickHandler
        );

        returnButton.setOnClickListener(
                (final View v) -> {
                    switchActivityToMain();
                }

        );

        prepareProgressDialog();
        prepareAlertDialog();
    }

    private void prepareProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(PLEASE_WAIT_LABEL);
    }

    private void switchActivityToMain() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }



    public void selectQueryClickHandler(View v) {
        // we do not want button clicks queueing, so we just 'swallow'
        // unneeded extra ones.
        if (busy) return;
        busy = true;


        progressDialog.show();

        sqlSelectAsyncTask sTask = new sqlSelectAsyncTask();
        Integer[] sarr = new Integer[]{};
        ArrayList<Product> data = new ArrayList<Product>();
        try {
            sTask.execute();
            // Wait for this worker thread’s notification
//                    sTask.wait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Exception");
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, (CharSequence) "Ok",
                (DialogInterface.OnClickListener) (dialog, which) ->

                {
                    dialog.dismiss();
                    switchActivityToMain();
                });



//


                                  }




    class sqlSelectAsyncTask extends AsyncTask<List[], Void, ArrayList<Product>> {

        public ArrayList<Product> doInBackground(List[]... params) {
            ArrayList<Product> result = null;

            JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(c);
            try {
                result = jdbcDatabaseHelper.doSelect(selectQueryEditText.getText().toString());
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
                alertDialog.setMessage("Exception: "+exceptionMessageString);
                alertDialog.show();
                busy = false;
                progressDialog.dismiss();
                return;
            }

            ProductAdapter pa = new ProductAdapter(c, products);
            productsListView.setAdapter(pa);

            busy = false;
            progressDialog.dismiss();

        }

    }


}