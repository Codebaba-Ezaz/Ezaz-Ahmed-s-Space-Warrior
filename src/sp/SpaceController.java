package sp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sayem
 */
public class SpaceController implements Initializable {

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void btnStartClicked(ActionEvent event) {
    
    SpaceWarrior spaceWarrior = new SpaceWarrior();

    Stage gameStage = new Stage();

    try {
        spaceWarrior.start(gameStage);
    } catch (Exception e) {
    }
}
    @FXML
    private void btnExitClicked(ActionEvent event) {
        // Show a "Thank You" message in a dialog and then exit the program.
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thank You");
        alert.setHeaderText(null);
        alert.setContentText("This Game is Brought To You By Ezaz Ahmed C223009.");
        alert.showAndWait();

        // Close the application
        System.exit(0);
    }

    @FXML
    private void btnaboutClicked(ActionEvent event) {
        String filePath = "C:\\Users\\Saem\\Desktop\\About.txt";

        
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            

        }

      
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText(content.toString());
        alert.showAndWait();
    }
}
