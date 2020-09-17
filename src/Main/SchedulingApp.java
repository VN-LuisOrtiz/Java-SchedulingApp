//C195 PA - Luis Ortiz
//Logs and Reports are stored in the "C195 - Scheduling Application" folder.
/*Lambda Expressions used in:
                MainController.java - To update filter table by month and by week
                AddAppointment, AddCustomer, Login Controllers - To close Error/Information windows.
                ModifyAppointment, ModifyCustomer Controllers - To receive confirmation from user when wanting to delete from Database
 */

package Main;
import Utils.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SchedulingApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View_Controller/LoginScreen.fxml"));
        primaryStage.setScene(new Scene(root, 321, 337));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        DBConnection.startConnection(); //Start Database Connection
        launch(args);
        DBConnection.closeConnection(); //End Database Connection
    }
}
