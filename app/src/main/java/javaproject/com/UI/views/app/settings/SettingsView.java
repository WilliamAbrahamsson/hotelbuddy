package javaproject.com.UI.views.app.settings;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.RoomContent;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.components.button;
import javaproject.com.UI.components.layout.SettingsBox;
import javaproject.com.UI.views.app.App;
import javaproject.com.UI.views.app.View;

public class SettingsView extends View {

  HBox topbar;

  User user = App.getUser();
  protected RequestHandler finalRequestHandler = App.getDBCON();

  // constructor
  public SettingsView() {
    
    String color0 = "";
    String color1 = "";

    if (user.getTheme().equals("dark")) {
      color0 = "#000000";
      color1 = "#111111";
    } else {
      color0 = "#c3c3c3";
      color1 = "#ffffff";
    }

    // Checkbox css
    String checkbox_css = "";

    if (user.getTheme().equals("dark")) {
      checkbox_css =
        "-fx-text-alignment:left; -fx-font-size: 15px; -fx-text-fill: #c3c3c3;";
    } else {
      checkbox_css =
        "-fx-text-alignment:left; -fx-font-size: 15px; -fx-text-fill: #545454;";
    }

    topbar = getTopBar();
    updateTopBar(false);

    ScrollPane scroll_pane = new ScrollPane();
    SettingsBox spaceBox = null;

    // background color
    if (user.getTheme().equals("dark")) {
      contentTable.setStyle("-fx-background-color: #111111;");
      spaceBox = new SettingsBox(10, "#000000", "", "", new VBox());
    } else {
      contentTable.setStyle("-fx-background-color: #ffffff;");
      spaceBox = new SettingsBox(10, "#ffffff", "", "", new VBox());
    }

    // Add stuff to userSettingsBox
    button add_content = new button(
      "Add content",
      "grey",
      new Insets(10, 10, 10, 10)
    );
    HBox button_box = new HBox();
    button_box.getChildren().add(add_content.getButton());
    button_box.setPadding(new Insets(5, 0, 0, 0));
    TextField field = new TextField();
    field.setPrefWidth(100);
    field.setMaxWidth(100);

    HBox outer_combox = new HBox();

    ComboBox comboBox = new ComboBox();
    comboBox.getItems().add("Bed");
    comboBox.getItems().add("Other");
    comboBox.getSelectionModel().selectFirst();
    outer_combox.getChildren().add(comboBox);
    outer_combox.setPadding(new Insets(10, 0, 0, 0));

    add_content
      .getButton()
      .setOnMousePressed(e -> {
        add_content
          .getButton()
          .setOnAction((ActionEvent event) -> {
            Boolean done = false;
            try {
              done =
                finalRequestHandler.addContent(
                  field.getText(),
                  comboBox.getValue().toString()
                );
            } catch (Exception e1) {
              e1.printStackTrace();
            }
            if (done) {
              refreshPage();
            }
          });
      });

    ArrayList<RoomContent> content = finalRequestHandler.Fetch_all_room_content();
    HBox content_box = new HBox();
    for (int i = 0; i < content.size(); i++) {
      VBox innerbox = new VBox();
      button c = new button(
        content.get(i).getcontent(),
        "content_red",
        new Insets(5, 5, 5, 5)
      );
      innerbox.getChildren().add(c.getButton());
      final int fi = i;
      c
        .getButton()
        .setOnMousePressed(e -> {
          c
            .getButton()
            .setOnAction((ActionEvent event) -> {
              Boolean done = false;
              try {
                done =
                  finalRequestHandler.removeContent(content.get(fi).getId());
              } catch (Exception e1) {
                e1.printStackTrace();
              }
              if (done) {
                refreshPage();
              }
            });
        });

      content_box.getChildren().addAll(innerbox);
    }
    content_box.setPadding(new Insets(20, 20, 20, 0));
    content_box.setAlignment(Pos.CENTER_LEFT);
    VBox tmp = new VBox();
    tmp.setAlignment(Pos.CENTER_LEFT);
    HBox middle = new HBox();

    // middle center vertically
    middle.setAlignment(Pos.CENTER_LEFT);

    middle.getChildren().addAll(field, outer_combox, button_box);
    tmp.getChildren().addAll(content_box, middle);

    // user settings settingsbox
    SettingsBox userSettingsBox = new SettingsBox(
      400,
      color0,
      "Room Contents",
      "Modify the room contents.",
      new VBox(tmp)
    );

    // hotel information settingsbox
    SettingsBox hotelInfoBox = new SettingsBox(
      400,
      color1,
      "Hotel Information",
      "Information about this hotel.",
      new VBox()
    );

    // color theme settingsbox
    CheckBox ColorCheckBox = new CheckBox("Light theme");
    ColorCheckBox.setStyle(checkbox_css);
    ColorCheckBox.setPadding(new Insets(30, 0, 0, 500));
    ColorCheckBox.setSelected(user.getTheme().equals("light"));

    ColorCheckBox
      .selectedProperty()
      .addListener((o, oldValue, newValue) -> {
        if (newValue) {
          try {
            user.setTheme("light");
            finalRequestHandler.updateUserTheme(user);
            refreshPage();
            App.refreshUser();
            App.loadBars();
          } catch (Exception errors) {
            errors.printStackTrace();
          }
        } else {
          try {
            user.setTheme("dark");
            finalRequestHandler.updateUserTheme(user);
            refreshPage();
            App.refreshUser();
            App.loadBars();
          } catch (Exception errors) {
            errors.printStackTrace();
          }
        }
      });

    SettingsBox ColorSettingsBox = new SettingsBox(
      400,
      color1,
      "Color Theme",
      "Switch between light and dark mode.",
      new VBox(ColorCheckBox)
    );

    // my statistics
    SettingsBox myStatisticsBox = new SettingsBox(
      80,
      color1,
      "My Statistics",
      "View your statistics.",
      new VBox()
    );

    // log out
    SettingsBox logoutBox = new SettingsBox(
      80,
      color0,
      "Log Out",
      "Log out of the application.",
      new VBox()
    );

    // delete account
    SettingsBox deleteAccountBox = new SettingsBox(
      80,
      color1,
      "Delete Account",
      "Delete your account.",
      new VBox()
    );

    // bottom box
    HBox bottomBox = new HBox();
    bottomBox.setPrefWidth(1000);

    // height 15
    bottomBox.setMinHeight(40);

    if (user.getTheme().equals("dark")) {
      bottomBox.setStyle(
        "-fx-background-color: #111111; -fx-border-width: 1 0 0 0; -fx-border-color: #2b2b2b;"
      );
    } else {
      bottomBox.setStyle(
        "-fx-background-color: #ffffff; -fx-border-width: 1 0 0 0; -fx-border-color: #000000;"
      );
    }
    bottomBox.setAlignment(Pos.CENTER);
    bottomBox.setPadding(new Insets(0, 0, 0, 0));
    bottomBox.setSpacing(0);

    // label
    Label copyrightLabel = new Label(
      "Copyright © 2023 Hotelbuddy. All rights reserved."
    );

    if (user.getTheme().equals("dark")) {
      copyrightLabel.setStyle(
        "-fx-text-fill: #ffffff; -fx-font-size: 12px; -fx-font-family: 'Roboto'; -fx-font-weight: 300;"
      );
    } else {
      copyrightLabel.setStyle(
        "-fx-text-fill: #000000; -fx-font-size: 12px; -fx-font-family: 'Roboto'; -fx-font-weight: 300;"
      );
    }

    // add label to bottom box
    bottomBox.getChildren().add(copyrightLabel);

    // userSettingsBox.getChildren().addAll(tmp);

    // add
    contentTable.getChildren().add(spaceBox);
    contentTable.getChildren().add(userSettingsBox);
    // contentTable.getChildren().add(hotelInfoBox);
    contentTable.getChildren().add(ColorSettingsBox);
    // contentTable.getChildren().add(myStatisticsBox);
    // contentTable.getChildren().add(logoutBox);
    // contentTable.getChildren().add(deleteAccountBox);

    // add bottom box to bottom of borderpane

    // disable horizontal scrolling
    scroll_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    // make the scrollpane ONLY vertical
    scroll_pane.setFitToWidth(true);

    // transparent scrollpane
    if (user.getTheme().equals("dark")) {
      scroll_pane.setStyle("-fx-background: #000;");
    } else {
      scroll_pane.setStyle("-fx-background: #ffffff;");
    }

    // remove scrollpane border
    scroll_pane.setBorder(null);
    scroll_pane.getStyleClass().add("edge-to-edge");

    // disable vertical scroll
    scroll_pane.setVbarPolicy(ScrollBarPolicy.NEVER);

    // hide horizontal scrollbar
    scroll_pane.setHbarPolicy(ScrollBarPolicy.NEVER);

    scroll_pane.setContent(contentTable);
    borderPane.setCenter(scroll_pane);
    borderPane.setBottom(bottomBox);
  }

  // Refresh page (change the view to itself)
  public void refreshPage() {
    App.setView("Settings");
  }
}
