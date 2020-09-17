package Model;

import Utils.DBConnection;
import Utils.DBQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CityDAO {
    private static City cityObj;

    //Add City to Database
    public static void addCity(String city,String country){
        try {
            Connection connection = DBConnection.getConnection();
            String insertCityStatement = "INSERT INTO city(city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
            DBQuery.setPreparedStatement(connection,insertCityStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1,city);
            preparedStatement.setInt(2,CountryDAO.searchCountryID(country));
            preparedStatement.setString(3,UserDAO.getUserObj().getUsername());
            preparedStatement.setString(4,UserDAO.getUserObj().getUsername());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    //Searches for city - If not found, this function calls the addCity function to add the new city, and then calls itself by recursion to return the new City's ID.
    public static int searchCityID(String city, String country){
        try {
            Connection connection = DBConnection.getConnection();
            String selectStatement = "SELECT * FROM city WHERE city = ? AND countryId = ?";
            DBQuery.setPreparedStatement(connection,selectStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1,city);
            preparedStatement.setInt(2,CountryDAO.searchCountryID(country));
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if(resultSet.next()) {
                cityObj = new City();
                cityObj.setCityID(resultSet.getInt("cityId"));
                cityObj.setCity(resultSet.getString("city"));
                return cityObj.getCityID();
            } else {
                addCity(city, country);
                return searchCityID(city, country);
            }
        } catch (SQLException e) {
            e.getMessage();
            return 0;
        }
    }

    //Update City record on Database
    public static void updateCity(int cityID, String city){
        try {
            Connection connection = DBConnection.getConnection();
            String updateStatement = "UPDATE city SET city = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE cityId = ? ";
            DBQuery.setPreparedStatement(connection,updateStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1,city);
            preparedStatement.setString(2,UserDAO.getUserObj().getUsername());
            preparedStatement.setInt(3,cityID);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
