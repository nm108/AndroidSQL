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

    private boolean busyDeleting;

    private boolean busy = false;

    private EditText deleteQueryEditText;

    private ListView productsListView;

    private boolean error = false;

    private boolean notYetDeleted = false;


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

    public boolean isBusyDeleting() {
        return busyDeleting;
    }

    public void setBusyDeleting(final boolean busyDeleting) {
        this.busyDeleting = busyDeleting;
    }

    public boolean isError() {
        return error;
    }

    public void setError(final boolean error) {
        this.error = error;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(final boolean busy) {
        this.busy = busy;
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

    public boolean isNotYetDeleted() {
        return notYetDeleted;
    }

    public void setNotYetDeleted(boolean notYetDeleted) {
        this.notYetDeleted = notYetDeleted;
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
        try {
            if (isBusy()) {
                return;
            }
            if (isBusyDeleting()) {
                return;
            }
            setBusyDeleting(true);

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
     * We want to refresh list view AFTER product deletion completes
     *
     * We sleep until product deletion is completed, then populate
     * products list view
     */

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
        if (isBusy()) return;
        setBusy(true);
        if (!getProgressDialog().isShowing()) {
            getProgressDialog().show();
        }
        try {
            final SQLSelectAsyncTask sTask = new SQLSelectAsyncTask();
            sTask.execute();
        } catch (Exception e) {
            getErrorAlertDialog().setMessage(EXCEPTION_LABEL+e);
            getErrorAlertDialog().show();
            return;
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

            // querying database (SELECT operation).
            JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(context);
            try {
                result = jdbcDatabaseHelper.doSelect(deleteQueryEditText.getText().toString());
            } catch (Exception e) {
                setBusy(true);
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
                getProgressDialog().dismiss();
                getDoQueryButton().setVisibility(View.VISIBLE);
                setBusy(false);
                return;
            }

            // we do not want clicks to queue, thus we 'swallow' unneeded
            // extra ones.
            setBusy(false);

            // updating GUI
            getProgressDialog().dismiss();
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
        }
    }

    /**
     * Async Task for operating database in background (DELETE operation)
     *
     * @author Andrzej Wysocki (nm108).
     */
    class SQLDeleteAsyncTask extends AsyncTask<Void[], Void, List<Product>> {
        public List<Product> doInBackground(final Void[]... params) {
            setNotYetDeleted(true);

            ArrayList<Product> result = null;

            // updating GUI
            if (!progressDialog.isShowing()) {
                getProgressDialog().show();
            }

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
                getProgressDialog().dismiss();

                return;
            }

            // updating GUI
            getOperationResultAlertDialog().show();

            getProgressDialog().dismiss();

            // we can handle clicks again
            setNotYetDeleted(false);
            setBusy(false);
            setBusyDeleting(false);
        }
    }
}

