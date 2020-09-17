package Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBQuery {

    private static PreparedStatement preparedStatement;

    //Getter and Setter for PreparedStatement
    public static void setPreparedStatement(Connection conn, String sqlStatement) {
        try {
            preparedStatement = conn.prepareStatement(sqlStatement);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static PreparedStatement getPreparedStatement(){
        return preparedStatement;
    }




}
