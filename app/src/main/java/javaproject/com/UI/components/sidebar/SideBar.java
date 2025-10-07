package javaproject.com.UI.components.sidebar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.views.app.App;

public class SideBar extends BorderPane {

  static SideBarButtons buttons;

  String transparent_css = "-fx-background-color: transparent;";

  User user = App.getUser();

  public SideBar() {
    buttons = new SideBarButtons();

    //add the stylesheet to the scene
    if(user.getTheme().equals("dark")){
      getStylesheets().add("/style/SideBarStyle.css");
    }
    else {
      getStylesheets().add("/style/LightSideBarStyle.css");
    }


    // Vbox on bottom of sidebar that will hold the logo
    VBox logoBox = new VBox();
    logoBox.setAlignment(Pos.BOTTOM_CENTER);

    // Logo
    ImageView logoView = new ImageView();
    Image logo = null;
    if(user.getTheme().equals("dark")){
      logo = new Image("/textlogo.png");
    }
    else {
      logo = new Image("/dark_textlogo.png");
    }
    logoView.setImage(logo);

    // make logo fit the sidebar
    logoView.setFitWidth(200);
    logoView.setPreserveRatio(true);
    logoView.setSmooth(true);
    logoView.setCache(true);

    logoBox.getChildren().add(logoView);
    //logoBox.setStyle("-fx-border-width: 1px 0 0 0; -fx-border-color: #444444;");
    logoBox.setId("logoBox");

    // add margin at the bottom of logo
    logoBox.setMargin(logoView, new Insets(0, 0, 20, 0));

    // add SideBarButtons

    // logout button
    Button logoutButton = new Button();
    Image logoutImage;
    if(user.getTheme().equals("dark")){
      logoutImage = new Image("/menu/logout.png");
    }
    else {
      logoutImage = new Image("/menu/dark_logout.png");
    }

    ImageView logoutImageView = new ImageView(logoutImage);
    logoutImageView.setFitWidth(38);
    logoutImageView.setFitHeight(38);
    logoutButton.setGraphic(logoutImageView);

    // on click
    logoutButton.setOnAction(e -> {
      App.logout();
    });

    // on mouse enter and exit
    if(user.getTheme().equals("dark")){
      logoutButton.setOnMouseEntered(e -> {
        logoutButton.setStyle("-fx-background-color: #171717;");
      });
    }
    else {
      logoutButton.setOnMouseEntered(e -> {
        logoutButton.setStyle("-fx-background-color: #b9b9b9;");
      });
    }

    logoutButton.setOnMouseExited(e -> {
      logoutButton.setStyle(transparent_css);
    });

    // settings button
    Button settingsButton = new Button();
    Image settingsImage;
    if(user.getTheme().equals("dark")){
      settingsImage = new Image("/menu/settings.png");
    }
    else {
      settingsImage = new Image("/menu/dark_settings.png");
    }
    ImageView settingsImageView = new ImageView(settingsImage);

    settingsImageView.setFitWidth(38);
    settingsImageView.setFitHeight(38);
    settingsButton.setGraphic(settingsImageView);

    // on click
    settingsButton.setOnAction(e -> {
      App.setView("Settings");
      buttons.removeSelection();
    });

    // on mouse enter and exit
    if(user.getTheme().equals("dark")){
      settingsButton.setOnMouseEntered(e -> {
        settingsButton.setStyle("-fx-background-color: #171717;");
      });
    }
    else {
      settingsButton.setOnMouseEntered(e -> {
        settingsButton.setStyle("-fx-background-color: #b9b9b9;");
      });
    }

    settingsButton.setOnMouseExited(e -> {
      settingsButton.setStyle(transparent_css);
    });

    // transparent buttons
    logoutButton.setStyle(transparent_css);
    settingsButton.setStyle(transparent_css);

   

    HBox controlButtons = new HBox();
    controlButtons.setAlignment(Pos.BOTTOM_CENTER);
    controlButtons.getChildren().addAll(logoutButton, settingsButton);

    // Add the buttons and logo to the BorderPane
    setCenter(controlButtons);
    setTop(buttons);
    setBottom(logoBox);

    // id for css
    setId("sideBar");
  }

  public static void removeSelection() {
    buttons.removeSelection();
  }

  public static void pressButton(String buttonName) {
    buttons.setSelection(buttonName);
  }
}
