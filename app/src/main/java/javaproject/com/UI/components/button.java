package javaproject.com.UI.components;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.views.app.App;

public class button {

  public Button local_button;

  User user = App.getUser();
  final String buttonStyle = (user.getTheme().equals("dark")) ?
    " -fx-border-color: #1E1E26; -fx-alignment: CENTER-LEFT; -fx-background-radius: 0, 0, 0, 0; -fx-font-size: 13px; -fx-text-fill: #ffffff; -fx-font-weight: bold;" :
    " -fx-border-color: #000000; -fx-alignment: CENTER-LEFT; -fx-background-radius: 0, 0, 0, 0; -fx-font-size: 13px; -fx-text-fill: #000000; -fx-font-weight: bold;";
  final String buttonStyleHover = (user.getTheme().equals("dark")) ?
    "-fx-border-color: #1E1E26; -fx-background-color: transparent; -fx-alignment: CENTER-LEFT; -fx-background-radius: 0, 0, 0, 0; -fx-font-size: 13px; -fx-text-fill: #ffffff; -fx-font-weight: bold;" :
    "-fx-border-color: #b9b9b9; -fx-background-color: transparent; -fx-alignment: CENTER-LEFT; -fx-background-radius: 0, 0, 0, 0; -fx-font-size: 13px; -fx-text-fill: #000000; -fx-font-weight: bold;";
  String color;
  String hover_color;

  public button(String text, String bgcolor, Insets padding) {
    color = "";
    hover_color = "";
    local_button = new Button(text);
    local_button.setPadding(padding);
    if (bgcolor == "green") {
      if(user.getTheme().equals("dark")){
        color = "-fx-background-color: #1d1d1d;";
      }
      else {
        color = "-fx-background-color: #b9b9b9;";
      }
    } else if (bgcolor == "grey") {
      if(user.getTheme().equals("dark")){
        color = "-fx-background-color: #1E1E26;";
      }
      else {
        color = "-fx-background-color: #545454;";
      }
    } else if (bgcolor == "green_confirm") {
      color = "-fx-background-color: green;";
    } else if (bgcolor == "red") {
      color =
        "-fx-border-color: transparent; -fx-background-color: #e31617; -fx-alignment: CENTER-LEFT; -fx-background-radius: 0, 0, 0, 0; -fx-font-size: 13px; -fx-text-fill: #ffffff; -fx-font-weight: bold;";
      // hover color little darker
      hover_color =
        "-fx-border-color: transparent; -fx-background-color: #c31617; -fx-alignment: CENTER-LEFT; -fx-background-radius: 0, 0, 0, 0; -fx-font-size: 13px; -fx-text-fill: #ffffff; -fx-font-weight: bold;";
    } else if (bgcolor == "transparent") {
      if(user.getTheme().equals("dark")){
        color =
                "-fx-background-color: transparent; -fx-text-fill: #ffffff; -fx-border-color:#1E1E26; -fx-border-width: 2px; -fx-font-weight: bold; -fx-font-size: 13px;";
        hover_color =
                "-fx-border-color:#1d1d1d; -fx-background-color: #1d1d1d; -fx-background-radius:2px; -fx-border-width: 2px; -fx-font-weight: bold; -fx-font-size: 13px;";
      }
      else {
        color =
                "-fx-background-color: transparent; -fx-text-fill: #000000; -fx-border-color:#b9b9b9; -fx-border-width: 2px; -fx-font-weight: bold; -fx-font-size: 13px;";
        hover_color =
                "-fx-border-color:#545454; -fx-background-color: #545454; -fx-background-radius:2px; -fx-border-width: 2px; -fx-font-weight: bold; -fx-font-size: 13px;";
      }
    } else if (bgcolor == "content_red") {
      if(user.getTheme().equals("dark")){
        color = "-fx-background-color: #1E1E26;";
        hover_color = "-fx-border-color: transparent; -fx-background-color: #c31617;";
      }
      else {
        color = "-fx-background-color: #545454;";
        hover_color = "-fx-border-color: transparent; -fx-background-color: #c31617;";
      }
    }
    local_button.setStyle(buttonStyle + color);

    // on mouse hover, give user a visual feedback
    local_button.setOnMouseEntered(e -> {
      local_button.setStyle(buttonStyleHover + hover_color);
    });

    // on mouse exit, give user a visual feedback
    local_button.setOnMouseExited(e -> {
      local_button.setStyle(buttonStyle + color);
    });
  }

  public Button getButton() {
    return local_button;
  }

  public void setStyleSheet(String sheet) {
    local_button.getStylesheets().add(sheet);
  }

  public void setGraphic(ImageView value) {
    local_button.setGraphic(value);
  }
}
