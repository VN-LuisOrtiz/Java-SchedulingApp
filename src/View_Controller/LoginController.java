package View_Controller;

import Model.UserDAO;
import Utils.LoggerUtil;
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
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    //Labels
    @FXML
    private Label titleLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;

    //Text Fields
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    //Button
    @FXML
    private Button logInButton;

    //Initialization - Takes care of User's Language (English or Spanish)
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ResourceBundle rb = ResourceBundle.getBundle("Utils/Lang", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("en")) {
            titleLabel.setText(rb.getString("Title"));
            usernameLabel.setText(rb.getString("UserLabel"));
            passwordLabel.setText(rb.getString("PassLabel"));
            logInButton.setText(rb.getString("Button"));
        }
    }


    //Trying to log in with the user credentials
    @FXML
    public void tryToLogin(MouseEvent mouseEvent) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username.isEmpty() || password.isEmpty()){
            displayEmptyFieldsError();
        } else if (UserDAO.validateLogin(username,password)){
            LoggerUtil.logUser(UserDAO.getUserObj().getUserID(),UserDAO.getUserObj().getUsername());
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainScreenWindow = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            mainScreenWindow.setScene(mainScreenScene);
            mainScreenWindow.show();
        } else
            incorrectCredentials();
    }

    //Error message if any text field is empty - Uses Resource Bundle
    public static void displayEmptyFieldsError(){
        ResourceBundle rb = ResourceBundle.getBundle("Utils/Lang", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("en")) {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle(rb.getString("ErrorWindow"));
            window.setMinWidth(300);
            Label message = new Label();
            message.setText(rb.getString("EmptyFields"));
            Button closeError = new Button(rb.getString("TempButton"));
            closeError.setOnAction(e -> window.close());
            VBox layout = new VBox(20);
            layout.getChildren().addAll(message,closeError);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.showAndWait();
        }

    }

    //Error message if either the username or password are incorrect - Uses Resource Bundle
    public static void incorrectCredentials(){
        ResourceBundle rb = ResourceBundle.getBundle("Utils/Lang", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("en")) {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle(rb.getString("ErrorWindow"));
            window.setMinWidth(300);
            Label message = new Label();
            message.setText(rb.getString("BadCredentials"));
            Button closeError = new Button(rb.getString("TempButton"));
            closeError.setOnAction(e -> window.close());
            VBox layout = new VBox(20);
            layout.getChildren().addAll(message,closeError);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.showAndWait();
        }
    }

}
