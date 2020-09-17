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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {
    private boolean alertAnswer = false;

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
    @FXML
    private HBox customerInfo;
    @FXML
    private RadioButton deleteButton;
    @FXML
    private RadioButton editButton;
    @FXML
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn addressColumn;
    @FXML
    private TableColumn address2Column;
    @FXML
    private TableColumn cityColumn;
    @FXML
    private TableColumn zipColumn;
    @FXML
    private TableColumn countryColumn;
    @FXML
    private TableColumn phoneColumn;

    private String originalCustomerName;
    private String originalAddress;
    private String originalAddress2;
    private String originalCity;
    private String originalPostalCode;
    private String originalCountry;
    private String originalPhone;

    //Initialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    //Set columns to Customers Data from Database
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
    }


    //Save Button Function - Modifies the selected Appointment - If conditions used to check that there are no issues with the provided data.
    @FXML
    private void saveChange(MouseEvent mouseEvent) {
        if(!deleteButton.isSelected() && !editButton.isSelected()){
            //Do nothing
        } else if (deleteButton.isSelected()){
            if(customerTable.getSelectionModel().isEmpty()) {
                //Do nothing
            } else {
                cancelChange();
                if(alertAnswer==true) {
                    Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
                    if(AppointmentDAO.isCustomerAssociatedWithAppointment(selectedCustomer.getCustomerID()) != 0){
                        cancelDeleteDueToAppointment();
                        if(alertAnswer==true){
                            AppointmentDAO.deleteAppointment(AppointmentDAO.isCustomerAssociatedWithAppointment(selectedCustomer.getCustomerID()));
                            CustomerDAO.deleteCustomer(selectedCustomer.getCustomerID());
                            showData();
                        }
                    } else {
                        CustomerDAO.deleteCustomer(selectedCustomer.getCustomerID());
                        showData();
                    }
                }
            }
        } else if (editButton.isSelected()) { //Once the User Selects the Edit Radio Button, the following code checks what parts of customer data (customer, address, city, country) are being modified.
            if(customerTable.getSelectionModel().isEmpty()) {
                //Do nothing
            } else {
                cancelChange();
                if(alertAnswer==true) {
                    if(noEmptyFields()){
                        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
                        if(changeInName(customerName.getText())) {
                            CustomerDAO.updateCustomerName(customerName.getText(),selectedCustomer.getCustomerID());
                        }
                        if(changeInAddress(customerAddress.getText(),customerAddress2.getText(),customerZip.getText(),customerPhone.getText())){
                            int addressID = AddressDAO.searchAddressID(customerAddress.getText(),customerAddress2.getText(),customerCity.getText(),customerZip.getText(),customerPhone.getText(),customerCountry.getText());
                            if(addressID==selectedCustomer.getAddressID()) {
                                AddressDAO.updateAddress(selectedCustomer.getAddressID(), customerAddress.getText(),customerAddress2.getText(),customerZip.getText(),customerPhone.getText());
                            } else {
                                CustomerDAO.updateCustomerAddressID(addressID,selectedCustomer.getCustomerID());
                            }
                        }
                        if(changeInCity(customerCity.getText())) {
                            int cityID = CityDAO.searchCityID(customerCity.getText(),customerCountry.getText());
                            if(cityID==selectedCustomer.getCityID()) {
                                CityDAO.updateCity(selectedCustomer.getCityID(),customerCity.getText());
                            } else {
                                AddressDAO.updateAddressCityID(cityID,selectedCustomer.getAddressID());
                            }

                        }
                        if(changeInCountry(customerCountry.getText())) {
                            CountryDAO.updateCountry(selectedCustomer.getCountryID(),customerCountry.getText());
                        }
                        if((!changeInName(customerName.getText())) && (changeInAddress(customerAddress.getText(),customerAddress2.getText(),customerZip.getText(),customerPhone.getText())) && (changeInCity(customerCity.getText())) && (changeInCountry(customerCountry.getText())))
                            System.out.println("No updates");
                        showData();
                    }
                }
            }
        }
    }

    //Checks if there was a change in Customer's Name
    private Boolean changeInName(String customerName){
        if(originalCustomerName.equals(customerName))
            return false;
        else
            return true;
    }

    //Checks if there was a change in Customer's Address
    private Boolean changeInAddress(String address, String address2, String postalCode, String phone){
        if(!originalAddress.equals(address) || !originalAddress2.equals(address2) || !originalPostalCode.equals(postalCode) || !originalPhone.equals(phone))
            return true;
        else
            return false;
    }

    //Checks if there was a change in Customer's City
    private Boolean changeInCity(String city){
        if(originalCity.equals(city))
            return false;
        else
            return true;
    }

    //Checks if there was a change in Customer's Country
    private Boolean changeInCountry(String country){
        if(originalCountry.equals(country))
            return false;
        else
            return true;
    }

    //Function to manipulate the actions after user clicks on the delete radio button
    @FXML
    private void deleteClick(MouseEvent mouseEvent) {
        editButton.setSelected(false);
        customerInfo.setDisable(true);
    }

    //Function to manipulate the actions after user clicks on the edit radio button
    @FXML
    private void editClick(MouseEvent mouseEvent) {
        deleteButton.setSelected(false);
        customerInfo.setDisable(false);
    }

    //Alerts the user that they are about to delete a Customer and double checks with user if they want to continue with this action.
    //Lambda expression used to prevent from creating an additional method that checks if the user wants to continue with the action or not.
    private void cancelChange() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("");
        window.setMinWidth(300);
        Label message = new Label();
        message.setText("Are you sure you want to edit/delete the selected Customer?");
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

    //Alerts the user that they are about to delete a Customer that has an upcoming or past Appointment and informs the user that if they want to continue with this action, the Appointment data associated to
    //this customer will also be deleted.
    //Lambda expression used to prevent from creating an additional method that checks if the user wants to continue with the action or not.
    private void cancelDeleteDueToAppointment() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("");
        window.setMinWidth(300);
        Label message = new Label();
        message.setTextAlignment(TextAlignment.CENTER);
        message.setText("The selected Customer has a past or upcoming appointment.\n Deleting this Customer will also delete the appointments associated to their data.\n Do you want to continue?");
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
        if(customerTable.getSelectionModel().isEmpty()) {
            //Do nothing
        } else {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            customerName.setText(selectedCustomer.getCustomerName());
            customerAddress.setText(selectedCustomer.getAddress());
            customerAddress2.setText(selectedCustomer.getAddress2());
            customerCity.setText(selectedCustomer.getCity());
            customerZip.setText(selectedCustomer.getPostalCode());
            customerCountry.setText(selectedCustomer.getCountry());
            customerPhone.setText(selectedCustomer.getPhone());
            originalCustomerName = customerName.getText();
            originalAddress = customerAddress.getText();
            originalAddress2 = customerAddress2.getText();
            originalCity = customerCity.getText();
            originalPostalCode = customerZip.getText();
            originalCountry = customerCountry.getText();
            originalPhone = customerPhone.getText();
        }
    }

    //Checks for Empty Fields - Returns True if there are no empty fields and False if there are.
    private Boolean noEmptyFields(){
        if(customerName.getText().isEmpty() || customerAddress.getText().isEmpty() || customerCity.getText().isEmpty() || customerZip.getText().isEmpty() || customerCountry.getText().isEmpty() || customerPhone.getText().isEmpty()) {
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
        message.setText("Please make sure that there are no empty fields (except for Address 2 if not needed)");
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
