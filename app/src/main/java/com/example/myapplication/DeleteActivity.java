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

import java.util.ArrayList;

public class DeleteActivity extends AppCompatActivity {

    /* State */

    private Button doQueryButton;

    private Button returnButton;

    private String productIdToDelete;

    private AlertDialog deleteProductQuestionAlertDialog;

    private AlertDialog operationResultAlertDialog;

    private AlertDialog errorAlertDialog;


    private Context context;


    private ProgressDialog progressDialog;

    private boolean busyDeleting;

    private boolean error = false;
    private boolean busy = false;

    private EditText deleteQueryEditText;

    private ListView productsListView;

    /* Accessors */

    public Button getDoQueryButton() {
        return doQueryButton;
    }

    public void setDoQueryButton(Button doQueryButton) {
        this.doQueryButton = doQueryButton;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public void setReturnButton(Button returnButton) {
        this.returnButton = returnButton;
    }

    public String getProductIdToDelete() {
        return productIdToDelete;
    }

    public void setProductIdToDelete(String productIdToDelete) {
        this.productIdToDelete = productIdToDelete;
    }

    public AlertDialog getDeleteProductQuestionAlertDialog() {
        return deleteProductQuestionAlertDialog;
    }

    public void setDeleteProductQuestionAlertDialog(AlertDialog deleteProductQuestionAlertDialog) {
        this.deleteProductQuestionAlertDialog = deleteProductQuestionAlertDialog;
    }

    public AlertDialog getOperationResultAlertDialog() {
        return operationResultAlertDialog;
    }

    public void setOperationResultAlertDialog(AlertDialog operationResultAlertDialog) {
        this.operationResultAlertDialog = operationResultAlertDialog;
    }

    public AlertDialog getErrorAlertDialog() {
        return errorAlertDialog;
    }

    public void setErrorAlertDialog(AlertDialog errorAlertDialog) {
        this.errorAlertDialog = errorAlertDialog;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public boolean isBusyDeleting() {
        return busyDeleting;
    }

    public void setBusyDeleting(boolean busyDeleting) {
        this.busyDeleting = busyDeleting;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public EditText getDeleteQueryEditText() {
        return deleteQueryEditText;
    }

    public void setDeleteQueryEditText(EditText deleteQueryEditText) {
        this.deleteQueryEditText = deleteQueryEditText;
    }

    public ListView getProductsListView() {
        return productsListView;
    }

    public void setProductsListView(ListView productsListView) {
        this.productsListView = productsListView;
    }

    /* Methods */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        prepareView();
    }

    private void prepareErrorAlertDialog() {
        errorAlertDialog = new AlertDialog.Builder(this).create();
        errorAlertDialog.setTitle("Exception Occured");
        errorAlertDialog.setCancelable(false);
        errorAlertDialog.setCanceledOnTouchOutside(false);
        errorAlertDialog.setMessage("Exception: ");
        errorAlertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, (CharSequence)  "Ok",
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                    switchActivityToMain();
                }
        );
    }

    private void prepareView() {
        setContentView(R.layout.activity_delete);
        doQueryButton = findViewById(R.id.DeleteDoSelectQueryButton);
        deleteQueryEditText = findViewById(R.id.DeleteQueryEditText);
        productsListView = findViewById(R.id.DeleteLV);
        returnButton = findViewById(R.id.DeleteReturnButton);

        doQueryButton.setOnClickListener(
                this::onClick
        );

        returnButton.setOnClickListener(
                (final View v) -> {
                    switchActivityToMain();
                }

        );

        returnButton.setOnClickListener(
                (final View v) -> {
                    switchActivityToMain();
                }
        );

        prepareProgressDialog();
        prepareErrorAlertDialog();
        prepareOperationResultAlertDialog();
        prepareDeleteQuestionAlertDialog();
    }

