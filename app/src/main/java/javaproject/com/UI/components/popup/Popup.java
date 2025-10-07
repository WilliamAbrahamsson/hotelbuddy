package javaproject.com.UI.components.popup;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.views.app.App;

public class Popup {

  static User user = App.getUser();

  public static void ErrorDialogBox(String message) {
    // Create image and image view for logo
    Image logo1 = null;
    if (user.getTheme().equals("dark")) {
      logo1 = new Image("/hotelbuddy.png");
    } else {
      logo1 = new Image("/dark_hotelbuddy.png");
    }
    ImageView logo1View = new ImageView(logo1);

    logo1View.setPreserveRatio(true);
    logo1View.setFitWidth(200);
    logo1View.setFitHeight(200);
    StackPane logoPane = new StackPane(logo1View);

    // Create a label with the message
    Label messageLabel = new Label(message);
    if(user.getTheme().equals("dark")){
      messageLabel.setStyle(
              "-fx-font-size: 14pt; -fx-text-fill: #ffffff; -fx-font-family:Helvetica; -fx-translate-y: -50;"
      );

    }
    else {
      messageLabel.setStyle(
              "-fx-font-size: 14pt; -fx-text-fill: #000000; -fx-font-family:Helvetica; -fx-translate-y: -50;"
      );
    }

    // Create a VBox with the logo and message
    VBox vbox = new VBox(20, logo1View, messageLabel);
    int size = 250;
    vbox.setPrefSize(size, size);
    vbox.setMinSize(size, size);
    vbox.setMaxSize(size, size);
    vbox.setAlignment(Pos.CENTER);

    // Create an error alert
    Alert alert = new Alert(AlertType.ERROR);
    // Get the scene associated with the alert
    Scene scenee = alert.getDialogPane().getScene();
    if(user.getTheme().equals("dark")){
      scenee.getStylesheets().add("/style/BoxesStyle.css");
    }
    else {
      scenee.getStylesheets().add("/style/LightBoxesStyle.css");
    }

    // Set alert title and header text
    alert.setTitle("Error");

    // Set alert graphic to logo image view
    alert.setGraphic(logoPane);

    // Add a timer to close the alert after 10 seconds if the user has not clicked a button
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        Platform.runLater(() -> alert.hide());
      }
    };
    long period = 10 * 1000L;
    timer.schedule(task, period);

    // Add an "OK" button
    ButtonType okButton = new ButtonType("OK");
    alert.getButtonTypes().setAll(okButton);
    // Set the content of the dialog pane to the VBox with the logo and message
    alert.getDialogPane().setContent(vbox);
    // Show the alert
    alert.showAndWait();
    timer.cancel();
    // Add key event handler to close when enter is pressed
    scenee
      .getRoot()
      .setOnKeyPressed(event -> {
        if (event.getCode().equals(KeyCode.ENTER)) {
          alert.hide();
        }
      });
  }

  public static void ConfirmDialogBox(String message) {
    // Create image and image view for logo
    Image logoImage = null;
    if (user.getTheme().equals("dark")) {
      logoImage = new Image("/hotelbuddy.png");
    } else {
      logoImage = new Image("/dark_hotelbuddy.png");
    }
    ImageView logoImageView = new ImageView(logoImage);

    logoImageView.setPreserveRatio(true);
    logoImageView.setFitHeight(200);
    logoImageView.setFitWidth(200);
    StackPane logoPane = new StackPane(logoImageView);

    /*

        // Create a blur effect for the logo image
        BoxBlur blur = new BoxBlur();
        blur.setWidth(5);
        blur.setHeight(5);
        blur.setIterations(1);
        logoImageView.setEffect(blur);

 */

    // Create a label with the message
    Label messageLabel = new Label(message);
    if(user.getTheme().equals("dark")){
      messageLabel.setStyle(
              "-fx-font-size: 14pt; -fx-text-fill: white; -fx-font-family:Helvetica; -fx-translate-y: -50;"
      );
    }
    else {
      messageLabel.setStyle(
              "-fx-font-size: 14pt; -fx-text-fill: #000000; -fx-font-family:Helvetica; -fx-translate-y: -50;"
      );
    }

    // Create a VBox with the logo and message
    VBox vbox = new VBox(20, logoImageView, messageLabel);
    int size = 250;
    vbox.setPrefSize(size, size);
    vbox.setMinSize(size, size);
    vbox.setMaxSize(size, size);
    vbox.setAlignment(Pos.CENTER);

    // Create alert with CONFIRMATION type
    Alert alert = new Alert(AlertType.CONFIRMATION);
    // Get the scene of the alert dialog pane
    Scene dialogScene = alert.getDialogPane().getScene();

    // Add stylesheet to the scene
    if(user.getTheme().equals("dark")){
      dialogScene.getStylesheets().add("/style/BoxesStyle.css");
    }
    else {
      dialogScene.getStylesheets().add("/style/LightBoxesStyle.css");
    }


    // Set alert title and header text
    alert.setTitle("Confirm");
    // Set alert graphic to logo image view
    alert.setGraphic(logoPane);

    // Add key event handler to close when enter is pressed
    dialogScene
      .getRoot()
      .setOnKeyPressed(event -> {
        if (event.getCode().equals(KeyCode.ENTER)) {
          alert.hide();
        }
      });

    alert.setGraphic(logoPane);
    ButtonType okButton = new ButtonType("OK");
    alert.getButtonTypes().setAll(okButton);
    // Add the VBox to the dialog pane
    alert.getDialogPane().setContent(vbox);

    // Add a timer to close the alert after 10 seconds if the user has not clicked a button
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        Platform.runLater(() -> alert.hide());
      }
    };
    long period = 10 * 1000L;
    timer.schedule(task, period);

    // Show the alert and wait for the user to click a button
    alert.showAndWait();
    timer.cancel();
  }
}
