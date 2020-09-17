package Model;

import Utils.DBConnection;
import Utils.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class AppointmentDAO {
    private static Appointment appointmentObj;

    //Adds Appointment to Database
    public static void addAppointment(int customerID, int userID, String title, String description, String location, String contact, String type, String url, String start, String end) {
        try {
            Connection connection = DBConnection.getConnection();
            String insertAppointmentStatement = "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
            DBQuery.setPreparedStatement(connection, insertAppointmentStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setInt(1,customerID);
            preparedStatement.setInt(2,userID);
            preparedStatement.setString(3,title);
            preparedStatement.setString(4,description);
            preparedStatement.setString(5,location);
            preparedStatement.setString(6,contact);
            preparedStatement.setString(7,type);
            preparedStatement.setString(8,url);
            preparedStatement.setString(9,start);
            preparedStatement.setString(10,end);
            preparedStatement.setString(11,UserDAO.getUserObj().getUsername());
            preparedStatement.setString(12,UserDAO.getUserObj().getUsername());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Update Appointment record on Database
    public static void updateAppointment(int appointmentID, String title, String description, String location, String contact, String type, String url, String start, String end){
        try {
            Connection connection = DBConnection.getConnection();
            String updateStatement = "UPDATE appointment SET title = ?, description = ?, location = ?, contact = ?, type = ?, url = ?, start = ?, end = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE appointmentId = ? ";
            DBQuery.setPreparedStatement(connection,updateStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setString(1,title);
            preparedStatement.setString(2,description);
            preparedStatement.setString(3,location);
            preparedStatement.setString(4,contact);
            preparedStatement.setString(5,type);
            preparedStatement.setString(6,url);
            preparedStatement.setString(7,start);
            preparedStatement.setString(8,end);
            preparedStatement.setString(9,UserDAO.getUserObj().getUsername());
            preparedStatement.setInt(10,appointmentID);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Delete Appointment from Database
    public static void deleteAppointment(int appointmentID){
        try {
            Connection connection = DBConnection.getConnection();
            String deleteStatement = "DELETE FROM appointment WHERE appointmentId = ?";
            DBQuery.setPreparedStatement(connection,deleteStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setInt(1,appointmentID);
            preparedStatement.execute();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Checks if provided CustomerID is associated with an Appointment (past or future)
    public static int isCustomerAssociatedWithAppointment(int customerID){
        try {
            Connection connection = DBConnection.getConnection();
            String selectStatement = "SELECT * FROM appointment WHERE customerId = ?";
            DBQuery.setPreparedStatement(connection,selectStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.setInt(1,customerID);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if(resultSet.next()){
                appointmentObj = new Appointment();
                appointmentObj.setAppointmentID(resultSet.getInt("appointmentId"));
                return appointmentObj.getAppointmentID();
            } else
                return 0;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return 0;
        }
    }

    //Coverts localDT to GMT
    public static String localDateTimeToGMT(LocalDate date, LocalTime time){
        ZoneId userZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime userZDT = ZonedDateTime.of(date,time,userZoneId); //This has entered date in User ZDT
        Instant userToGMTInstant = userZDT.toInstant();
        ZonedDateTime userToGMTZDT = userToGMTInstant.atZone(ZoneOffset.UTC); //This has entered date in GMT
        String userToGMT = String.valueOf(userToGMTZDT.toLocalDate()) + " " + String.valueOf(userToGMTZDT.toLocalTime());
        return userToGMT;
    }

    //Coverts GMT to LocalDT
    public static String databaseTimeDateToLocal(String databaseDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
        LocalDateTime dateTime = LocalDateTime.parse(databaseDateTime,formatter);
        ZoneId databaseZoneId = ZoneId.of("UTC");
        ZonedDateTime databaseZDT = ZonedDateTime.of(dateTime.toLocalDate(),dateTime.toLocalTime(),databaseZoneId);
        Instant utcToLocalInstant = databaseZDT.toInstant();
        ZonedDateTime gmtToLocalZDT = utcToLocalInstant.atZone(ZoneId.of(TimeZone.getDefault().getID()));
        return String.valueOf(gmtToLocalZDT.toLocalDate()) + " " + String.valueOf(gmtToLocalZDT.toLocalTime());
    }

    //Returns an ObservableList with all Appointments from Database (used to populate tables)
    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        try {
            Connection connection = DBConnection.getConnection();
            String selectStatement = "SELECT * FROM appointment" +
                    " INNER JOIN customer ON appointment.customerId = customer.customerId";
            DBQuery.setPreparedStatement(connection,selectStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while(resultSet.next()){
                int appointmentID = resultSet.getInt("appointmentId");
                int customerID = resultSet.getInt("customerId");
                String customerName = resultSet.getString("customerName");
                int userID = UserDAO.getUserObj().getUserID();
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");
                String contact = resultSet.getString("contact");
                String type = resultSet.getString("type");
                String url = resultSet.getString("url");
                String start = databaseTimeDateToLocal(resultSet.getString("start"));
                String end = databaseTimeDateToLocal(resultSet.getString("end"));
                Appointment appointment = new Appointment(appointmentID,customerID,customerName,userID,title,description,location,contact,type,url,start,end);
                appointmentsList.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return appointmentsList;
    }

}
