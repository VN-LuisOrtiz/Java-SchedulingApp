package Model;

import Utils.DBConnection;
import Utils.DBQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressDAO {
    private static Address addressObj;

    //Add Address to Customer
    public static void addAddress(String address, String address2, String postalCode, String phone, String city, String country){
        try {
            Connection connection = DBConnection.getConnection();
            String insertAddressStatement = "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?,?,?,?,?,CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP,?)";
            DBQuery.setPreparedStatement(connection,insertAddressStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1,address);
            preparedStatement.setString(2,address2);
            preparedStatement.setInt(3,CityDAO.searchCityID(city, country));
            preparedStatement.setString(4,postalCode);
            preparedStatement.setString(5,phone);
            preparedStatement.setString(6,UserDAO.getUserObj().getUsername());
            preparedStatement.setString(7,UserDAO.getUserObj().getUsername());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Searches for Address - If not found, this function calls the addAddress function to add the new address, and then calls itself by recursion to return the new Address' ID.
    public static int searchAddressID(String address, String address2, String city, String postalCode, String phone, String country){
        try {
            Connection connection = DBConnection.getConnection();
            String selectStatement = "SELECT * FROM address WHERE address = ? AND address2 = ? AND cityId = ? AND postalCode = ? AND phone = ?";
            DBQuery.setPreparedStatement(connection,selectStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1,address);
            preparedStatement.setString(2,address2);
            preparedStatement.setInt(3,CityDAO.searchCityID(city,country));
            preparedStatement.setString(4,postalCode);
            preparedStatement.setString(5,phone);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if(resultSet.next()) {
                addressObj = new Address();
                addressObj.setAddressID(resultSet.getInt("addressId"));
                addressObj.setAddress(resultSet.getString("address"));
                addressObj.setAddress2(resultSet.getString("address2"));
                addressObj.setCityID(resultSet.getInt("cityId"));
                addressObj.setPostalCode(resultSet.getString("postalCode"));
                addressObj.setPhone(resultSet.getString("phone"));
                return addressObj.getAddressID();
            } else {
                addAddress(address, address2, postalCode, phone,city, country);
                return searchAddressID(address, address2,city, postalCode, phone, country);
            }
        } catch (SQLException e) {
            e.getMessage();
            return 0;
        }
    }

    //Update Address data on Database
    public static void updateAddress(int addressID, String address, String address2, String postalCode, String phone){
        try {
            Connection connection = DBConnection.getConnection();
            String updateStatement = "UPDATE address SET address = ?, address2 = ?, postalCode = ?, phone = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE addressId = ? ";
            DBQuery.setPreparedStatement(connection,updateStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1,address);
            preparedStatement.setString(2,address2);
            preparedStatement.setString(3,postalCode);
            preparedStatement.setString(4,phone);
            preparedStatement.setString(5,UserDAO.getUserObj().getUsername());
            preparedStatement.setInt(6,addressID);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Update Address CityID on City
    public static void updateAddressCityID(int cityID, int addressID){
        try {
            Connection connection = DBConnection.getConnection();
            String updateStatement = "UPDATE address SET cityId = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE addressId = ? ";
            DBQuery.setPreparedStatement(connection,updateStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setInt(1,cityID);
            preparedStatement.setString(2,UserDAO.getUserObj().getUsername());
            preparedStatement.setInt(3,addressID);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
