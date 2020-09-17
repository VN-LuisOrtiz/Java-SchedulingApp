package Model;

import Utils.DBConnection;
import Utils.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    //Adds all provided data about new Customer (Address, City and Country) and ties them together in the Database
    public static void addAllCustomerInfo(String customerName, String address, String address2, String city, String postalCode, String phone, String country) {
        CountryDAO.searchCountryID(country);
        CityDAO.searchCityID(city,country);
        AddressDAO.searchAddressID(address,address2,city,postalCode,phone,country);
        addCustomer(customerName,address,address2,city,postalCode,phone,country);
        System.out.println("Customer added successfully");
    }

    //Add new Customer to Database
    public static void addCustomer(String customerName, String address, String address2, String city, String postalCode, String phone, String country) {
        try {
            Connection connection = DBConnection.getConnection();
            String insertCustomerStatement = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, 1, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
            DBQuery.setPreparedStatement(connection, insertCustomerStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1, customerName);
            preparedStatement.setInt(2, AddressDAO.searchAddressID(address, address2, city, postalCode, phone, country));
            preparedStatement.setString(3, UserDAO.getUserObj().getUsername());
            preparedStatement.setString(4, UserDAO.getUserObj().getUsername());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Update Customer Name on Database
    public static void updateCustomerName(String customerName, int customerID){
        try {
            Connection connection = DBConnection.getConnection();
            String updateStatement = "UPDATE customer SET customerName = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE customerId = ? ";
            DBQuery.setPreparedStatement(connection,updateStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1,customerName);
            preparedStatement.setString(2,UserDAO.getUserObj().getUsername());
            preparedStatement.setInt(3,customerID);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Update Customer AddressID on Database
    public static void updateCustomerAddressID(int addressID, int customerID){
        try {
            Connection connection = DBConnection.getConnection();
            String updateStatement = "UPDATE customer SET addressId = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE customerId = ? ";
            DBQuery.setPreparedStatement(connection,updateStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setInt(1,addressID);
            preparedStatement.setString(2,UserDAO.getUserObj().getUsername());
            preparedStatement.setInt(3,customerID);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Delete Customer from Database
    public static void deleteCustomer(int customerID){
        try {
            Connection connection = DBConnection.getConnection();
            String deleteStatement = "DELETE FROM customer WHERE customerId = ?";
            DBQuery.setPreparedStatement(connection,deleteStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setInt(1,customerID);
            preparedStatement.execute();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Returns an ObservableList with all Customers from Database (used to populate tables)
    public static ObservableList<Customer> getAllCustomers(){
        ObservableList<Customer> customersList = FXCollections.observableArrayList();
        try {
            Connection connection = DBConnection.getConnection();
            String selectStatement = "SELECT * FROM customer" +
                    " INNER JOIN address ON customer.addressId = address.addressId" +
                    " INNER JOIN city ON address.cityId = city.cityId" +
                    " INNER JOIN country ON city.countryId = country.countryId";
            DBQuery.setPreparedStatement(connection,selectStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while(resultSet.next()){
                int customerID = resultSet.getInt("customerId");
                String customerName = resultSet.getString("customerName");
                int addressID = resultSet.getInt("addressId");
                String address = resultSet.getString("address");
                String address2 = resultSet.getString("address2");
                String postalCode = resultSet.getString("postalCode");
                String phone = resultSet.getString("phone");
                int cityID = resultSet.getInt("cityId");
                String city = resultSet.getString("city");
                int countryID = resultSet.getInt("countryId");
                String country = resultSet.getString("country");
                Customer customer = new Customer(customerID, customerName,addressID,address,address2,postalCode,phone,cityID,city,countryID,country);
                customersList.add(customer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customersList;
    }

}
