package javaproject.com.UI.views.app;

import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.components.button;
import javaproject.com.UI.components.topbar.LogoButton;

public class View extends Region {

  // initialize topbar
  public static HBox topbar = new HBox();

  protected VBox contentTable = new VBox();

  // create scrollpane
  protected ScrollPane scrollPane = new ScrollPane();

  // initialize borderpane
  protected BorderPane borderPane = new BorderPane();

  static User user = App.getUser();

  // constructor
  public View() {
    // size and style
    borderPane.prefWidthProperty().bind(widthProperty());
    borderPane.maxWidthProperty().bind(widthProperty());
    borderPane.minWidthProperty().bind(widthProperty());

    borderPane.prefHeightProperty().bind(heightProperty());
    borderPane.maxHeightProperty().bind(heightProperty());
    borderPane.minHeightProperty().bind(heightProperty());

    if(user.getTheme().equals("dark")){
      borderPane.getStylesheets().add("/style/ViewStyle.css");
    }
    else {
      borderPane.getStylesheets().add("/style/LightViewStyle.css");
    }

    LogoButton logoButton = new LogoButton();
    topbar.getChildren().add(logoButton.getButton());
    topbar.setAlignment(Pos.CENTER_RIGHT);
    topbar.setId("topbar");

    if(user.getTheme().equals("dark")){
      topbar.setStyle(
              "-fx-border-color: #2b2b2b; -fx-background-color: #111111; -fx-padding: 11 20 10 0;"
      );
    }
    else {
      topbar.setStyle(
              "-fx-border-color: #545454; -fx-background-color: #ffffff; -fx-padding: 11 20 10 0;"
      );
    }

    // Never change height of topbar, lock it
    topbar.setMinHeight(65);
    topbar.setMaxHeight(65);
    topbar.setPrefHeight(65);

    borderPane.setTop(topbar);

    // set background color
    if(user.getTheme().equals("dark")){
      contentTable.setStyle("-fx-background-color: #000000;");
    }
    else {
      contentTable.setStyle("-fx-background-color: #ffffff;");
    }

    // add scrollpane to borderpane
    borderPane.setCenter(scrollPane);

    getChildren().add(borderPane);
  }

  public HBox getTopBar() {
    return topbar;
  }

  public BorderPane getBorderPane() {
    return borderPane;
  }

  public static void updateTopBar(boolean controls) {
    // get old logobutton
    Node oldLogoButton = topbar
      .getChildren()
      .get(topbar.getChildren().size() - 1);

    topbar.getChildren().clear();

    // controls true
    if (controls) {
      button settingsButton = new button(
        "Preferences",
        "green",
        new Insets(10, 25, 10, 25)
      );
      button logoutButton = new button(
        "Log out",
        "transparent",
        new Insets(10, 25, 10, 25)
      );

      topbar.getChildren().add(logoutButton.getButton());
      topbar.getChildren().add(settingsButton.getButton());

      //onpressed logout
      logoutButton
        .getButton()
        .setOnAction(e -> {
          // logout
          App.logout();
        });

      // add css stylesheet
      if(user.getTheme().equals("dark")){
        settingsButton.setStyleSheet("/style/LogoButtonStyle.css");
      }
      else {
        settingsButton.setStyleSheet("/style/LightLogoButtonStyle.css");
      }

      // onpressed settings
      settingsButton
        .getButton()
        .setOnAction(e -> {
          // contextMenu.show(settingsButton.getButton(), Side.BOTTOM, -3, 18);
          App.setView("Settings");
          App.removeSidebarSelection();
        });
    }
    topbar.getChildren().add(oldLogoButton);
  }
}
