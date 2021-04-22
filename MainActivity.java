package com.example.myapplication_machbase;

import androidx.appcompat.app.AppCompatActivity;
import com.machbase.jdbc.*;

import android.util.Log;
import java.sql.*;
import java.util.Properties;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static Connection connect()
    {
        Connection conn = null;
        try
        {
            String sURL = "jdbc:machbase://192.168.0.47:5656/mhdb";

            Properties sProps = new Properties();
            sProps.put("user", "SYS");
            sProps.put("password", "MANAGER");

            Class.forName("com.machbase.jdbc.MachDriver");
            conn = DriverManager.getConnection(sURL, sProps);
        }
        catch ( ClassNotFoundException ex )
        {
            System.err.println("Exception : unable to load machbase jdbc driver class");
        }
        catch ( Exception e )
        {
            System.err.println("Exception : " + e.getMessage());
        }

        return conn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(){
            public void run(){

                Connection conn = null;
                Statement stmt = null;
                String sql;

                try
                {
                    conn = connect();
                    if( conn != null ) {
                        Log.d("machbase", "machbase JDBC connected.");

                        stmt = conn.createStatement();

                        for (int i = 1; i < 10; i++) {
                            sql = "INSERT INTO SAMPLE_TABLE VALUES (";
                            sql += (i - 5) * 6552;//short
                            sql += "," + ((i - 5) * 429496728);//integer
                            sql += "," + ((i - 5) * 922337203685477580L);//long
                            sql += "," + 1.23456789 + "e" + ((i <= 5) ? "" : "+") + ((i - 5) * 7);//float
                            sql += "," + 1.23456789 + "e" + ((i <= 5) ? "" : "+") + ((i - 5) * 61);//double
                            sql += ",'id-" + i + "'";//varchar
                            sql += ",'name-" + i + "'";//text
                            sql += ",'aabbccddeeff'";//binary
                            sql += ",'192.168.0." + i + "'";//ipv4
                            sql += ",'::192.168.0." + i + "'";//ipv6
                            sql += ",TO_DATE('2014-08-0" + i + "','YYYY-MM-DD')";//dt
                            sql += ")";

                            stmt.execute(sql);

                            System.out.println(i + " record inserted.");

                        }
                    }
                }
                catch( Exception e )
                {
                    System.err.println("Exception : " + e.getMessage());
                }
                finally
                {
                    if( conn != null )
                    {
                        try {
                            conn.close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        conn = null;
                    }
                }

            }
        }.start();
    }
}