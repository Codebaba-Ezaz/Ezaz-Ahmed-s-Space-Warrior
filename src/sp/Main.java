package sp;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file and create a new FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("space.fxml"));

            // Load the FXML file and create a new Parent node
            Parent root = loader.load();

            // Create the main scene with the loaded FXML content
            Scene scene = new Scene(root);

            primaryStage.setTitle("Space Warriors");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
