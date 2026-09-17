package trayce.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the
 * speaker's face and a label containing text from the speaker.
 */
public class DialogBox extends HBox {

    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image image) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load the dialog box layout.", exception);
        }

        dialog.setText(text);
        displayPicture.setImage(image);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left
     * and text is on the right.
     */
    private void flip() {
        ObservableList<Node> dialogElements =
                FXCollections.observableArrayList(getChildren());

        Collections.reverse(dialogElements);
        getChildren().setAll(dialogElements);
        setAlignment(Pos.TOP_LEFT);

        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a dialog box for a message entered by the user.
     *
     * @param text message to display
     * @param image user's display image
     * @return dialog box styled and aligned for the user
     */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /**
     * Creates a dialog box for a response from Trayce.
     *
     * @param text response to display
     * @param image Trayce's display image
     * @return dialog box styled and aligned for Trayce
     */
    public static DialogBox getTrayceDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.flip();
        return dialogBox;
    }
}
