package javaproject.com.UI.components.floatingchoices;

import javafx.scene.control.Button;

public class FloatingButton extends Button {

  public FloatingButton(String text) {
    // generate circular button with icon
    super(text);
    setStyle(
      "-fx-background-radius: 50em; " +
      "-fx-min-width: 50px; " +
      "-fx-min-height: 50px; " +
      "-fx-max-width: 50px; " +
      "-fx-max-height: 50px;" +
      "fx-background-color: #00bfff;"
    );
  }
}
