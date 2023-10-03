package com.example.myapplication;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
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

    private static final String ip = "192.168.1.103:1433";
    private static final String driverClass = "net.sourceforge.jtds.jdbc.Driver";
    private static final String dbInstance = "AWDBINSTANCE";
    private static final String db = "DBAW";
    private static final String userName = "aw108";
    private static final String password = "Firewall";

    private static final String connURL = "jdbc:jtds:sqlserver://"+ip+";instance="+dbInstance+";Database='"+db+"'";

    private Connection conn = null;

    private Context c;

    public JDBCDatabaseHelper() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public Context getContext() {
        return c;
    }
    private Connection getConnection() {
        try {
            Class.forName(driverClass).newInstance();

            conn = DriverManager.getConnection(connURL, userName, password);
            System.out.println("conn=="+conn);

        } catch (SQLException se) {
            System.out.println("1conn=="+conn);

            if (se.getMessage() != null) {
                Log.e("Error 1: ", se.getMessage());
            }

        } catch (ClassNotFoundException e) {
            System.out.println("2conn=="+conn);
            if (e.getMessage() != null) {
                Log.e("Error 3: ", e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("3conn=="+conn);
            if (e.getMessage() != null) {
                System.out.println(e);

                Log.e("Error 2: ", e.getMessage());
            }
        }

        return conn;
    }

    // TODO: this is just prototype, replace it with code that uses database properly.
    // returned value also needs to be modified.
    public List doSelect(String QueryStr) throws SQLException {
        ResultSet rs;
        List result = new ArrayList();


        try {
            final Connection conn = getConnection();
            System.out.println("conn="+conn);
            final Statement statement = conn.createStatement();
            rs = statement.executeQuery("SELECT * From Products WHERE ProductName LIKE '%" + QueryStr + "%'");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //

        while (rs.next()) {
            try {
                String str = rs.getString(2);
                Integer intc = (Integer) rs.getInt(3);
                result.add(str+": "+intc);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //System.out.println("result="+result);
        return result;// result;
    }
}
