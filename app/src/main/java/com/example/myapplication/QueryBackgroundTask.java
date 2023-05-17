package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryBackgroundTask extends AsyncTask <Void,Void,Void> {
    final Context c;
    String messageToDisplay;
    final String userName;
    ProgressDialog progressDialog;

    final String okLabel = "OK";

    public QueryBackgroundTask(Context context, String n) {
        this.c=context;
        this.userName=n;
    }

    protected Void doInBackground(Void... voids) {
        try {
            Connection conn = (new DatabaseConnection()).getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * From USERS where UserName='"+ userName +"'");
            if (rs.next()) {
                messageToDisplay="Username: "+ userName +" exists with ID: "+rs.getString(2);
            } else {
                messageToDisplay="User not found with username: "+ userName;
            }
        } catch (Exception e) {
            messageToDisplay = e.getMessage();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(c);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected  void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        AlertDialog alertDialog = new AlertDialog.Builder(c).create();
        alertDialog.setTitle("Message");
        alertDialog.setMessage(messageToDisplay);
        alertDialog.setButton(

                AlertDialog.BUTTON_NEUTRAL, (CharSequence) okLabel,
                    (DialogInterface.OnClickListener) (dialog, which) -> {
                        dialog.dismiss(); });
        alertDialog.show();
    };

}

