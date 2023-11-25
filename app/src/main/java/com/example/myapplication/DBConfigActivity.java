package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DBConfigActivity extends AppCompatActivity {

    // State

   private DBConfigSQLiteHelper sqlh =
           new DBConfigSQLiteHelper(DBConfigActivity.this);
    private Button saveConfigButton;

    private Button resetToDefaultsButton;

    private Button returnButton;

    private TextView titleTextView;

    private TextView ipAddressTextView;

    private EditText ipAddressEditText;

    private TextView portTextView;

    private EditText portEditText;

    private TextView dbNameTextView;

    private EditText dbNameEditText;

    private TextView dbInstanceTextView;

    private EditText dbInstanceEditText;

    private TextView usernameTextView;

    private EditText userNameEditText;

    private TextView passwordTextView;

    private EditText passwordEditText;

    // Accessors

    public DBConfigSQLiteHelper getSqlh() {
        return sqlh;
    }

    public void setSqlh(DBConfigSQLiteHelper sqlh) {
        this.sqlh = sqlh;
    }

    public Button getSaveConfigButton() {
        return saveConfigButton;
    }

    public void setSaveConfigButton(Button saveConfigButton) {
        this.saveConfigButton = saveConfigButton;
    }

    public Button getResetToDefaultsButton() {
        return resetToDefaultsButton;
    }

    public void setResetToDefaultsButton(Button resetToDefaultsButton) {
        this.resetToDefaultsButton = resetToDefaultsButton;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public void setReturnButton(Button returnButton) {
        this.returnButton = returnButton;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public TextView getIpAddressTextView() {
        return ipAddressTextView;
    }

    public void setIpAddressTextView(TextView ipAddressTextView) {
        this.ipAddressTextView = ipAddressTextView;
    }

    public EditText getIpAddressEditText() {
        return ipAddressEditText;
    }

    public void setIpAddressEditText(EditText ipAddressEditText) {
        this.ipAddressEditText = ipAddressEditText;
    }

    public TextView getPortTextView() {
        return portTextView;
    }

    public void setPortTextView(TextView portTextView) {
        this.portTextView = portTextView;
    }

    public EditText getPortEditText() {
        return portEditText;
    }

    public void setPortEditText(EditText portEditText) {
        this.portEditText = portEditText;
    }

    public TextView getDbNameTextView() {
        return dbNameTextView;
    }

    public void setDbNameTextView(TextView dbNameTextView) {
        this.dbNameTextView = dbNameTextView;
    }

    public EditText getDbNameEditText() {
        return dbNameEditText;
    }

    public void setDbNameEditText(EditText dbNameEditText) {
        this.dbNameEditText = dbNameEditText;
    }

    public TextView getDbInstanceTextView() {
        return dbInstanceTextView;
    }

    public void setDbInstanceTextView(TextView dbInstanceTextView) {
        this.dbInstanceTextView = dbInstanceTextView;
    }

    public EditText getDbInstanceEditText() {
        return dbInstanceEditText;
    }

    public void setDbInstanceEditText(EditText dbInstanceEditText) {
        this.dbInstanceEditText = dbInstanceEditText;
    }

    public TextView getUsernameTextView() {
        return usernameTextView;
    }

    public void setUsernameTextView(TextView usernameTextView) {
        this.usernameTextView = usernameTextView;
    }

    public EditText getUserNameEditText() {
        return userNameEditText;
    }

    public void setUserNameEditText(EditText userNameEditText) {
        this.userNameEditText = userNameEditText;
    }

    public TextView getPasswordTextView() {
        return passwordTextView;
    }

    public void setPasswordTextView(TextView passwordTextView) {
        this.passwordTextView = passwordTextView;
    }

    public EditText getPasswordEditText() {
        return passwordEditText;
    }

    public void setPasswordEditText(EditText passwordEditText) {
        this.passwordEditText = passwordEditText;
    }

    // Methods

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_db_config);

        setTitleTextView(findViewById(R.id.TitleTextView));
        setIpAddressTextView(findViewById(R.id.IPAddressTextView));
        setIpAddressEditText(findViewById(R.id.IPAddressEditText));
        setPortTextView(findViewById(R.id.PortTextView));
        setPortEditText(findViewById(R.id.PortEditText));
        setDbNameTextView(findViewById(R.id.DBNameTextView));
        setDbNameEditText(findViewById(R.id.DBNameEditText));
        setDbInstanceTextView(findViewById(R.id.DBInstanceTextView));
        setDbInstanceEditText(findViewById(R.id.DBInstanceEditText));

        getIpAddressEditText().setText(sqlh.getIpAddress());
        getPortEditText().setText(sqlh.getPort());
        getDbNameEditText().setText(sqlh.getDBName());
        getDbInstanceEditText().setText(sqlh.getDBInstance());

        setUsernameTextView(findViewById(R.id.UsernameTextView));
        setUserNameEditText(findViewById(R.id.UsernameEditText));

        setPasswordTextView(findViewById(R.id.PasswordTextView));
        setPasswordEditText(findViewById(R.id.PasswordEditText));

        getUserNameEditText().setText(sqlh.getUserName());
        getPasswordEditText().setText(sqlh.getPassword());

        setSaveConfigButton(findViewById(R.id.SaveConfigButton));
        getSaveConfigButton().setOnClickListener(
                (final View v) -> {
                    sqlh.setDbConfigValue(DBConfigActivity.this, findViewById(R.id.IPAddressEditText), DBConfigEntry.IP_ADDRESS);
                    sqlh.setDbConfigValue(DBConfigActivity.this, findViewById(R.id.PortEditText), DBConfigEntry.PORT);
                    sqlh.setDbConfigValue(DBConfigActivity.this, findViewById(R.id.DBNameEditText), DBConfigEntry.DB_NAME);
                    sqlh.setDbConfigValue(DBConfigActivity.this, findViewById(R.id.DBInstanceEditText), DBConfigEntry.DB_INSTANCE);
                    sqlh.setDbConfigValue(DBConfigActivity.this, findViewById(R.id.UsernameEditText), DBConfigEntry.USER_NAME);
                    sqlh.setDbConfigValue(DBConfigActivity.this, findViewById(R.id.PasswordEditText), DBConfigEntry.PASSWORD);
                    switchActivityToMain();
                });

        setResetToDefaultsButton(findViewById(R.id.ResetToDefaultsButton));
        getResetToDefaultsButton().setOnClickListener(
                (final View v) -> {
                    sqlh.setDbConfigToDefault(DBConfigActivity.this);

                    // restart activity
                    finish();
                    startActivity(getIntent());

                }
        );

        setReturnButton(findViewById(R.id.ReturnButton));
        getReturnButton().setOnClickListener(
                (final View v) -> {
                    switchActivityToMain();
                }
        );

    }

    private void switchActivityToMain() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }
}

