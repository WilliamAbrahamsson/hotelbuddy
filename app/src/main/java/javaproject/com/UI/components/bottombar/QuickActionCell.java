package javaproject.com.UI.components.bottombar;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.views.app.App;

public class QuickActionCell extends HBox {

  User user = App.getUser();

  public QuickActionCell(String title) {
    setStyle("-fx-border-color: #1d1d1d; -fx-border-width: 0 1 0 1;");

    // min size
    setMinWidth(80);
    setPrefWidth(10_000);

    // set height
    setMinHeight(40);
    setPrefHeight(40);

    HBox horizontal_wrapper = new HBox();
    horizontal_wrapper.setSpacing(20);
    horizontal_wrapper.setAlignment(Pos.CENTER);
    HBox.setHgrow(horizontal_wrapper, Priority.ALWAYS);

    // add title label in the center of the tab
    Label titleLabel = new Label(title);
    if(user.getTheme().equals("dark")){
      titleLabel.setStyle(
              "-fx-font-size: 12px; -fx-text-fill: #fff; -fx-font-weight: bold; -fx-text-color: #fff;"
      );
    }
    else {
      titleLabel.setStyle(
              "-fx-font-size: 12px; -fx-text-fill: #000000; -fx-font-weight: bold; -fx-text-color: #000000;"
      );
    }
    titleLabel.setAlignment(Pos.CENTER_LEFT);
    titleLabel.setMaxWidth(Double.MAX_VALUE);

    // add padding left and right
    HBox.setHgrow(titleLabel, Priority.ALWAYS);

    // padding left
    titleLabel.setPadding(new javafx.geometry.Insets(0, 0, 0, 10));

    Button exitButton = new Button();
    Image logoutImage = null;
    if(user.getTheme().equals("dark")){
      logoutImage = new Image("/plus.png");
    }
    else {
      logoutImage = new Image("/dark_plus.png");
    }

    // remove bg
    exitButton.setStyle("-fx-background-color: transparent;");

    ImageView crossImageView = new ImageView(logoutImage);
    crossImageView.setFitWidth(20);
    crossImageView.setFitHeight(20);
    exitButton.setGraphic(crossImageView);

    // on click
    exitButton.setOnAction(e -> {});

    // on mouse enter and exit
    if(user.getTheme().equals("dark")){
      exitButton.setOnMouseEntered(e -> {
        exitButton.setStyle("-fx-background-color: #171717;");
      });
    }
    else {
      exitButton.setOnMouseEntered(e -> {
        exitButton.setStyle("-fx-background-color: #545454;");
      });
    }

    exitButton.setOnMouseExited(e -> {
      exitButton.setStyle("-fx-background-color: transparent;");
    });

    // add to horizontal_wrapper
    horizontal_wrapper.getChildren().addAll(titleLabel, exitButton);

    // add to tab
    getChildren().add(horizontal_wrapper);

    // hover functon
    if(user.getTheme().equals("dark")){
      setOnMouseEntered(e -> {
        setStyle(
                "-fx-background-color: #1d1d1d; -fx-border-color: #2b2b2b; -fx-border-width: 0 1 0 1;"
        );
      });
    }
    else {
      setOnMouseEntered(e -> {
        setStyle(
                "-fx-background-color: #b9b9b9; -fx-border-color: #000000; -fx-border-width: 0 1 0 1;"
        );
      });

    }

    setOnMouseExited(e -> {
      setStyle("-fx-border-color: #2b2b2b; -fx-border-width: 0 1 0 1;");
    });
  }
}
