package Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;

public class LoggerUtil {
    private static String filename = "log.txt";

    //Log user logins
    public static void logUser(int userID, String username) throws IOException{
        FileWriter fileWriter = new FileWriter(filename,true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        outputFile.println("UserID: " + userID + ", Username: " + username + ", Last Successful Login:  " + ZonedDateTime.now());
        outputFile.close();
    }
}
