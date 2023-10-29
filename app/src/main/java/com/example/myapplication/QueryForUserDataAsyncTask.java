
package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Asynchronous task for sending query for user data to database in background.
 * After query execution, message with query results is displayed on Android Device.
 *
 * @author Andrzej Wysocki
 */
public class QueryForUserDataAsyncTask extends AsyncTask <Void,Void,Void> {

    /* State */

    private final Context c;
    String messageToDisplay;

    private final String messageTitleLabel = "Message";
    final String userName;
    private ProgressDialog progressDialog;

    private final String progressDialogMessage = "Please Wait";
    private final String okLabel = "OK";

    private ArrayList<Product> al = new ArrayList<Product>();


    private SelectActivity sa;

    /* Constructor */
    public QueryForUserDataAsyncTask(Context context, String n, SelectActivity sa) {
        this.c=context;
        this.userName=n;
        this.sa = sa;

    }

    /* Methods */

    /**
     * Sends query to database and sets message to display in this object's state.
     *
     * @author Andrzej Wysocki
     *
     * @return null
     */

    protected Void doInBackground(Void... voids) {
        try {

            JDBCDatabaseHelper jdbcdbh = new JDBCDatabaseHelper(c);
            ArrayList<Product> al = jdbcdbh.doSelect(userName);

//            final Connection conn = jdbcdbh.getConnection();
//            final Statement statement = conn.createStatement();
//            final ResultSet rs = statement.executeQuery("SELECT count(*) From Products");
//            if (rs.next()) {
//                messageToDisplay="Username: "+ userName +" exists with ID: "+rs.getString(2);
//            } else {
//                messageToDisplay="User not found with username: "+ userName;
//            }
        } catch (Exception e) {
            messageToDisplay = e.getMessage();
        }
        return null;
    }

    /**
     * Shows progress dialog before query execution.
     *
     * @author Andrzej Wysocki
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        sa.getDoSelectQueryButton().setClickable(false);
//
//        progressDialog = new ProgressDialog(c);
//        progressDialog.setMessage(progressDialogMessage);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setCancelable(false);
//        progressDialog.show();
    }

    /**
     * After query execution, dismissess progress dialog and displays message with
     * query results.
     *
     * @author Andrzej Wysocki
     *
     * @param aVoid The result of the operation computed by {@link #doInBackground}.
     *
     */
    @Override
    protected  void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        sa.getDoSelectQueryButton().setClickable(true);
//        progressDialog.dismiss();
//        AlertDialog alertDialog = new AlertDialog.Builder(c).create();
//        alertDialog.setTitle(messageTitleLabel);
//        alertDialog.setMessage(messageToDisplay);
//        alertDialog.setButton(
//                AlertDialog.BUTTON_NEUTRAL, (CharSequence) okLabel,
//                (DialogInterface.OnClickListener) (dialog, which) -> {
//                    dialog.dismiss(); });
//        alertDialog.show();
    };

}