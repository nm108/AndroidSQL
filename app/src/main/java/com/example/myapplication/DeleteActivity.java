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

    private boolean error = false;


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
        setErrorAlertDialog(new AlertDialog.Builder(this).create());
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
        setDoQueryButton(findViewById(R.id.DeleteDoSelectQueryButton));
        setDeleteQueryEditText(findViewById(R.id.DeleteQueryEditText));
        setProductsListView(findViewById(R.id.DeleteLV));
        setReturnButton(findViewById(R.id.DeleteReturnButton));

        getDoQueryButton().setOnClickListener(
                this::onClick
        );

        getReturnButton().setOnClickListener(
                (final View v) -> {
                    switchActivityToMain();
                }

        );

        getReturnButton().setOnClickListener(
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
        setDeleteProductQuestionAlertDialog(new AlertDialog.Builder( this ).create());
        deleteProductQuestionAlertDialog.setTitle(
                DO_YOU_WANT_TO_DELETE_A_PRODUCT_QUESTION_LABEL);
        deleteProductQuestionAlertDialog.setCancelable(false);
        deleteProductQuestionAlertDialog.setCanceledOnTouchOutside(false);
        deleteProductQuestionAlertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, (CharSequence) DELETE_LABEL,
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    doDelete(dialog);
                });
        deleteProductQuestionAlertDialog.setButton(
                AlertDialog.BUTTON_NEGATIVE, CANCEL_LABEL,
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                });
    }

    private void doDelete(DialogInterface dialog) {
        try {
            if (busy) {
                return;
            }
            if (busyDeleting) {
                return;
            }
            busyDeleting = true;
            SQLDeleteAsyncTask sqlDeleteAsyncTask = new SQLDeleteAsyncTask();
            sqlDeleteAsyncTask.execute();
            populateProductsListView();
        } catch (Exception e) {
            dialog.dismiss();
            errorAlertDialog.setMessage(EXCEPTION_LABEL+e);
            errorAlertDialog.show();
        }
    }

    private void prepareOperationResultAlertDialog() {
        setOperationResultAlertDialog(new AlertDialog.Builder(this).create());
        operationResultAlertDialog.setTitle(DATABASE_OPERATION_LABEL);
        operationResultAlertDialog.setMessage(PRODUCT_DELETED_LABEL);
        operationResultAlertDialog.setCancelable(false);
        operationResultAlertDialog.setCanceledOnTouchOutside(false);

        operationResultAlertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, OK_LABEL,
                (DialogInterface.OnClickListener)
                        (dia, wh) -> {
                    dia.dismiss();
                });
    }

    void populateProductsListView() {
        if (busy) return;
        busy = true;
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        try {
            SQLSelectAsyncTask sTask = new SQLSelectAsyncTask();
            sTask.execute();
        } catch (Exception e) {
            errorAlertDialog.setMessage(EXCEPTION_LABEL+e);
            errorAlertDialog.show();
            return;
        }
    }

    /**
     * Async Task for querying database in background (SELECT operation)
     *
     * @author Andrzej Wysocki (nm108).
     */
    class SQLSelectAsyncTask extends AsyncTask<Void[], Void, ArrayList<Product>> {

        public ArrayList<Product> doInBackground(Void[]... params) {
            ArrayList<Product> result = null;

            // querying database (SELECT operation).
            JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(context);
            try {
                result = jdbcDatabaseHelper.doSelect(deleteQueryEditText.getText().toString());
            } catch (Exception e) {
                setBusy(true);
                errorAlertDialog.setMessage(EXCEPTION_LABEL+e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products) {
            super.onPostExecute(products);
            // handle exceptions
            if (error) {
                errorAlertDialog.show();
                error = false;
                progressDialog.dismiss();
                doQueryButton.setVisibility(View.VISIBLE);
                busy = false;
                return;
            }

            // we do not want clicks to queue, thus we 'swallow' unneeded
            // extra ones.
            busy = false;

            // updating GUI
            progressDialog.dismiss();
            ProductAdapter productAdapter = new ProductAdapter(context, products);
            productsListView.setAdapter(productAdapter);
            doQueryButton.setVisibility(View.VISIBLE);

            // attaching itemclick handler to products list view.
            productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position,
                                        long id) {
                    final Product p = (Product) parent.getItemAtPosition(position);
                    productIdToDelete = p.id;
                    deleteProductQuestionAlertDialog.setMessage(PRODUCT_NAME_LABEL +p.name+"\n"+
                            PRODUCT_QUANTITY_LABEL +p.amount);
                    deleteProductQuestionAlertDialog.show();
                }
            });
        }
    }

    /**
     * Async Task for operating database in background (DELETE operation)
     *
     * @author Andrzej Wysocki (nm108).
     */
    class SQLDeleteAsyncTask extends AsyncTask<Integer[], Integer, ArrayList<Product>> {
        public ArrayList<Product> doInBackground(Integer[]... params) {
            ArrayList<Product> result = null;

            // updating GUI
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

            // operating database
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

            // handling exceptions
            if (isError()) {
                errorAlertDialog.show();
                setError(false);
                progressDialog.dismiss();

                return;
            }

            // updating GUI
            progressDialog.dismiss();
            operationResultAlertDialog.show();
            populateProductsListView();

            // we can handle clicks again
            busy = false;
            busyDeleting = false;
        }
    }
}

