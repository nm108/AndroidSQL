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
import java.util.List;

/**
 * Activity for DELETE operation.
 */
public class DeleteActivity extends AppCompatActivity {

    /* Constants */

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
    public static final int WAIT_LENGTH = 1000;

    /* State */

    private Button doQueryButton;

    private Button returnButton;

    private String productIdToDelete;

    private AlertDialog deleteProductQuestionAlertDialog;

    private AlertDialog operationResultAlertDialog;

    private AlertDialog errorAlertDialog;


    private Context context;


    private ProgressDialog progressDialog;

    private EditText deleteQueryEditText;

    private ListView productsListView;

    private boolean error = false;

    private boolean selecting = false;

    private boolean deleting = false;

    /* Accessors */

    public Button getDoQueryButton() {
        return doQueryButton;
    }

    public void setDoQueryButton(final Button doQueryButton) {
        this.doQueryButton = doQueryButton;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public void setReturnButton(final Button returnButton) {
        this.returnButton = returnButton;
    }

    public String getProductIdToDelete() {
        return productIdToDelete;
    }

    public void setProductIdToDelete(final String productIdToDelete) {
        this.productIdToDelete = productIdToDelete;
    }

    public AlertDialog getDeleteProductQuestionAlertDialog() {
        return deleteProductQuestionAlertDialog;
    }

    public void setDeleteProductQuestionAlertDialog(final AlertDialog deleteProductQuestionAlertDialog) {
        this.deleteProductQuestionAlertDialog = deleteProductQuestionAlertDialog;
    }

    public AlertDialog getOperationResultAlertDialog() {
        return operationResultAlertDialog;
    }

    public void setOperationResultAlertDialog(final AlertDialog operationResultAlertDialog) {
        this.operationResultAlertDialog = operationResultAlertDialog;
    }

    public AlertDialog getErrorAlertDialog() {
        return errorAlertDialog;
    }

    public void setErrorAlertDialog(final AlertDialog errorAlertDialog) {
        this.errorAlertDialog = errorAlertDialog;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(final Context context) {
        this.context = context;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(final ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public boolean isError() {
        return error;
    }

    public void setError(final boolean error) {
        this.error = error;
    }

    public EditText getDeleteQueryEditText() {
        return deleteQueryEditText;
    }

    public void setDeleteQueryEditText(final EditText deleteQueryEditText) {
        this.deleteQueryEditText = deleteQueryEditText;
    }

    public ListView getProductsListView() {
        return productsListView;
    }

    public void setProductsListView(final ListView productsListView) {
        this.productsListView = productsListView;
    }

    public boolean isSelecting() {
        return selecting;
    }

    public void setSelecting(boolean selecting) {
        this.selecting = selecting;
    }

    public boolean isDeleting() {
        return deleting;
    }

    public void setDeleting(boolean deleting) {
        this.deleting = deleting;
    }

    /* Methods */

    /**
     * @author Andrzej Wysocki (nm108)
     */
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        prepareView();
    }
    /**
     * @author Andrzej Wysocki (nm108)
     */
    private void prepareErrorAlertDialog() {
        setErrorAlertDialog(new AlertDialog.Builder(this).create());
        getErrorAlertDialog().setTitle(EXCEPTION_OCCURED_LABEL);
        getErrorAlertDialog().setCancelable(false);
        getErrorAlertDialog().setCanceledOnTouchOutside(false);
        getErrorAlertDialog().setMessage(EXCEPTION_LABEL);
        getErrorAlertDialog().setButton(
                AlertDialog.BUTTON_NEUTRAL, (CharSequence)  OK_LABEL,
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                    switchActivityToMain();
                }
        );
    }

    /**
     * @author Andrzej Wysocki (nm108)
     */
    private void prepareView() {
        setContentView(R.layout.activity_delete);
        setDoQueryButton(findViewById(R.id.DeleteDoSelectQueryButton));
        setDeleteQueryEditText(findViewById(R.id.DeleteQueryEditText));
        setProductsListView(findViewById(R.id.DeleteProductsListView));
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

    /**
     * @author Andrzej Wysocki (nm108)
     */
    private void prepareProgressDialog() {
        setProgressDialog(new ProgressDialog(this));
        getProgressDialog().setCancelable(false);
        getProgressDialog().setCanceledOnTouchOutside(false);
        getProgressDialog().setMessage(PLEASE_WAIT_LABEL);
    }


    /**
     * @author Andrzej Wysocki (nm108)
     */
    private void switchActivityToMain() {
        Intent switchActivityIntent = new Intent(this,
                MainActivity.class);
        startActivity(switchActivityIntent);
    }

    /**
     * @author Andrzej Wysocki (nm108)
     */
    private void onClick(final View v) {
        populateProductsListView();
    }
    /**
     * @author Andrzej Wysocki (nm108)
     */
    private void prepareDeleteQuestionAlertDialog() {
        setDeleteProductQuestionAlertDialog(new AlertDialog.Builder( this ).create());
        getDeleteProductQuestionAlertDialog().setTitle(
                DO_YOU_WANT_TO_DELETE_A_PRODUCT_QUESTION_LABEL);
        getDeleteProductQuestionAlertDialog().setCancelable(false);
        getDeleteProductQuestionAlertDialog().setCanceledOnTouchOutside(false);
        getDeleteProductQuestionAlertDialog().setButton(
                AlertDialog.BUTTON_POSITIVE, (CharSequence) DELETE_LABEL,
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    doDelete(dialog);
                });
        getDeleteProductQuestionAlertDialog().setButton(
                AlertDialog.BUTTON_NEGATIVE, CANCEL_LABEL,
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                });
    }
    /**
     * @author Andrzej Wysocki (nm108)
     */
    private void doDelete(final DialogInterface dialog) {
        setDeleting(true);
        updateProgressDialog();

        try {
            SQLDeleteAsyncTask sqlDeleteAsyncTask = new SQLDeleteAsyncTask();
            sqlDeleteAsyncTask.execute();
            populateProductsListView();
        } catch (Exception e) {
            dialog.dismiss();
            getErrorAlertDialog().setMessage(EXCEPTION_LABEL+e);
            getErrorAlertDialog().show();
        }
    }

    /**
     * @author Andrzej Wysocki (nm108)
     */
    private void prepareOperationResultAlertDialog() {
        setOperationResultAlertDialog(new AlertDialog.Builder(this).create());
        getOperationResultAlertDialog().setTitle(DATABASE_OPERATION_LABEL);
        getOperationResultAlertDialog().setMessage(PRODUCT_DELETED_LABEL);
        getOperationResultAlertDialog().setCancelable(false);
        getOperationResultAlertDialog().setCanceledOnTouchOutside(false);

        getOperationResultAlertDialog().setButton(AlertDialog.BUTTON_NEUTRAL, OK_LABEL,
                (DialogInterface.OnClickListener)
                        (dia, wh) -> {
                    dia.dismiss();
                });
    }
    /**
     * @author Andrzej Wysocki (nm108)
     */
    void populateProductsListView() {
        setSelecting(true);
        updateProgressDialog();
        try {
            final SQLSelectAsyncTask sTask = new SQLSelectAsyncTask();
            sTask.execute();
        } catch (Exception e) {
            getErrorAlertDialog().setMessage(EXCEPTION_LABEL+e);
            getErrorAlertDialog().show();
            return;
        }
    }

    void updateProgressDialog() {
        if (isSelecting() || isDeleting()) {
            if (!getProgressDialog().isShowing()) {
                getProgressDialog().show();
            }
        } else {
            if (getProgressDialog().isShowing()) {
                getProgressDialog().dismiss();
            }
        }
    }

    /**
     * Async Task for querying database in background (SELECT operation)
     *
     * @author Andrzej Wysocki (nm108).
     */
    class SQLSelectAsyncTask extends AsyncTask<Void[], Void, ArrayList<Product>> {

        public ArrayList<Product> doInBackground(final Void[]... params) {
            ArrayList<Product> result = null;

            // Updating GUI
            setSelecting(true);
            updateProgressDialog();

            // querying database (SELECT operation).
            JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(context);
            try {
                result = jdbcDatabaseHelper.doSelect(deleteQueryEditText.getText().toString());
            } catch (Exception e) {
                getErrorAlertDialog().setMessage(EXCEPTION_LABEL+e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<Product> products) {
            super.onPostExecute(products);
            // handle exceptions
            if (isError()) {
                getErrorAlertDialog().show();
                setError(false);
                setSelecting(false);
                updateProgressDialog();
                getDoQueryButton().setVisibility(View.VISIBLE);
                return;
            }

            // updating GUI
            ProductAdapter productAdapter = new ProductAdapter(context, products);
            getProductsListView().setAdapter(productAdapter);
            getDoQueryButton().setVisibility(View.VISIBLE);

            // attaching itemclick handler to products list view.
            productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position,
                                        long id) {
                    final Product p = (Product) parent.getItemAtPosition(position);
                    setProductIdToDelete(p.getId());
                    getDeleteProductQuestionAlertDialog().setMessage(PRODUCT_NAME_LABEL +p.getName()+"\n"+
                            PRODUCT_QUANTITY_LABEL +p.getAmount());
                    getDeleteProductQuestionAlertDialog().show();
                }
            });

            // Updating GUI
            setSelecting(false);
            updateProgressDialog();
        }
    }

    /**
     * Async Task for operating database in background (DELETE operation)
     *
     * @author Andrzej Wysocki (nm108).
     */
    class SQLDeleteAsyncTask extends AsyncTask<Void[], Void, List<Product>> {
        public List<Product> doInBackground(final Void[]... params) {
            ArrayList<Product> result = null;

            // Updating GUI
            setDeleting(true);
            updateProgressDialog();

            // operating database
            try {
                new JDBCDatabaseHelper(context).doDelete(productIdToDelete);
            } catch (Exception e) {
                setError(true);
                getErrorAlertDialog().setMessage(EXCEPTION_LABEL +e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<Product> lp) {
            // handling exceptions
            if (isError()) {
                getErrorAlertDialog().show();
                setError(false);
                setDeleting(false);
                updateProgressDialog();

                return;
            }

            // updating GUI
            getOperationResultAlertDialog().show();
            setDeleting(false);
            updateProgressDialog();
        }
    }
}

