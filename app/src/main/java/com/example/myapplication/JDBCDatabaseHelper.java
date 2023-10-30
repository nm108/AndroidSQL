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
        }
        ;


//        try {
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
//
//        } catch (SQLException se) {
//            System.out.println("1conn=="+conn);
//
//            if (se.getMessage() != null) {
//                Log.e("Error 1: ", se.getMessage());
//            }
//
//        } catch (ClassNotFoundException e) {
//            System.out.println("2conn=="+conn);
//            if (e.getMessage() != null) {
//                Log.e("Error 3: ", e.getMessage());
//            }

//        } catch (Exception e) {
//            System.out.println("3conn=="+conn);
//            if (e.getMessage() != null) {
//                System.out.println(e);
//
//                Log.e("Error 2: ", e.getMessage());
//            }
//        }

        return conn;
    }


    public void doDelete(String id) throws Exception {

//        try {
        conn = getConnection();
        PreparedStatement statement = conn.prepareStatement("DELETE FROM Products WHERE id='" + id + "';");
        statement.executeUpdate();
//        } catch (NullPointerException npe ) {
//            throw new RuntimeException(npe);
//        }
//conn.commit();
        conn.close();
        conn = null;
    }

    public void doInsert(String productName, int productAmount) throws Exception {
        conn = getConnection();
//        try {
        PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO Products (ProductName, ProductQuantity)" +
                        " VALUES('" + productName + "'," + productAmount + ")");


        statement.executeUpdate();
        conn.close();
        conn = null;

//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void doUpdate( String id, String newProductName, int newProductAmount) throws Exception {

//        Log.d("Update", "doUpdate: "+id+","+newProductName+","+newProductAmount);

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


    // TODO: this is just prototype, replace it with code that uses database properly.
    // returned value also needs to be modified.
    public ArrayList<Product> doSelect(String queryStr) throws Exception {
        ResultSet rs;
        ArrayList result = new ArrayList();



            conn = getConnection();
            final Statement statement = conn.createStatement();
            rs = statement.executeQuery("SELECT * From Products WHERE ProductName LIKE '%" + queryStr + "%'");


        //

        while (rs.next()) {
            try {
                String id = rs.getString(1);
                String name = rs.getString(2);
                Integer amount = (Integer) rs.getInt(3);
                Product product = new Product(id, name, amount);
//                result.add(str);
                result.add(product);
//                System.out.println("prod="+product.toString());
//                result.add(str+":"+intc);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //System.out.println("result="+result);
//        conn.close();
//        conn = null;
        return result;// result;
    }
}
