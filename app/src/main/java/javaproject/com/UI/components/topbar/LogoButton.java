package javaproject.com.UI.components.topbar;

import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.views.app.App;
import javaproject.com.UI.views.app.View;

public class LogoButton {

  Button button;
  User user = App.getUser();
  char firstChar;
  boolean isClicked = false;

  public LogoButton() {
    Button newButton = new Button();
    newButton.setShape(new Circle(18));
    newButton.setMinSize(2 * 18, 2 * 18);
    newButton.setMaxSize(2 * 18, 2 * 18);

    firstChar = user.getFirstName().charAt(0);
    newButton.setText(Character.toString(firstChar));

    if(user.getTheme().equals("dark")){
      newButton.getStylesheets().add("/style/LogoButtonStyle.css");
    }
    else {
      newButton.getStylesheets().add("/style/LightLogoButtonStyle.css");
    }

    newButton.setId("Button");

    // LogoButton
    newButton.setStyle(user.getColor());
    button = newButton;

    button.setOnMousePressed(event -> {
      if (isClicked == false) {
        isClicked = true;
        View.updateTopBar(true);
      } else {
        isClicked = false;
        View.updateTopBar(false);
        App.setView(App.getCurrentView());
      }
    });

    button.setOnMouseEntered(e -> {
      newButton.setId("Buttonhover");
    });

    button.setOnMouseExited(e -> {
      newButton.setId("Button");
    });
  }

  public Button getButton() {
    return button;
  }
}
