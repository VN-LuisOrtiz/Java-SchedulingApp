package View_Controller;

import Model.Appointment;
import Model.AppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class MainController implements Initializable {


    //GUI Objects
    @FXML
    private RadioButton weeklyViewButton;
    @FXML
    private RadioButton monthlyViewButton;
    @FXML
    private RadioButton allAppointmentsButton;
    @FXML
    private DatePicker selectDate;
    @FXML
    private TableColumn startColumn;
    @FXML
    private TableColumn endColumn;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
    @FXML
    private ObservableList<Appointment> appointmentsFilteredList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Appointment,String> apptIDColumn;
    @FXML
    private TableColumn<Appointment,String> customerColumn;
    @FXML
    private TableColumn<Appointment,String> titleColumn;
    @FXML
    private TableColumn<Appointment,String> descColumn;
    @FXML
    private TableColumn<Appointment,String> locationColumn;
    @FXML
    private Label timeZoneLabel;

    //Interface for Lambda Expression
    public interface updateDateInterface{

        //Void Abstract Method
        void updateAppointmentList(LocalDate date);

    }

    //Initialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectDate.setValue(LocalDate.now()); //Set the Date to current Date
        timeZoneLabel.setText("TimeZone: " + TimeZone.getDefault().getID()); //Display User's Timezone
        showData(); //Show the Appointments Data every time user comes to the Main Screen
        readAppointmentsForReminder(); //Read the Appointments Date (used to check for reminders)
    }


    //Set columns to Appointments Data from Database
    private void showData() {
        apptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentsList.setAll(AppointmentDAO.getAllAppointments());
        appointmentTable.setItems(appointmentsList);
        appointmentTable.refresh();
    }

    //Read all Appointments Data
    private void readAppointmentsForReminder() {
        for(int i=0;i<appointmentTable.getItems().size();i++) {
            LocalDate startDate = LocalDate.parse(appointmentTable.getItems().get(i).getStart().substring(0,10));
            if(LocalDate.now().equals(startDate)) {
                LocalTime startTime = LocalTime.parse(appointmentTable.getItems().get(i).getStart().substring(11,16));
                LocalTime currentTime = LocalTime.now();
                long timeDifference = ChronoUnit.MINUTES.between(startTime,currentTime) * -1; //Multiplied by -1 to turn it into a positive number
                if(timeDifference>=0 && timeDifference<=15)
                    displayAppointmentReminder(appointmentTable.getItems().get(i).getCustomerName(),timeDifference); //If there is an upcoming appointment, remind the user.
            }
        }
    }

    //Read all Appointments Data - Filters by Week
    private void readAppointmentsForFilteringByWeek(LocalDate currentDate) {
        appointmentsFilteredList.clear();
        LocalDate weekMonday = currentDate.with(DayOfWeek.MONDAY);
        LocalDate weekSunday = currentDate.with(DayOfWeek.SUNDAY);
        for(int i=0;i<appointmentsList.size();i++) {
            LocalDate startDate = LocalDate.parse(appointmentsList.get(i).getStart().substring(0,10));
            if(startDate.isAfter(weekMonday) && startDate.isBefore(weekSunday)) {
                appointmentsFilteredList.add(appointmentsList.get(i));
            }
        }
        appointmentTable.setItems(appointmentsFilteredList);
        appointmentTable.refresh();
    }

    //Read all Appointments Data - Filters by Month
    private void readAppointmentsForFilteringByMonth(LocalDate currentDate) {
        appointmentsFilteredList.clear();
        Month currentMonth = currentDate.getMonth();
        for(int i=0;i<appointmentsList.size();i++) {
            LocalDate startDate = LocalDate.parse(appointmentsList.get(i).getStart().substring(0,10));
            if(startDate.getMonth().equals(currentMonth) && startDate.getYear()==currentDate.getYear()) {
                appointmentsFilteredList.add(appointmentsList.get(i));
            }
        }
        appointmentTable.setItems(appointmentsFilteredList);
        appointmentTable.refresh();
    }



    //Lambda Expression used to display an appointment reminder - Expression used to make the code shorter and easier to read/understand
    public static void displayAppointmentReminder(String customerName, long minutes) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("REMINDER");
        window.setMinWidth(300);
        Label message = new Label();
        message.setText("You have an appointment with " + customerName + " in approximately " + minutes + " minute(s)");
        Button close = new Button("Got it!");
        close.setOnMouseClicked(e -> {
            window.close();
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(message,close);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }


    //Updates the selected date
    @FXML
    private void datepickerUpdate(ActionEvent actionEvent) {
        if(weeklyViewButton.isSelected()){
            readAppointmentsForFilteringByWeek(selectDate.getValue());
        }

        if(monthlyViewButton.isSelected()){
            readAppointmentsForFilteringByMonth(selectDate.getValue());
        }

    }

    //Sends user back to LogIn Screen
    @FXML
    private void logOut(MouseEvent mouseEvent) throws IOException {
        Parent logInScreenParent = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        Scene logInScreenScene = new Scene(logInScreenParent);
        Stage logInScreenWindow = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        logInScreenWindow.setScene(logInScreenScene);
        logInScreenWindow.show();
    }

    //Move to Add Appointment Screen
    @FXML
    private void addAppointment(MouseEvent mouseEvent) throws IOException {
        Parent addAppointmentScreenParent = FXMLLoader.load(getClass().getResource("AddAppointmentScreen.fxml"));
        Scene addAppointmentScreenScene = new Scene(addAppointmentScreenParent);
        Stage addAppointmentScreenWindow = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        addAppointmentScreenWindow.setScene(addAppointmentScreenScene);
        addAppointmentScreenWindow.show();
    }

    //Move to Add Customer Screen
    @FXML
    private void addCustomer(MouseEvent mouseEvent) throws IOException {
        Parent addCustomerScreenParent = FXMLLoader.load(getClass().getResource("AddCustomerScreen.fxml"));
        Scene addCustomerScreenScene = new Scene(addCustomerScreenParent);
        Stage addCustomerScreenWindow = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        addCustomerScreenWindow.setScene(addCustomerScreenScene);
        addCustomerScreenWindow.show();
    }

    //Move to Modify Appointment Screen
    @FXML
    private void modifyAppointment(MouseEvent mouseEvent) throws IOException {
        Parent modifyAppointmentScreenParent = FXMLLoader.load(getClass().getResource("ModifyAppointmentScreen.fxml"));
        Scene modifyAppointmentScreenScene = new Scene(modifyAppointmentScreenParent);
        Stage modifyAppointmentScreenWindow = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        modifyAppointmentScreenWindow.setScene(modifyAppointmentScreenScene);
        modifyAppointmentScreenWindow.show();
    }

    //Move to Modify Customer Screen
    @FXML
    private void modifyCustomer(MouseEvent mouseEvent) throws IOException {
        Parent modifyCustomerScreenParent = FXMLLoader.load(getClass().getResource("ModifyCustomerScreen.fxml"));
        Scene modifyCustomerScreenScene = new Scene(modifyCustomerScreenParent);
        Stage modifyCustomerScreenWindow = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        modifyCustomerScreenWindow.setScene(modifyCustomerScreenScene);
        modifyCustomerScreenWindow.show();
    }

    //Move to Reports Screen
    @FXML
    private void reports(MouseEvent mouseEvent) throws IOException {
        Parent reportsScreenParent = FXMLLoader.load(getClass().getResource("ReportsScreen.fxml"));
        Scene reportsScreenScene = new Scene(reportsScreenParent);
        Stage reportsScreenWindow = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        reportsScreenWindow.setScene(reportsScreenScene);
        reportsScreenWindow.show();
    }



    //Function to manipulate the actions after user clicks on the Weekly View radio button
    //Lambda Expression used to filter the table according to the Weekly View radio button action.
    //Using this expression makes the code shorter and easier to read and understand.
    @FXML
    private void weeklyViewClicked(MouseEvent mouseEvent) {
        selectDate.setDisable(false);
        allAppointmentsButton.setSelected(false);
        monthlyViewButton.setSelected(false);
        weeklyViewButton.setSelected(true);
        LocalDate weekCurrentDate = selectDate.getValue();
        updateDateInterface filteredList = date -> {
            readAppointmentsForFilteringByWeek(date);
        };
        filteredList.updateAppointmentList(weekCurrentDate);
    }

    //Function to manipulate the actions after user clicks on the Monthly View radio button
    //Lambda Expression used to filter the table according to the Monthly View radio button action
    //Using this expression makes the code shorter and easier to read and understand.
    @FXML
    private void monthlyViewClick(MouseEvent mouseEvent) {
        selectDate.setDisable(false);
        allAppointmentsButton.setSelected(false);
        monthlyViewButton.setSelected(true);
        weeklyViewButton.setSelected(false);
        LocalDate monthCurrentDate = selectDate.getValue();
        updateDateInterface filteredList = date -> {
            readAppointmentsForFilteringByMonth(date);
        };
        filteredList.updateAppointmentList(monthCurrentDate);

    }

    //Function to manipulate the actions after user clicks on the All Appointments radio button
    @FXML
    private void allAppointmentsClick(MouseEvent mouseEvent) {
        selectDate.setDisable(true);
        allAppointmentsButton.setSelected(true);
        monthlyViewButton.setSelected(false);
        weeklyViewButton.setSelected(false);
        showData();
    }
}
