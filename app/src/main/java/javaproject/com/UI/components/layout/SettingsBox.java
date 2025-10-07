package javaproject.com.UI.components.layout;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javaproject.com.DB.tables.User;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.UI.views.app.App;

public class SettingsBox extends HBox {
  protected RequestHandler finalRequestHandler = App.getDBCON();
  VBox leftBox = new VBox();
  User user = App.getUser();

  public SettingsBox(
      double height,
      String color,
      String title,
      String description,
      VBox rightBox) {
    // width as parents width
    setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(this, Priority.ALWAYS);

    // height specified
    setPrefHeight(height);
    setMinHeight(height);
    setMaxHeight(height);

    setStyle("-fx-background-color: " + color + ";");

    // left box
    leftBox.setPrefWidth(300);
    leftBox.setMinWidth(300);
    leftBox.setMaxWidth(300);

    // text box vbox
    VBox textBox = new VBox();
    textBox.setPadding(new Insets(15, 0, 0, 40));

    // add title label to left box
    Label titleLabel = new Label(title);

    // add description label to left box
    Label descriptionLabel = new Label(description);

    if(user.getTheme().equals("dark")){
      titleLabel.setStyle(
              "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #fff;"
      );

      descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff;");
    }
    else {
      titleLabel.setStyle(
              "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #000000;"
      );

      descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;");
    }

    textBox.getChildren().add(titleLabel);
    textBox.getChildren().add(descriptionLabel);

    leftBox.getChildren().addAll(textBox);
    leftBox.setPrefHeight(height);

    // right box fit the rest of width
    rightBox.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(rightBox, Priority.ALWAYS);
    rightBox.setPrefHeight(height);

    // add to parent
    getChildren().add(leftBox);
    getChildren().add(rightBox);
  }

}