    private void prepareProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please Wait.");
    }


    private void switchActivityToMain() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    private void onClick(View v) {
        populateLV();
    }

    private void prepareDeleteQuestionAlertDialog() {
        deleteProductQuestionAlertDialog = new AlertDialog.Builder( this ).create();
        deleteProductQuestionAlertDialog.setTitle("Do you want to Delete a Product?");
        deleteProductQuestionAlertDialog.setCancelable(false);
        deleteProductQuestionAlertDialog.setCanceledOnTouchOutside(false);
        deleteProductQuestionAlertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, (CharSequence) "Delete",
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    try {
                        if (busyDeleting) {
                            return;
                        }
                        busyDeleting = true;
                        SQLDeleteTask sqlDeleteTask = new SQLDeleteTask();
                        sqlDeleteTask.execute();
                        populateLV();


                        dialog.dismiss();
                    } catch (Exception e) {
                        dialog.dismiss();
                        errorAlertDialog.setMessage("Exception: "+e);
                        errorAlertDialog.show();
                    }

                });
        deleteProductQuestionAlertDialog.setButton(
                AlertDialog.BUTTON_NEGATIVE, "Cancel",
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                });
    }

    private void prepareOperationResultAlertDialog() {
        operationResultAlertDialog = new AlertDialog.Builder(this).create();
        operationResultAlertDialog.setTitle("Database Operation");
        operationResultAlertDialog.setMessage("Product Deleted");
        operationResultAlertDialog.setCancelable(false);
        operationResultAlertDialog.setCanceledOnTouchOutside(false);

        operationResultAlertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                (DialogInterface.OnClickListener)
                        (dia, wh) -> {
                    populateLV();
                    dia.dismiss();
                });
    }

    void populateLV() {
        if (busy) return;
        busy = true;
        progressDialog.show();


        DeleteActivity.SQLTask sTask = new DeleteActivity.SQLTask();
        Integer[] sarr = new Integer[]{};
        ArrayList<Product> data = new ArrayList<Product>();

        try {
            sTask.execute(sarr);
        } catch (Exception e) {
            errorAlertDialog.setMessage("Exception: "+e);
            errorAlertDialog.show();
            return;
        }
    }



    class SQLTask extends AsyncTask<Integer[], Integer, ArrayList<Product>> {

        public ArrayList<Product> doInBackground(Integer[]... params) {
            ArrayList<Product> result = null;

            JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(context);
            try {
                result = jdbcDatabaseHelper.doSelect(deleteQueryEditText.getText().toString());

            } catch (Exception e) {
                error = true;
                errorAlertDialog.setMessage("Exception: "+e);

            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products) {
            super.onPostExecute(products);
            if (error) {
                errorAlertDialog.show();
                error = false;
                progressDialog.dismiss();
                doQueryButton.setVisibility(View.VISIBLE);
                busy = false;
                return;
            }


            ProductAdapter pa = new ProductAdapter(context, products);
            productsListView.setAdapter(pa);
            busy = false;
            progressDialog.dismiss();




            ProductAdapter adapter = new ProductAdapter(context, products);

            productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v, int position,
                                        long id) {
                    Product p = (Product) parent.getItemAtPosition(position);
                    productIdToDelete = p.id;
                    deleteProductQuestionAlertDialog.setMessage("Product Name: "+p.name+"\nProduct Quantity: "+p.amount);
                    deleteProductQuestionAlertDialog.show();


                }
            });
            productsListView.setAdapter(adapter);


            doQueryButton.setVisibility(View.VISIBLE);

        }

    }

    class SQLDeleteTask extends AsyncTask<Integer[], Integer, ArrayList<Product>> {

        public ArrayList<Product> doInBackground(Integer[]... params) {

            busy = true;
            ArrayList<Product> result = null;
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
//            pd.show();
            try {
                new JDBCDatabaseHelper(context).doDelete(productIdToDelete);
            } catch (Exception e) {
                error = true;
                errorAlertDialog.setMessage("Exception: "+e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products) {
            super.onPostExecute(products);
            if (error) {
                errorAlertDialog.show();
                error = false;
                progressDialog.dismiss();

                return;
            }
            progressDialog.dismiss();

            operationResultAlertDialog.show();

            busy = false;
            busyDeleting = false;
        }

    }

}

