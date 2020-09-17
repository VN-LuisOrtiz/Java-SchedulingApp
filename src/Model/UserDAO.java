package Model;

import Utils.DBConnection;
import Utils.DBQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static User userObj;

    //Validates the Username and Password according to the User's data on the Database
    public static boolean validateLogin(String username, String password){
        try {
            Connection connection = DBConnection.getConnection();
            String selectStatement = "SELECT * FROM user WHERE userName = ? AND password = ?";
            DBQuery.setPreparedStatement(connection,selectStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if(resultSet.next()) {
                userObj = new User();
                userObj.setUserID(resultSet.getInt("userId"));
                userObj.setUsername(resultSet.getString("userName"));
                userObj.setPassword(resultSet.getString("password"));
                if(userObj.getUsername().equals(username) && userObj.getPassword().equals(password))
                    return true;
                else
                    return false;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.getMessage();
            return false;
        }
    }

    //Returns the current user
    public static User getUserObj(){
        return userObj;
    }

}
