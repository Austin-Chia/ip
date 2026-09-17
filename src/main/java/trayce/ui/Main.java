package trayce.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Trayce using FXML.
 */
public class Main extends Application {

    private final trayce.Trayce trayce = new trayce.Trayce();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));

            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Trayce — Your Task Trail Guide");

            stage.setMinHeight(220);
            stage.setMinWidth(417);

            fxmlLoader.<MainWindow>getController().setTrayce(trayce);

            stage.show();
        } catch (IOException exception) {
            showStartupError();
        }
    }

    /** Shows a clear error when the application layout cannot be loaded. */
    private void showStartupError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Trayce startup error");
        alert.setHeaderText("Trayce could not start");
        alert.setContentText("The application layout could not be loaded. "
                + "Please reinstall Trayce and try again.");
        alert.showAndWait();
    }
}
