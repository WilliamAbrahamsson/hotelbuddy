package javaproject.com.UI.components.layout;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.views.app.App;

public class TitleBox extends VBox {

  User user = App.getUser();

  public TitleBox(String title, String subtitle) {
    setMaxHeight(70);
    setPrefHeight(70);
    setMinHeight(70);

    // hgrow
    HBox.setHgrow(this, Priority.ALWAYS);

    // color green
    setStyle("-fx-background-color: transparent;");

    Label Title = new Label(title);
    Label Subtitle = new Label(subtitle);

    // size
    Title.setMinHeight(50);
    Title.setMaxHeight(50);
    Title.setPrefHeight(50);

    Subtitle.setMinHeight(30);
    Subtitle.setMaxHeight(30);
    Subtitle.setPrefHeight(30);

    // text colors
    if(user.getTheme().equals("dark")){
      Title.setStyle(
              "-fx-text-fill: #fff; -fx-text-fill: #fff;-fx-font-size: 30px;"
      );
      Subtitle.setStyle(
              "-fx-text-fill: #fff;-fx-text-fill: #fff;-fx-font-size: 19px;"
      );
    }
    else {
      Title.setStyle(
              "-fx-text-fill: #000000; -fx-text-fill: #000000;-fx-font-size: 30px;"
      );
      Subtitle.setStyle(
              "-fx-text-fill: #000000;-fx-text-fill: #000000;-fx-font-size: 19px;"
      );
    }

    // add
    this.getChildren().add(Title);

    if (subtitle != "") {
      this.getChildren().add(Subtitle);
    }
  }
}
