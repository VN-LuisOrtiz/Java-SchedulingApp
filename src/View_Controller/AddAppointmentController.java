package View_Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ResourceBundle;


public class AddAppointmentController implements Initializable {


    @FXML
    private TextField apptTitle;
    @FXML
    private TextField apptDesc;
    @FXML
    private TextField apptLocation;
    @FXML
    private TextField apptContact;
    @FXML
    private TextField apptType;
    @FXML
    private TextField apptUrl;
    @FXML
    private ComboBox apptStartHourBox;
    @FXML
    private ComboBox apptStartMinBox;
    @FXML
    private ComboBox apptEndHourBox;
    @FXML
    private ComboBox apptEndMinBox;
    @FXML
    private DatePicker apptDate;
    @FXML
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    @FXML
    private ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer,String> addressColumn;
    @FXML
    private TableColumn<Customer,String> address2Column;
    @FXML
    private TableColumn<Customer, String> cityColumn;
    @FXML
    private TableColumn<Customer,String> zipColumn;
    @FXML
    private TableColumn<Customer,String> countryColumn;
    @FXML
    private TableColumn<Customer,String> phoneColumn;

    //Initialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        apptStartHourBox.getItems().addAll("08","09","10","11","12","13","14","15","16");
        apptStartMinBox.getItems().addAll("00","15","30","45");
        apptEndHourBox.getItems().addAll("09","10","11","12","13","14","15","16","17");
        apptEndMinBox.getItems().addAll("00","15","30","45");
        showData();
    }

    //Set columns to Appointments Data from Database
    private void showData() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        address2Column.setCellValueFactory(new PropertyValueFactory<>("address2"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerList.setAll(CustomerDAO.getAllCustomers());
        customerTable.setItems(customerList);
        customerTable.refresh();
        appointmentsList.setAll(AppointmentDAO.getAllAppointments());
    }

    //Checks for Empty Fields - Returns True if there are no empty fields and False if there are.
    private Boolean noEmptyFields(){
        if(apptTitle.getText().isEmpty() || apptDesc.getText().isEmpty() || apptLocation.getText().isEmpty() || apptContact.getText().isEmpty() || apptType.getText().isEmpty() || apptUrl.getText().isEmpty() || apptDate.getValue()==null || apptStartHourBox.getValue()==null || apptStartMinBox.getValue()==null || apptEndHourBox.getValue()==null || apptEndMinBox.getValue()==null) {
            oneOrMoreEmptyFields();
            return false;
        } else
            return true;
    }

    //Returns False if the date selected has passed already and True if it hasn't.
    private Boolean noPastDate(){
        if(apptDate.getValue().isBefore(LocalDate.now())) {
            pastDateError();
            return false;
        } else
            return true;
    }


    //Move back to Main Screen
    @FXML
    public void changeToMainScreen(MouseEvent mouseEvent) throws IOException {
        Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage mainScreenWindow = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        mainScreenWindow.setScene(mainScreenScene);
        mainScreenWindow.show();
    }


    //Save Button Function - Inserts new Appointment into Database - If conditions used to check that there are no issues with the provided data.
    @FXML
    public void saveAppointment(MouseEvent mouseEvent) throws IOException {
        if(customerTable.getSelectionModel().isEmpty()) {
            //Do nothing
        } else {
            if(noEmptyFields()) {
                Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
                if(noPastDate()) {
                    LocalDate userSelectedDate = apptDate.getValue();
                    int startHour = Integer.valueOf(String.valueOf(apptStartHourBox.getValue()));
                    int startMinute = Integer.valueOf(String.valueOf(apptStartMinBox.getValue()));
                    int endHour = Integer.valueOf(String.valueOf(apptEndHourBox.getValue()));
                    int endMinute = Integer.valueOf(String.valueOf(apptEndMinBox.getValue()));
                    LocalTime userSelectedStartTime = LocalTime.of(startHour,startMinute);
                    LocalTime userSelectedEndTime = LocalTime.of(endHour,endMinute);
                    System.out.println("Here1");
                    if(correctStartAndEndTime(userSelectedStartTime,userSelectedEndTime,userSelectedDate)) {
                        System.out.println("Here2");
                        if(noOverlappingAppointments(userSelectedDate,userSelectedStartTime,userSelectedEndTime)) {
                            System.out.println("Here7");
                            String startDateTime = AppointmentDAO.localDateTimeToGMT(userSelectedDate,userSelectedStartTime);
                            String endDateTime = AppointmentDAO.localDateTimeToGMT(userSelectedDate,userSelectedEndTime);
                            AppointmentDAO.addAppointment(selectedCustomer.getCustomerID(),UserDAO.getUserObj().getUserID(),apptTitle.getText(),apptDesc.getText(),apptLocation.getText(),apptContact.getText(),apptType.getText(),apptUrl.getText(),startDateTime,endDateTime);
                            displayAppointmentAddedSuccessfully();
                            clearFields();
                            showData();
                        }
                }
                }
            }

            }
        }

    //Checks that the Selected Start and End Times are correct. Returns False if times are the same, end time is before than start time, or if start time is not after current time (if selecting appointment for current day)
    public Boolean correctStartAndEndTime(LocalTime start, LocalTime end, LocalDate date){
        if(start.equals(end)) {
            displayEqualStartEndError();
            return false;
        } else if(end.isBefore(start)) {
            displayEndBeforeStartError();
            return false;
        } else if(date.equals(LocalDate.now()) && start.isBefore(LocalTime.now()) ){
            displayStartIsNotAfterCurrentTimeError();
            return false;
        } else
            return true;
    }

    //Returns True if the selected Dates and Times DO NOT overlap with existing appointments
    public Boolean noOverlappingAppointments(LocalDate selectedDate, LocalTime selectedStart, LocalTime selectedEnd){
        System.out.println("Here3");
        boolean result = false;
        for(int i=0;i<appointmentsList.size();i++) {
            System.out.println(appointmentsList.size() + " being compared to i which is " + i);
            System.out.println("i = "+ i + " " + selectedDate);
            System.out.println("i = "+ i + " " + appointmentsList.get(i).getStart().substring(0,10));
            if(selectedDate.equals(LocalDate.parse(appointmentsList.get(i).getStart().substring(0,10)))) {
                System.out.println("Here4");
                LocalTime startTime = LocalTime.parse(appointmentsList.get(i).getStart().substring(11,16));
                LocalTime endTime = LocalTime.parse(appointmentsList.get(i).getEnd().substring(11,16));
                if((selectedStart.isAfter(startTime) && selectedStart.isBefore(endTime)) || selectedStart.equals(startTime) || selectedStart.equals(endTime)) {
                    System.out.println("Here5");
                    displayOverlappingError();
                    return false;
                } else if((selectedEnd.isAfter(startTime) && selectedEnd.isBefore(endTime)) || selectedEnd.equals(startTime) || selectedEnd.equals(endTime)) {
                    System.out.println("Here6");
                    displayOverlappingError();
                    return false;
                } else
                    result = true;
            }
            result=true;
        }
        System.out.println(result);
        return result;
    }


    //Message to indicate that Appointment was added without any issues
    public static void displayAppointmentAddedSuccessfully() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Success!");
        window.setMinWidth(300);
        Label message = new Label();
        message.setText("Appointment added successfully!");
        Button closeError = new Button("Close");
        closeError.setOnAction(e -> window.close());
        VBox layout = new VBox(20);
        layout.getChildren().addAll(message,closeError);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    //Message to indicate that Appointment is overlapping another appointment.
    public static void displayOverlappingError() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ERROR");
        window.setMinWidth(300);
        Label message = new Label();
        message.setText("Overlapping Appointment!");
        Button closeError = new Button("Close");
        closeError.setOnAction(e -> window.close());
        VBox layout = new VBox(20);
        layout.getChildren().addAll(message,closeError);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    //Clears all fields after adding an appointment
    private void clearFields(){
        apptTitle.clear();
        apptDesc.clear();
        apptLocation.clear();
        apptContact.clear();
        apptType.clear();
        apptUrl.clear();
        apptDate.setValue(null);
        apptStartHourBox.setValue(null);
        apptStartMinBox.setValue(null);
        apptEndHourBox.setValue(null);
        apptEndMinBox.setValue(null);
    }

    //Message to indicate there are empty fields
    public static void oneOrMoreEmptyFields() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ERROR");
        window.setMinWidth(300);
        Label message = new Label();
        message.setText("Please make sure that there are no empty fields");
        Button closeError = new Button("Close");
        closeError.setOnAction(e -> window.close());
        VBox layout = new VBox(20);
        layout.getChildren().addAll(message,closeError);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    //Message to indicate that the selected date has already passed
    public static void pastDateError() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ERROR");
        window.setMinWidth(300);
        Label message = new Label();
        message.setText("The selected Date has already passed");
        Button closeError = new Button("Close");
        closeError.setOnAction(e -> window.close());
        VBox layout = new VBox(20);
        layout.getChildren().addAll(message,closeError);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }


    //Message to indicate that the Start and End Time are the same
    public static void displayEqualStartEndError() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ERROR");
        window.setMinWidth(300);
        Label message = new Label();
        message.setText("Start and End hours cannot be the same");
        Button closeError = new Button("Close");
        closeError.setOnAction(e -> window.close());
        VBox layout = new VBox(20);
        layout.getChildren().addAll(message,closeError);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    //Message to indicate that the End time is before Start Time.
    public static void displayEndBeforeStartError() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ERROR");
        window.setMinWidth(300);
        Label message = new Label();
        message.setText("End Time cannot be before Start Time");
        Button closeError = new Button("Close");
        closeError.setOnAction(e -> window.close());
        VBox layout = new VBox(20);
        layout.getChildren().addAll(message,closeError);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    //Message to indicate that the Start time is not after the current time while trying to create an appointment for the current day (today)
    public static void displayStartIsNotAfterCurrentTimeError() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ERROR");
        window.setMinWidth(300);
        Label message = new Label();
        message.setText("Start Time is not after current time");
        Button closeError = new Button("Close");
        closeError.setOnAction(e -> window.close());
        VBox layout = new VBox(20);
        layout.getChildren().addAll(message,closeError);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
