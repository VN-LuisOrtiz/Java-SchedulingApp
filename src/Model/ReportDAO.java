package Model;

import Utils.DBConnection;
import Utils.DBQuery;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReportDAO {

    //Set names of files for reports.
    private static String appointmentTypesByMonthFile = "appointmentTypesByMonthFile.txt";
    private static String userScheduleFile = "userScheduleFile.txt";
    private static String customersByCountryFile = "customersByCountryFile.txt";

    //Overloaded Function - Generates Appointment Types By Month Report
    public static void writeToFile(String filename, String appointments, String type, String month, String year, Boolean first) throws IOException{
        FileWriter fileWriter = new FileWriter(filename,true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        if(first)
            outputFile.println("--------------------------------------Generated on "+ LocalDateTime.now()+"--------------------------------------");
        outputFile.printf("%-20s %-5s %-5s %-15s %-8s %-15s %-10s %-4s\n","Number of Appointments:",appointments,"Type:",type,"Month:",month,"Year:",year);
        outputFile.close();
    }

    //Overloaded Function - Generates User Schedule Report
    public static void writeToFile(String filename, String userName, int appointmentID, String customerName, String start, String end, Boolean first) throws IOException{
        FileWriter fileWriter = new FileWriter(filename,true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        if(first)
            outputFile.println("--------------------------------------Generated on "+ LocalDateTime.now()+"--------------------------------------");
        outputFile.printf("%-10s %-10s %-10s %-5s %-15s %-15s %-5s %-20s %-5s %-20s\n","Consultant: ",userName,"AppointmentID:",appointmentID,"Customer Name:",customerName,"Start:",start,"End:",end);
        outputFile.close();
    }

    //Overloaded Function - Generates Number of Customers by Country Report
    public static void writeToFile(String filename, String numberOfCustomers, String country, Boolean first) throws IOException{
        FileWriter fileWriter = new FileWriter(filename,true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        if(first)
            outputFile.println("--------------------------------------Generated on "+ LocalDateTime.now()+"--------------------------------------");
        outputFile.printf("%-20s %-5s %-10s %-10s\n","Number of Customers: ",numberOfCustomers,"Country:",country);
        outputFile.close();
    }

    //Function called when trying to generate the Appointment Types By Month Report
    public static void appointmentTypesByMonth(){
        try {
            Connection connection = DBConnection.getConnection();
            Boolean first=false;
            String selectStatement = "SELECT COUNT(type), type, MONTHNAME(start),YEAR(start) FROM appointment GROUP BY MONTH(start),type";
            DBQuery.setPreparedStatement(connection,selectStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while(resultSet.next()){
                if(resultSet.isFirst())
                    first=true;
                else
                    first=false;
                String appointments = resultSet.getString("COUNT(type)");
                String type = resultSet.getString("type");
                String month = resultSet.getString("MONTHNAME(start)");
                String year = resultSet.getString("YEAR(start)");
                try {
                    writeToFile(appointmentTypesByMonthFile,appointments,type,month,year,first);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    //Function called when trying to generate the user Schedule Report
    public static void usersScheduleReport(){
        try {
            Connection connection = DBConnection.getConnection();
            Boolean first=false;
            String selectStatement = "SELECT username, appointmentId, customerName, start, end  " +
                                     "FROM appointment " +
                                     "INNER JOIN customer " +
                                     "ON appointment.customerId = customer.customerId " +
                                     "INNER JOIN user " +
                                     "ON appointment.userId = user.userId " +
                                     "GROUP BY start;";
            DBQuery.setPreparedStatement(connection,selectStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while(resultSet.next()){
                if(resultSet.isFirst())
                    first=true;
                else
                    first=false;
                String username = resultSet.getString("username");
                int appointmentID = resultSet.getInt("appointmentId");
                String customerName = resultSet.getString("customerName");
                String start = resultSet.getString("start");
                String end = resultSet.getString("end");
                try {
                    writeToFile(userScheduleFile,username,appointmentID,customerName,start,end,first);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    //Function called when trying to generate the Number of Customers by Country Report
    public static void customersByCountryReport(){
        try {
            Connection connection = DBConnection.getConnection();
            Boolean first=false;
            String selectStatement = "SELECT COUNT(customerId), country " +
                    "FROM customer " +
                    "INNER JOIN address " +
                    "ON customer.addressId = address.addressId " +
                    "INNER JOIN city " +
                    "ON address.cityId = city.cityId " +
                    "INNER JOIN country " +
                    "ON city.countryId = country.countryId " +
                    "GROUP BY country;";
            DBQuery.setPreparedStatement(connection,selectStatement);
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while(resultSet.next()){
                if(resultSet.isFirst())
                    first=true;
                else
                    first=false;
                String numberOfCustomers = resultSet.getString("COUNT(customerId)");
                String country = resultSet.getString("country");
                try {
                    writeToFile(customersByCountryFile,numberOfCustomers,country,first);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}
