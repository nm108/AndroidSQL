package com.example.myapplication;

import android.content.Context;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is just prototype, replace it with production code.
 */
public class JDBCDatabaseHelper {

    private static final String driverClass = "net.sourceforge.jtds.jdbc.Driver";
    public static final String SQL_CONNURL_JDBC_JTDS_SQLSERVER_PREFIX_LABEL = "jdbc:jtds:sqlserver://";
    public static final String IP_PORT_SEPARATOR_STRING_LABEL = ":";
    public static final String INSTANCE_LABEL = ";instance=";
    public static final String DATABASE_LABEL = ";Database='";
    public static final String SQL_CONNURL_TERMINATOR = "'";
    public static final String DELETE_FROM_PRODUCTS_WHERE_ID_LABEL = "DELETE FROM Products WHERE id='";
    public static final String SQL_DELETESTRING_TERMINATOR = "';";
    public static final String INSERT_INTO_PRODUCTS_PRODUCT_NAME_PRODUCT_QUANTITY_LABEL = "INSERT INTO Products (ProductName, ProductQuantity)";
    public static final String VALUES_LABEL = " VALUES('";
    public static final String INSERT_SEPARATOR = "',";
    public static final String SQL_INSERTSTRING_TERMINATOR = ")";
    public static final String SQL_UPDATE_PRODUCTS_LABEL = "UPDATE Products ";
    public static final String SQL_UPDATE_SET_PRODUCT_NAME_LABEL = " SET ProductName='";
    public static final String SQL_UPDATE_PRODUCT_QUANTITY_LABEL = "', ProductQuantity='";
    public static final String SQL_UPDATE_WHERE_ID_LABEL = "' WHERE ID='";
    public static final String SQL_UPDATESTRING_TERMINATOR = "'";
    public static final String IS_DIGIT_PRODUCTID_REGEX = "[\\p{IsDigit}]*";
    public static final String EXCEPTION_STRING_PRODUCT_ID = "Invalid input (id). Allowable characters are digits";
    public static final String IS_DIGIT_PRODUCTAMOUNT_REGEX = "[\\p{IsDigit}]*";
    public static final String EXCEPTION_STRING_PRODUCT_AMOUNT = "Invalid input (amount). Allowable characters are digits";
    public static final String IS_ALNUM_PRODUCTNAME_REGEX = "[\\p{Alnum}\\s\\#]*";
    public static final String EXCEPTION_STRING_PRODUCT_NAME = "Invalid input (product name).\n\nAllowable characters are digits, letters and # character";
    public static final String SQL_SELECT_PREFIX = "SELECT * From Products WHERE ProductName LIKE '%";
    public static final String SQL_SELECT_POSTFIX = "%'";


    private Connection conn = null;

    private Context context = null;

    public JDBCDatabaseHelper(final Context c) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.context = c;
    }

    public Context getContext() {
        return context;
    }

    public Connection getConnection() throws Exception {
        if ((conn != null) && (!conn.isClosed())) {
            return conn;
        };

        Class.forName(driverClass).newInstance();
        DBConfigSQLiteHelper sqlliteHelper = new DBConfigSQLiteHelper(context);

        final String connURL =
                SQL_CONNURL_JDBC_JTDS_SQLSERVER_PREFIX_LABEL +
                        sqlliteHelper.getIpAddress() +
                        IP_PORT_SEPARATOR_STRING_LABEL +sqlliteHelper.getPort() +
                        INSTANCE_LABEL + sqlliteHelper.getDBInstance() +
                        DATABASE_LABEL + sqlliteHelper.getDBName() + SQL_CONNURL_TERMINATOR;

        conn = DriverManager.getConnection(connURL,
                sqlliteHelper.getUserName(),
                sqlliteHelper.getPassword());
        return conn;
    }


    public void doDelete(String id) throws Exception {
        validateProductId(id);

        conn = getConnection();
        PreparedStatement statement =
                conn.prepareStatement(DELETE_FROM_PRODUCTS_WHERE_ID_LABEL + id +
                        SQL_DELETESTRING_TERMINATOR);
        statement.executeUpdate();
        conn.close();
        conn = null;
    }

    public void doInsert(String productName, int productAmount) throws Exception {
        validateProductName(productName);
        validateProductAmount(productAmount);

        conn = getConnection();

        PreparedStatement statement = conn.prepareStatement(
                INSERT_INTO_PRODUCTS_PRODUCT_NAME_PRODUCT_QUANTITY_LABEL +
                        VALUES_LABEL + productName + INSERT_SEPARATOR +
                        productAmount + SQL_INSERTSTRING_TERMINATOR);


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
                    SQL_UPDATE_PRODUCTS_LABEL +
                            SQL_UPDATE_SET_PRODUCT_NAME_LABEL + newProductName +
                            SQL_UPDATE_PRODUCT_QUANTITY_LABEL + newProductAmount +
                            SQL_UPDATE_WHERE_ID_LABEL +id+
                            SQL_UPDATESTRING_TERMINATOR);
            statement.executeUpdate();
            conn.close();
            conn = null;
    }

    private static void validateProductId(String id) {
        Pattern productIdPattern = Pattern.compile(IS_DIGIT_PRODUCTID_REGEX);
        Matcher productIdMatcher = productIdPattern.matcher(id);

        if (!productIdMatcher.matches()) {
            throw new RuntimeException(EXCEPTION_STRING_PRODUCT_ID);
        }
    }

    private static void validateProductAmount(int newProductAmount) {
        Pattern productAmountPattern = Pattern.compile(IS_DIGIT_PRODUCTAMOUNT_REGEX);
        Matcher productAmountMatcher = productAmountPattern.matcher(""+ newProductAmount);

        if (!productAmountMatcher.matches()) {
            throw new RuntimeException(EXCEPTION_STRING_PRODUCT_AMOUNT);
        }
    }

    private static void validateProductName(String newProductName) {
        Pattern productNamePattern = Pattern.compile(IS_ALNUM_PRODUCTNAME_REGEX);
        Matcher productNameMatcher = productNamePattern.matcher(newProductName);

        if (!productNameMatcher.matches()) {
            throw new RuntimeException(EXCEPTION_STRING_PRODUCT_NAME);
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
            rs = statement.executeQuery(
                    SQL_SELECT_PREFIX + queryStr + SQL_SELECT_POSTFIX);

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
