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

    /* Constants */

    public static final String PLEASE_WAIT_LABEL = "Please Wait.";
    public static final String OK_LABEL = "Ok";
    public static final String ORIGINAL_PRODUCT_ID_LABEL = "Original Product id: ";
    public static final String ORIGINAL_PRODUCT_NAME_LABEL = "Original Product name: ";
    public static final String ORIGINAL_PRODUCT_QUANTITY_LABEL = "Original Product quantity: ";
    public static final String PRODUCT_UPDATED_LABEL = "Product Updated.";

    /* State */

    private Button doUpdateQueryButton;

    private Button doUpdateButton;

    private AlertDialog exceptionAlertDialog;

    private EditText queryEditText;

    private TextView originalProductTextView;

    private Button returnButton;

    private EditText newProductNameEditText;

    private EditText newProductAmountEditText;

    private Context context;

    private boolean error;

    private ProgressDialog progressDialog;

    private ListView productsListView;

    AlertDialog operationResultAlertDialog;

    private Product product;

    private boolean selecting;

    private boolean updating;

    /* Accessors */

    public boolean isSelecting() {
        return selecting;
    }

    public void setSelecting(boolean selecting) {
        this.selecting = selecting;
    }

    public boolean isUpdating() {
        return updating;
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

    public Button getDoUpdateQueryButton() {
        return doUpdateQueryButton;
    }

    public void setDoUpdateQueryButton(Button doUpdateQueryButton) {
        this.doUpdateQueryButton = doUpdateQueryButton;
    }

    public Button getDoUpdateButton() {
        return doUpdateButton;
    }

    public void setDoUpdateButton(Button doUpdateButton) {
        this.doUpdateButton = doUpdateButton;
    }

    public AlertDialog getExceptionAlertDialog() {
        return exceptionAlertDialog;
    }

    public void setExceptionAlertDialog(AlertDialog exceptionAlertDialog) {
        this.exceptionAlertDialog = exceptionAlertDialog;
    }

    public EditText getQueryEditText() {
        return queryEditText;
    }

    public void setQueryEditText(EditText queryEditText) {
        this.queryEditText = queryEditText;
    }

    public TextView getOriginalProductTextView() {
        return originalProductTextView;
    }

    public void setOriginalProductTextView(TextView originalProductTextView) {
        this.originalProductTextView = originalProductTextView;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public void setReturnButton(Button returnButton) {
        this.returnButton = returnButton;
    }

    public EditText getNewProductNameEditText() {
        return newProductNameEditText;
    }

    public void setNewProductNameEditText(EditText newProductNameEditText) {
        this.newProductNameEditText = newProductNameEditText;
    }

    public EditText getNewProductAmountEditText() {
        return newProductAmountEditText;
    }

    public void setNewProductAmountEditText(EditText newProductAmountEditText) {
        this.newProductAmountEditText = newProductAmountEditText;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
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

    public AlertDialog getOperationResultAlertDialog() {
        return operationResultAlertDialog;
    }

    public void setOperationResultAlertDialog(AlertDialog operationResultAlertDialog) {
        this.operationResultAlertDialog = operationResultAlertDialog;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    /* Methods */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        prepareView();
        prepareProgressDialog();
        prepareOperationResultAlertDialog();
        prepareExceptionAlertDialog();

    }

    private void prepareView() {
        setContentView(R.layout.activity_update);

        queryEditText = findViewById(R.id.QueryEditText);

        productsListView = findViewById(R.id.LV);

        newProductNameEditText = findViewById(R.id.NewProductNameEditText);
        newProductNameEditText.setVisibility(View.GONE);
        newProductAmountEditText = findViewById(R.id.NewProductAmountEditText);
        newProductAmountEditText.setVisibility(View.GONE);

        originalProductTextView = findViewById(R.id.originalProductTextView);
        originalProductTextView.setVisibility(View.GONE);

        doUpdateButton = findViewById(R.id.DoUpdateButton);
        doUpdateButton.setVisibility(View.GONE);
        doUpdateButton.setOnClickListener(
                (final View v) -> {

                    progressDialog.show();
                    SQLUpdateAsyncTask sqlUpdateTask = new SQLUpdateAsyncTask();
                    sqlUpdateTask.execute();
                }
        );

        doUpdateQueryButton = findViewById(R.id.DoUpdateQueryButton);
        doUpdateQueryButton.setOnClickListener(
                (final View v) -> {
                    getProgressDialog().show();
                    populateProductsListView();
                }
        );

        returnButton = findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(
                (final View v) -> {
                    switchActivityToMain();
                }
        );
    }

    private void prepareExceptionAlertDialog() {
        exceptionAlertDialog = new AlertDialog.Builder(this).create();
        exceptionAlertDialog.setCancelable(false);
        exceptionAlertDialog.setCanceledOnTouchOutside(false);

        exceptionAlertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, (CharSequence) OK_LABEL,
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    error = false;
                    dialog.dismiss();
                    switchActivityToMain();
                }
        );
    }

    private void prepareOperationResultAlertDialog() {
        operationResultAlertDialog = new AlertDialog.Builder(this).create();
        operationResultAlertDialog.setCancelable(false);
        operationResultAlertDialog.setCanceledOnTouchOutside(false);

        operationResultAlertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, (CharSequence) OK_LABEL,
                (DialogInterface.OnClickListener) (dialog, which) ->
                    {
                        switchGUIToProductsList();
                        populateProductsListView();

                        setUpdating(false);
                        updateProgressDialog();
                    });
    }

    private void prepareProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(PLEASE_WAIT_LABEL);
    }

    private void switchGUIToProductsList() {
        queryEditText.setVisibility(View.VISIBLE);
        originalProductTextView.setVisibility(View.GONE);
        doUpdateButton.setVisibility(View.GONE);
        newProductNameEditText.setVisibility(View.GONE);
        newProductAmountEditText.setVisibility(View.GONE);
        originalProductTextView.setVisibility(View.GONE);
        productsListView.setVisibility(View.VISIBLE);
        doUpdateQueryButton.setVisibility(View.VISIBLE);
    }

    private void switchGUIToEditForm() {
        originalProductTextView.setVisibility(View.VISIBLE);
        originalProductTextView.setText(ORIGINAL_PRODUCT_ID_LABEL +
                product.getId()+"\n" +
                ORIGINAL_PRODUCT_NAME_LABEL +
                product.getName()+"\n" +
                ORIGINAL_PRODUCT_QUANTITY_LABEL +
                product.getAmount());
        queryEditText.setVisibility(View.GONE);
        productsListView.setVisibility(View.GONE);
        doUpdateQueryButton.setVisibility(View.GONE);
        newProductNameEditText.setVisibility(View.VISIBLE);
        newProductAmountEditText.setVisibility(View.VISIBLE);
        doUpdateButton.setVisibility(View.VISIBLE);
    }

    private void switchActivityToMain() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    public Button getDoSelectQueryButton() {
        return getDoSelectQueryButton();
    }


    void updateProgressDialog () {
        if (isSelecting() || isUpdating()) {
            if (!getProgressDialog().isShowing()) {
                getProgressDialog().show();
            }
        } else {
            if (getProgressDialog().isShowing()) {
                getProgressDialog().dismiss();
            }
        }
    }

    private void populateProductsListView() {
        setSelecting(true);
        updateProgressDialog();

        SQLSelectAsyncTask sTask = new SQLSelectAsyncTask();
        sTask.execute();

        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {

                product = (Product) parent.getItemAtPosition(position);

                switchGUIToEditForm();
            }
        });
    }


        class SQLSelectAsyncTask extends AsyncTask<Void[], Void, ArrayList<Product>> {

            public ArrayList<Product> doInBackground(Void[]... params) {
                ArrayList<Product> result = null;
                // Updating GUI
                setSelecting(true);
                updateProgressDialog();

                JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(context);

                try {
                    result = jdbcDatabaseHelper.doSelect(queryEditText.getText().toString());
                } catch (Exception e) {
                    exceptionAlertDialog.setMessage(e.toString());
                    return null;
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<Product> products) {
                super.onPostExecute(products);
                if (products == null) {
                    exceptionAlertDialog.show();


                    return;
                }

                ProductAdapter pa = new ProductAdapter(context, products);
                productsListView.setAdapter(pa);

                // Updating GUI
                setSelecting(false);
                updateProgressDialog();

            }
        }

        class SQLUpdateAsyncTask extends AsyncTask<Void[], Void, ArrayList<Product>> {

            public ArrayList<Product> doInBackground(Void[]... params) {
                // Updating GUI
                setUpdating(true);
                updateProgressDialog();

                JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper(context);
                String newProductName = newProductNameEditText.getText().toString();
                if (newProductName.trim().equals("")) {
                    newProductName = "!!";
                }
                int newProductAmount;
                try {
                    newProductAmount = Integer.parseInt(
                            newProductAmountEditText.getText().toString()
                    );
                } catch (NumberFormatException e) {
                    newProductAmount = -1;
                }

                try {
                    jdbcDatabaseHelper.doUpdate(product.getId(),
                            newProductName, newProductAmount);
                } catch (Exception e) {
                    exceptionAlertDialog.setMessage(e.toString());
                    error = true;
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Product> products) {
                super.onPostExecute(products);
                if (error) {
                    exceptionAlertDialog.show();

                    progressDialog.dismiss();
                    error = false;
                    return;
                }

                operationResultAlertDialog.setMessage(PRODUCT_UPDATED_LABEL);

                getOperationResultAlertDialog().show();
            }
        }


};

