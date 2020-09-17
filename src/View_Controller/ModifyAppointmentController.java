package View_Controller;

import Model.Appointment;
import Model.AppointmentDAO;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ModifyAppointmentController implements Initializable {
    private boolean alertAnswer = false;
    @FXML
    private RadioButton deleteButton;
    @FXML
    private RadioButton editButton;
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
    private TableColumn startColumn;
    @FXML
    private TableColumn endColumn;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
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
    private TableColumn<Appointment,String> contactColumn;
    @FXML
    private TableColumn<Appointment,String> typeColumn;
    @FXML
    private TableColumn<Appointment,String> urlColumn;
    @FXML
    private Label timeZoneLabel;

    //Initialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        apptStartHourBox.getItems().addAll("08","09","10","11","12","13","14","15","16");
        apptStartMinBox.getItems().addAll("00","15","30","45");
        apptEndHourBox.getItems().addAll("09","10","11","12","13","14","15","16","17");
        apptEndMinBox.getItems().addAll("00","15","30","45");
        showData();
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

    //Set columns to Appointments Data from Database
    private void showData() {
        apptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentsList.setAll(AppointmentDAO.getAllAppointments());
        appointmentTable.setItems(appointmentsList);
        appointmentTable.refresh();
    }

    //Save Button Function - Modifies the selected Appointment - If conditions used to check that there are no issues with the provided data.
    @FXML
    private void saveChange(MouseEvent mouseEvent) {
        if(!deleteButton.isSelected() && !editButton.isSelected()){
            //Do nothing
        } else if (deleteButton.isSelected()){
            if(appointmentTable.getSelectionModel().isEmpty()) {
                //Do nothing
            } else {
                cancelDelete();
                if(alertAnswer==true) {
                    Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
                    AppointmentDAO.deleteAppointment(selectedAppointment.getAppointmentID());
                    showData();
                }
            }
        } else if (editButton.isSelected()) {
            if(appointmentTable.getSelectionModel().isEmpty()) {
                //Do nothing
            } else {
                if(noEmptyFields()){
                    Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
                    if(noPastDate()){
                        LocalDate userSelectedDate = apptDate.getValue();
                        int startHour = Integer.valueOf(String.valueOf(apptStartHourBox.getValue()));
                        int startMinute = Integer.valueOf(String.valueOf(apptStartMinBox.getValue()));
                        int endHour = Integer.valueOf(String.valueOf(apptEndHourBox.getValue()));
                        int endMinute = Integer.valueOf(String.valueOf(apptEndMinBox.getValue()));
                        LocalTime userSelectedStartTime = LocalTime.of(startHour,startMinute);
                        LocalTime userSelectedEndTime = LocalTime.of(endHour,endMinute);
                        if(correctStartAndEndTime(userSelectedStartTime,userSelectedEndTime,userSelectedDate)){
                            if(noOverlappingAppointments(userSelectedDate,userSelectedStartTime,userSelectedEndTime)){
                                String startDateTime = AppointmentDAO.localDateTimeToGMT(userSelectedDate,userSelectedStartTime);
                                String endDateTime = AppointmentDAO.localDateTimeToGMT(userSelectedDate,userSelectedEndTime);
                                AppointmentDAO.updateAppointment(selectedAppointment.getAppointmentID(),apptTitle.getText(),apptDesc.getText(),apptLocation.getText(),apptContact.getText(),apptType.getText(),apptUrl.getText(),startDateTime,endDateTime);
                                displayAppointmentChangedSuccessfully();
                                showData();
                            }
                        }
                    }
                }
            }
        }
    }


    //Checks for Empty Fields - Returns True if there are no empty fields and False if there are.
    private Boolean noEmptyFields(){
        if(apptTitle.getText().isEmpty() || apptDesc.getText().isEmpty() || apptLocation.getText().isEmpty() || apptContact.getText().isEmpty() || apptType.getText().isEmpty() || apptUrl.getText().isEmpty() || apptDate.getValue()==null || apptStartHourBox.getValue()==null || apptStartMinBox.getValue()==null || apptEndHourBox.getValue()==null || apptEndMinBox.getValue()==null) {
            oneOrMoreEmptyFields();
            return false;
        } else
            return true;
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

    //Returns False if the date selected has passed already and True if it hasn't.
    private Boolean noPastDate(){
        if(apptDate.getValue().isBefore(LocalDate.now())) {
            pastDateError();
            return false;
        } else
            return true;
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

    //Message to indicate that Appointment was changed without any issues
    public static void displayAppointmentChangedSuccessfully() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Success!");
        window.setMinWidth(300);
        Label message = new Label();
        message.setText("Appointment changed successfully!");
        Button closeError = new Button("Close");
        closeError.setOnAction(e -> window.close());
        VBox layout = new VBox(20);
        layout.getChildren().addAll(message,closeError);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    //Function to manipulate the actions after user clicks on the delete radio button
    @FXML
    private void deleteClick(MouseEvent mouseEvent) {
        editButton.setSelected(false);
        apptTitle.setDisable(true);
        apptDesc.setDisable(true);
        apptLocation.setDisable(true);
        apptContact.setDisable(true);
        apptType.setDisable(true);
        apptUrl.setDisable(true);
        apptDate.setDisable(true);
        apptStartHourBox.setDisable(true);
        apptStartMinBox.setDisable(true);
        apptEndHourBox.setDisable(true);
        apptEndMinBox.setDisable(true);
    }

    //Function to manipulate the actions after user clicks on the edit radio button
    @FXML
    private void editClick(MouseEvent mouseEvent) {
        deleteButton.setSelected(false);
        apptTitle.setDisable(false);
        apptDesc.setDisable(false);
        apptLocation.setDisable(false);
        apptContact.setDisable(false);
        apptType.setDisable(false);
        apptUrl.setDisable(false);
        apptDate.setDisable(false);
        apptStartHourBox.setDisable(false);
        apptStartMinBox.setDisable(false);
        apptEndHourBox.setDisable(false);
        apptEndMinBox.setDisable(false);
    }

    //Alerts the user that they are about to delete an Appointment and double checks with user if they want to continue with this action.
    //Lambda expression used to prevent from creating an additional method that checks if the user wants to continue with the action or not.
    private void cancelDelete() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("");
        window.setMinWidth(300);
        Label message = new Label();
        message.setText("Are you sure you want to delete the selected Appointment?");
        Button yesB = new Button("Yes");
        Button noB = new Button("No");
        noB.setOnAction(e -> {
            alertAnswer = false;
            window.close();
        });
        yesB.setOnMouseClicked(e -> {
            alertAnswer = true;
            window.close();
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(message,yesB,noB);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    //Updates the Text Fields according to what the user selects from the table
    @FXML
    private void updateTextFields(MouseEvent mouseEvent) {
        if(appointmentTable.getSelectionModel().isEmpty()) {
            //Do nothing
        } else {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            apptTitle.setText(selectedAppointment.getTitle());
            apptDesc.setText(selectedAppointment.getDescription());
            apptLocation.setText(selectedAppointment.getLocation());
            apptContact.setText(selectedAppointment.getContact());
            apptType.setText(selectedAppointment.getType());
            apptUrl.setText(selectedAppointment.getUrl());
            apptDate.setValue(LocalDate.parse(selectedAppointment.getStart().substring(0,10)));
            apptStartHourBox.setValue(selectedAppointment.getStart().substring(11,13));
            apptStartMinBox.setValue(selectedAppointment.getStart().substring(14,16));
            apptEndHourBox.setValue(selectedAppointment.getEnd().substring(11,13));
            apptEndMinBox.setValue(selectedAppointment.getEnd().substring(14,16));
        }
    }
}
