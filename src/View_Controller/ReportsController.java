package View_Controller;

import Model.ReportDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    private RadioButton customersByCountry;
    @FXML
    private RadioButton userSchedule;
    @FXML
    private RadioButton typesByMonth;
    @FXML
    private TextArea textArea;

    //Initialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textArea.clear();
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

    //Generates selected report
    public void generateReport(MouseEvent mouseEvent) {
        if(typesByMonth.isSelected()) {
            ReportDAO.appointmentTypesByMonth();
            textArea.setText("Number of Appointment Types by Month Report has been created!\n Check the \"C195 - Scheduling Application Folder\"");
        }

        if(userSchedule.isSelected()){
            ReportDAO.usersScheduleReport();
            textArea.setText("Schedule for Each Consultant Report has been created!\n Check the \"C195 - Scheduling Application Folder\"");
        }

        if(customersByCountry.isSelected()){
            ReportDAO.customersByCountryReport();
            textArea.setText("Number of Customers by Country Report has been created!\n Check the \"C195 - Scheduling Application Folder\"");
        }
    }

    //Function to manipulate the actions after user clicks on the first radio button
    @FXML
    public void report1Click(MouseEvent mouseEvent) {
        typesByMonth.setSelected(true);
        userSchedule.setSelected(false);
        customersByCountry.setSelected(false);
    }

    //Function to manipulate the actions after user clicks on the second radio button
    @FXML
    public void report2Click(MouseEvent mouseEvent) {
        typesByMonth.setSelected(false);
        userSchedule.setSelected(true);
        customersByCountry.setSelected(false);
    }

    //Function to manipulate the actions after user clicks on the third radio button
    @FXML
    public void report3Click(MouseEvent mouseEvent) {
        typesByMonth.setSelected(false);
        userSchedule.setSelected(false);
        customersByCountry.setSelected(true);
    }
}
