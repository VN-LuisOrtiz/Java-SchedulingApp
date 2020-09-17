package View_Controller;

import Model.CustomerDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {
    @FXML
    private TextField customerName;
    @FXML
    private TextField customerAddress;
    @FXML
    private TextField customerAddress2;
    @FXML
    private TextField customerCity;
    @FXML
    private TextField customerZip;
    @FXML
    private TextField customerCountry;
    @FXML
    private TextField customerPhone;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    //Checks for Empty Fields - Returns True if there are no empty fields (except for address 2) and False if there are.
    private Boolean noEmptyFields(){
        if(customerName.getText().isEmpty() || customerAddress.getText().isEmpty() || customerCity.getText().isEmpty() || customerZip.getText().isEmpty() || customerCountry.getText().isEmpty() || customerPhone.getText().isEmpty()) {
            oneOrMoreEmptyFields();
            return false;
        } else
            return true;

    }

    //Message that lets user know that there are empty fields
    public static void oneOrMoreEmptyFields() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ERROR");
        window.setMinWidth(300);
        Label message = new Label();
        message.setText("Please make sure that there are no empty fields (except for Address 2)");
        Button closeError = new Button("Close");
        closeError.setOnAction(e -> window.close());
        VBox layout = new VBox(20);
        layout.getChildren().addAll(message,closeError);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }


    //Save Button Function - Inserts new Customer into Database.
    @FXML
    public void addNewCustomer(MouseEvent mouseEvent) throws IOException {
        if(noEmptyFields()) {
            CustomerDAO.addAllCustomerInfo(customerName.getText(),customerAddress.getText(),customerAddress2.getText(),customerCity.getText(),customerZip.getText(),customerPhone.getText(),customerCountry.getText());
        }
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

}
