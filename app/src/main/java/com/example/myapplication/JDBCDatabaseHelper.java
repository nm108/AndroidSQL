package com.example.myapplication;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is just prototype, replace it with production code.
 */
public class JDBCDatabaseHelper {

    private static final String driverClass = "net.sourceforge.jtds.jdbc.Driver";


    private Connection conn = null;

    private Context c = null;

    public JDBCDatabaseHelper(final Context c) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.c = c;
    }

    public Context getContext() {
        return c;
    }

    public Connection getConnection() throws Exception {
        if ((conn != null) && (!conn.isClosed())) {
            return conn;
        };

        Class.forName(driverClass).newInstance();
        DBConfigSQLiteHelper sqlliteHelper = new DBConfigSQLiteHelper(c);

        final String connURL =
                "jdbc:jtds:sqlserver://" + sqlliteHelper.getIpAddress() +
                ":"+sqlliteHelper.getPort() +
                ";instance=" + sqlliteHelper.getDBInstance() +
                ";Database='" + sqlliteHelper.getDBName() + "'";


        conn = DriverManager.getConnection(connURL,
                sqlliteHelper.getUserName(),
                sqlliteHelper.getPassword());
        System.out.println("conn==" + conn);
        return conn;
    }


    public void doDelete(String id) throws Exception {
        validateProductId(id);

        conn = getConnection();
        PreparedStatement statement = conn.prepareStatement("DELETE FROM Products WHERE id='" + id + "';");
        statement.executeUpdate();
        conn.close();
        conn = null;
    }

    public void doInsert(String productName, int productAmount) throws Exception {
        validateProductName(productName);
        validateProductAmount(productAmount);

        conn = getConnection();

        PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO Products (ProductName, ProductQuantity)" +
                        " VALUES('" + productName + "'," + productAmount + ")");


        statement.executeUpdate();
        conn.close();
        conn = null;
    }

    public void doUpdate( String id, String newProductName, int newProductAmount) throws Exception {
        validateProductName(newProductName);
        validateProductAmount(newProductAmount);
        validateProductId(id);

        conn = getConnection();

        PreparedStatement statement = conn.prepareStatement(
                    "UPDATE Products " +
                            " SET ProductName='"+ newProductName +
                            "', ProductQuantity='" + newProductAmount +
                            "' WHERE ID='"+id+"'");
            statement.executeUpdate();
            conn.close();
            conn = null;
    }

    private static void validateProductId(String id) {
        Pattern productIdPattern = Pattern.compile("[\\p{IsDigit}]*");
        Matcher productIdMatcher = productIdPattern.matcher(id);

        if (!productIdMatcher.matches()) {
            throw new RuntimeException("Invalid input (id). Allowable characters are digits");
        }
    }

    private static void validateProductAmount(int newProductAmount) {
        Pattern productAmountPattern = Pattern.compile("[\\p{IsDigit}]*");
        Matcher productAmountMatcher = productAmountPattern.matcher(""+ newProductAmount);

        if (!productAmountMatcher.matches()) {
            throw new RuntimeException("Invalid input (amount). Allowable characters are digits");
        }
    }

    private static void validateProductName(String newProductName) {
        Pattern productNamePattern = Pattern.compile("[\\p{Alnum}\\s\\#]*");
        Matcher productNameMatcher = productNamePattern.matcher(newProductName);

        if (!productNameMatcher.matches()) {
            throw new RuntimeException("Invalid input (product name).\nAllowable characters are digits, letters and # character");
        }
    }


    // TODO: this is just prototype, replace it with code that uses database properly.
    // returned value also needs to be modified.
    public ArrayList<Product> doSelect(String queryStr) throws Exception {
        validateProductName(queryStr);

        ResultSet rs;
        ArrayList result = new ArrayList();

            conn = getConnection();
            final Statement statement = conn.createStatement();
            rs = statement.executeQuery("SELECT * From Products WHERE ProductName LIKE '%" + queryStr + "%'");

        while (rs.next()) {
            try {
                String id = rs.getString(1);
                String name = rs.getString(2);
                Integer amount = (Integer) rs.getInt(3);
                Product product = new Product(id, name, amount);

                result.add(product);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
}
