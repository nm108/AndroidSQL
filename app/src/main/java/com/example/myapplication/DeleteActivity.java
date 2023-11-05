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
    public static final String EXCEPTION_LABEL = "Exception: ";
    public static final String PRODUCT_NAME_LABEL = "Product Name: ";
    public static final String PRODUCT_QUANTITY_LABEL = "Product Quantity: ";
    public static final String OK_LABEL = "Ok";
    public static final String DATABASE_OPERATION_LABEL = "Database Operation";
    public static final String PRODUCT_DELETED_LABEL = "Product Deleted";
    public static final String CANCEL_LABEL = "Cancel";
    public static final String DO_YOU_WANT_TO_DELETE_A_PRODUCT_QUESTION_LABEL = "Do you want to Delete a Product?";
    public static final String DELETE_LABEL = "Delete";
    public static final String PLEASE_WAIT_LABEL = "Please Wait.";
    public static final String EXCEPTION_OCCURED_LABEL = "Exception Occured";

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
        errorAlertDialog.setTitle(EXCEPTION_OCCURED_LABEL);
        errorAlertDialog.setCancelable(false);
        errorAlertDialog.setCanceledOnTouchOutside(false);
        errorAlertDialog.setMessage(EXCEPTION_LABEL);
        errorAlertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, (CharSequence)  OK_LABEL,
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
        progressDialog.setMessage(PLEASE_WAIT_LABEL);
    }


    private void switchActivityToMain() {
        Intent switchActivityIntent = new Intent(this,
                MainActivity.class);
        startActivity(switchActivityIntent);
    }

    private void onClick(View v) {
        populateProductsListView();
    }

    private void prepareDeleteQuestionAlertDialog() {
        deleteProductQuestionAlertDialog = new AlertDialog.Builder( this ).create();
        deleteProductQuestionAlertDialog.setTitle(
                DO_YOU_WANT_TO_DELETE_A_PRODUCT_QUESTION_LABEL);
        deleteProductQuestionAlertDialog.setCancelable(false);
        deleteProductQuestionAlertDialog.setCanceledOnTouchOutside(false);
        deleteProductQuestionAlertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, (CharSequence) DELETE_LABEL,
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    try {
                        if (busyDeleting) {
                            return;
                        }
                        busyDeleting = true;
                        SQLDeleteAsyncTask sqlDeleteAsyncTask = new SQLDeleteAsyncTask();
                        sqlDeleteAsyncTask.execute();
                        populateProductsListView();


                        dialog.dismiss();
                    } catch (Exception e) {
                        dialog.dismiss();
                        errorAlertDialog.setMessage(EXCEPTION_LABEL+e);
                        errorAlertDialog.show();
                    }

                });
        deleteProductQuestionAlertDialog.setButton(
                AlertDialog.BUTTON_NEGATIVE, CANCEL_LABEL,
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                });
    }

    private void prepareOperationResultAlertDialog() {
        operationResultAlertDialog = new AlertDialog.Builder(this).create();
        operationResultAlertDialog.setTitle(DATABASE_OPERATION_LABEL);
        operationResultAlertDialog.setMessage(PRODUCT_DELETED_LABEL);
        operationResultAlertDialog.setCancelable(false);
        operationResultAlertDialog.setCanceledOnTouchOutside(false);

        operationResultAlertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, OK_LABEL,
                (DialogInterface.OnClickListener)
                        (dia, wh) -> {
                    populateProductsListView();
                    dia.dismiss();
                });
    }

    void populateProductsListView() {
        if (busy) return;
        busy = true;
        progressDialog.show();


        SQLSelectAsyncTask sTask = new SQLSelectAsyncTask();

        try {
            sTask.execute();
        } catch (Exception e) {
            errorAlertDialog.setMessage(EXCEPTION_LABEL+e);
            errorAlertDialog.show();
            return;
        }
    }



    class SQLSelectAsyncTask extends AsyncTask<Void[], Void, ArrayList<Product>> {


        private boolean error = false;

        public ArrayList<Product> doInBackground(Void[]... params) {
            ArrayList<Product> result = null;

            JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(context);
            try {
                result = jdbcDatabaseHelper.doSelect(deleteQueryEditText.getText().toString());

            } catch (Exception e) {
                error = true;
                errorAlertDialog.setMessage(EXCEPTION_LABEL+e);

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


            ProductAdapter productAdapter = new ProductAdapter(context, products);
            productsListView.setAdapter(productAdapter);
            busy = false;
            progressDialog.dismiss();





            productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v, int position,
                                        long id) {
                    Product p = (Product) parent.getItemAtPosition(position);
                    productIdToDelete = p.id;
                    deleteProductQuestionAlertDialog.setMessage(PRODUCT_NAME_LABEL +p.name+"\n"+
                            PRODUCT_QUANTITY_LABEL +p.amount);
                    deleteProductQuestionAlertDialog.show();


                }
            });



            doQueryButton.setVisibility(View.VISIBLE);

        }

    }

    class SQLDeleteAsyncTask extends AsyncTask<Integer[], Integer, ArrayList<Product>> {


        private boolean error = false;


        public ArrayList<Product> doInBackground(Integer[]... params) {

            busy = true;
            ArrayList<Product> result = null;
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            try {
                new JDBCDatabaseHelper(context).doDelete(productIdToDelete);
            } catch (Exception e) {
                error = true;
                errorAlertDialog.setMessage(EXCEPTION_LABEL +e);
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

