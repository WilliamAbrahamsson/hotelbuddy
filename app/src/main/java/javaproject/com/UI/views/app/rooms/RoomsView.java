package javaproject.com.UI.views.app.rooms;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.Room;
import javaproject.com.DB.tables.RoomContent;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.components.button;
import javaproject.com.UI.components.popup.Popup;
import javaproject.com.UI.views.app.App;
import javaproject.com.UI.views.app.View;

public class RoomsView extends View {

  User user = App.getUser();
  RequestHandler finalRequestHandler = App.getDBCON();
  // ArrayList<Room> _rooms = fetchRooms();
  HBox topbar;
  VBox contentTable;
  int rows = 0;
  int open_cell = 0;
  BorderPane borderpane;
  RoomsTable table;

  // CSS Vars
  String Red_text = "-fx-font-size: 15px; -fx-text-fill: #FF0000;";
  String header_text = (user.getTheme().equals("dark"))
      ? "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;"
      : "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #000000;";
  String background_color_black = (user.getTheme().equals("dark")) ? "-fx-background-color: #000000;"
      : "-fx-background-color: #ffffff;";

  public RoomsView() {
    topbar = getTopBar();
    borderpane = getBorderPane();
    button newButton = new button(
        "Add New Room",
        "green",
        new Insets(10, 25, 10, 25));
    button refButton = new button(
        "Refresh",
        "transparent",
        new Insets(10, 25, 10, 25));
    updateTopBar(false);
    topbar.getChildren().add(newButton.getButton());
    topbar.getChildren().add(refButton.getButton());
    newButton.getButton().toBack();
    refButton.getButton().toBack();

    newButton
        .getButton()
        .setOnMousePressed(e -> {
          newButton
              .getButton()
              .setOnAction((ActionEvent event) -> {
                VBox allContent = new VBox();
                allContent.setPadding(new Insets(0, 0, 0, 10));
                allContent.setStyle(background_color_black);
                allContent.setAlignment(Pos.TOP_CENTER);

                Label header = new Label("Add New Room");
                header.setStyle(header_text);
                header.setPadding(new Insets(20, 0, 20, 0));
                header.setAlignment(Pos.CENTER);
                HBox fields = new HBox();
                fields.setSpacing(20);
                fields.setStyle(background_color_black);
                VBox outer_fields = new VBox();
                VBox column1 = new VBox();
                column1.setSpacing(10);
                VBox column2 = new VBox();
                column2.setSpacing(10);
                VBox column3 = new VBox();
                column3.setSpacing(10);

                String style = "";

                if (user.getTheme().equals("dark")) {
                  style = "-fx-font-size: 15px; -fx-text-fill: #c3c3c3;";
                } else {
                  style = "-fx-font-size: 15px; -fx-text-fill: #545454;";
                }

                Label text1 = new Label("Name");
                text1.setStyle(style);
                text1.setTranslateY(5);
                Label text2 = new Label("Description");
                text2.setStyle(style);
                text2.setTranslateY(5);
                Label text3 = new Label("Price");
                text3.setStyle(style);
                text3.setTranslateY(5);

                ArrayList<Integer> add_content = new ArrayList<>();

                ArrayList<RoomContent> content = finalRequestHandler.Fetch_all_room_content();
                HBox content_box = new HBox();
                for (int i = 0; i < content.size(); i++) {
                  CheckBox c = new CheckBox(content.get(i).getcontent());

                  if (user.getTheme().equals("dark")) {
                    c.setStyle("-fx-text-fill: #FFFFFF;");
                  } else {
                    c.setStyle("-fx-text-fill: #000000;");
                  }

                  final int jk = i;
                  c.selectedProperty().addListener((o, oldValue, newValue) -> {
                    if (newValue) {
                      add_content.add(content.get(jk).getId());
                    } else {
                      add_content.remove(content.get(jk).getId());
                    }
                  });

                  content_box.getChildren().addAll(c);
                }
                content_box.setAlignment(Pos.CENTER);
                content_box.setPadding(new Insets(20, 0, 20, 0));

                TextField name = new TextField();
                name.setFont(new Font(15));
                name.setMinHeight(30);

                TextField description = new TextField();
                description.setFont(new Font(15));
                description.setMinHeight(30);

                TextField price = new TextField();
                price.setFont(new Font(15));
                price.setMinHeight(30);

                VBox spacer = new VBox();
                spacer.setPadding(new Insets(20, 0, 35, 0));
                spacer.setVisible(false);
                button create = new button(
                    "Create",
                    "green",
                    new Insets(10, 25, 10, 25));
                button back = new button(
                    "Go Back",
                    "transparent",
                    new Insets(10, 25, 10, 25));

                HBox buttonBox = new HBox();
                buttonBox
                    .getChildren()
                    .addAll(back.getButton(), create.getButton());

                // buttonbox transparent background and border
                buttonBox.setStyle(
                    "-fx-background-color: transparent; -fx-border-color: transparent;");

                column1.getChildren().addAll(text1, name, text3, price, buttonBox);
                column2.getChildren().addAll(text2, description);

                fields.getChildren().addAll(column1, column2, column3);
                fields.setAlignment(Pos.TOP_CENTER);
                outer_fields.getChildren().addAll(fields, content_box);

                allContent.getChildren().addAll(header, outer_fields);
                borderpane.setCenter(allContent);

                // Create user and Back button on click

                // Back Button on click
                back
                    .getButton()
                    .setOnMousePressed(eback -> {
                      back
                          .getButton()
                          .setOnAction((ActionEvent event_back) -> {
                            borderpane.setTop(topbar);
                            borderpane.setCenter(contentTable);
                            refreshPage();
                          });
                    });

                create
                    .getButton()
                    .setOnMousePressed(ecreate -> {
                      create
                          .getButton()
                          .setOnAction((ActionEvent event_create) -> {
                            try {
                              Room new_room = new Room();

                              new_room.setName(name.getText());
                              new_room.setDesc(description.getText());

                              String strPrice = price.getText();
                              int intPrice = Integer.parseInt(strPrice);
                              new_room.setPrice(intPrice);
                              int room_id = finalRequestHandler.createRoom_with_id(
                                  new_room);
                              Boolean add_bool = true;
                              if (room_id != 0) {
                                for (int i = 0; i < add_content.size(); i++) {
                                  Boolean tmp = finalRequestHandler.addContent_to_room(room_id,
                                      add_content.get(i));
                                  if (!tmp) {
                                    add_bool = false;
                                  }
                                }
                              }
                              if (room_id != 0 || add_bool) {
                                borderpane.setTop(topbar);
                                borderpane.setCenter(contentTable);
                                Popup.ConfirmDialogBox("Room Creation Successful");
                              } else {
                                Label alreadyExists = new Label("Room already exists!");
                                text1.setStyle(Red_text);
                                text1.setTranslateY(5);
                                alreadyExists.setStyle(Red_text);
                                column2.getChildren().add(alreadyExists);
                                borderpane.setCenter(allContent);
                              }
                            } catch (Exception errors) {
                              Popup.ErrorDialogBox(
                                  "Something went wrong!" + "\n Room creation failed!");
                            }
                            refreshPage();
                          });
                    });
              });
        });

    refButton
        .getButton()
        .setOnMousePressed(e -> {
          refButton
              .getButton()
              .setOnAction((ActionEvent event) -> {
                refreshPage();
              });
        });
    refButton.getButton().toBack();

    // Display the table
    table = new RoomsTable();
    borderPane.setCenter(table);
  }

  public RoomsView toggleDetailView(int id) {
    table.toggleDetailView(id);
    return this;
  }

  // Refresh page (change the view to itself)
  public void refreshPage() {
    App.setView("Rooms");
  }
}
