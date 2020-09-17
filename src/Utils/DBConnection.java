package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    //JDBC URL
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U05jlG?allowMultiQueries=true";
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    //Driver and Connection Interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    //Username and Password
    private static final String username = "U05jlG";
    private static final String password = null; //PLEASE EMAIL ME FOR PASSWORD


    //Start Connection
    public static void startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection) DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successful!");
        } catch(ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    //Close Database Connection
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed!");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Get Database Connection
    public static Connection getConnection() {
        return conn;
    }



}
