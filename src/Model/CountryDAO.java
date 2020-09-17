package Model;

import Utils.DBConnection;
import Utils.DBQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDAO {
    private static Country countryObj;

    //Add Country to Database
    public static void addCountry(String country){
        try {
            Connection connection = DBConnection.getConnection();
            String insertCountryStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
            DBQuery.setPreparedStatement(connection,insertCountryStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1,country);
            preparedStatement.setString(2,UserDAO.getUserObj().getUsername());
            preparedStatement.setString(3,UserDAO.getUserObj().getUsername());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Update Country record on Database
    public static void updateCountry(int countryID, String country){
        try {
            Connection connection = DBConnection.getConnection();
            String updateStatement = "UPDATE country SET country = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE countryID = ? ";
            DBQuery.setPreparedStatement(connection,updateStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1,country);
            preparedStatement.setString(2,UserDAO.getUserObj().getUsername());
            preparedStatement.setInt(3,countryID);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Searches for country - If not found, this function calls the addCountry function to add the new country, and then calls itself by recursion to return the new Country's ID.
    public static int searchCountryID(String country){
        try {
            Connection connection = DBConnection.getConnection();
            String selectStatement = "SELECT * FROM country WHERE country = ?";
            DBQuery.setPreparedStatement(connection,selectStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1,country);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if(resultSet.next()) {
                countryObj = new Country();
                countryObj.setCountryID(resultSet.getInt("countryId"));
                countryObj.setCountry(resultSet.getString("country"));
                return countryObj.getCountryID();
            } else {
                addCountry(country);
                return searchCountryID(country);
            }
        } catch (SQLException e) {
            e.getMessage();
            return 0;
        }
    }

}
