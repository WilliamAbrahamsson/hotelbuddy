package javaproject.com.UI.views.app.rooms;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javaproject.com.DB.tables.Room;
import javaproject.com.DB.tables.RoomContent;
import javaproject.com.DB.tables.joins.FatRoom;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.components.button;
import javaproject.com.UI.components.dynamictable.DynamicTable;
import javaproject.com.UI.components.popup.Popup;
import javaproject.com.UI.views.app.App;

public class RoomsTable extends DynamicTable {

  User user = App.getUser();

  ArrayList<FatRoom> _rooms = fetchRooms();

  public RoomsTable() {
    String[] headers = { "ID", "Name", "Price", "Contains", "Description" };
    this.addHeaders(headers);
    this.Add_checkboxses_room_contains();
    for (int ix = 0; ix < _rooms.size(); ix++) {
      Room room = _rooms.get(ix).getRoom();

      String is_cleaned = "False";
      if (room.get_cleaned_status() == 1) {
        is_cleaned = "True";
      }
      StringBuilder contents_string = new StringBuilder();
      ArrayList<RoomContent> room_contents = _rooms.get(ix).getRoomContents();
      for (int i = 0; i < room_contents.size(); i++) {

        String bed = "";
        if (room_contents.get(i).getType() == "Bed") {
          bed = "Bed";
        }
        if (i != room_contents.size() - 1) {
          bed += ",";
        }
        contents_string.append(room_contents.get(i).getcontent() + bed + " ");
      }
      String[] data = {
          String.valueOf(room.getId()),
          room.getName(),
          String.valueOf(room.getPrice()),
          contents_string.toString(),
          room.getDesc(),
      };
      this.addItem(data);
    }

    // on pressed, print id of cell
    table.setOnMousePressed(e ->

    {
      String id = table.getSelectionModel().getSelectedItem().get(0);
      int roomId = Integer.parseInt(id);
      // Room detail view
      toggleDetailView(roomId);
    });
  }

  public ArrayList<FatRoom> fetchRooms() {
    try {
      ArrayList<FatRoom> fatrooms = finalRequestHandler.getAllFatRooms();
      return fatrooms;
    } catch (Exception errors) {
      errors.printStackTrace();
    }
    return null;
  }

  public void deleteRoom(int id) {
    try {
      finalRequestHandler.deleteRoom(id);
      Popup.ConfirmDialogBox(
          "You have successfully deleted" + " \n room nr. " + id);
    } catch (Exception errors) {
      errors.printStackTrace();
      Popup.ErrorDialogBox("Deleting failed!" + "\n Please try again.");
    }
  }

  private FatRoom getfatRoom(int id) {
    for (int ix = 0; ix < _rooms.size(); ix++) {
      FatRoom room = _rooms.get(ix);
      if (room.getRoom().getId() == id) {
        return room;
      }
    }
    return null;
  }

  private HBox getRoomContents(Room room) {
    HBox contentsCell = new HBox();
    contentsCell.setAlignment(Pos.CENTER);

    // style and add padding to all sides
    contentsCell.setStyle(
        "-fx-border-color: transparent; -fx-background-color: transparent; -fx-padding: 10 10 10 40;");
    // Arraylist with room contents, separated by comma in string if not null
    ArrayList<String> roomContents = new ArrayList<>(
        Arrays.asList(room.getContents().split(", ")));

    // for each room content
    for (String roomContent : roomContents) {
      // if not null
      if (roomContent == null) {
        continue;
      }

      // create hbox with a label centered
      HBox contentBox = new HBox();

      // style where center text
      if (user.getTheme().equals("dark")) {
        contentBox.setStyle(
            "-fx-border-color: transparent; -fx-border-color: #1E1E26; -fx-background-color: transparent; -fx-border-width: 1.5 1.5 1.5 1.5; -fx-padding: 10; -fx-alignment: center;");
      } else {
        contentBox.setStyle(
            "-fx-border-color: transparent; -fx-border-color: #ffffff; -fx-background-color: transparent; -fx-border-width: 1.5 1.5 1.5 1.5; -fx-padding: 10; -fx-alignment: center;");
      }

      // create label and box
      Label contentLabel = new Label(roomContent);
      contentLabel.setAlignment(Pos.CENTER);
      // white text
      if (user.getTheme().equals("dark")) {
        contentLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12px;");
      } else {
        contentLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 12px;");
      }
      contentBox.getChildren().add(contentLabel);
      contentsCell.getChildren().add(contentBox);
    }
    return contentsCell;
  }

  // open detail view
  public void toggleDetailView(int id) {

    try {
      HBox directionBox = new HBox();
      directionBox.setMinHeight(50);
      directionBox.setStyle(
          "-fx-background-color: transparent; -fx-padding: 10px; -fx-border-color: #444444; -fx-border-width: 0 0 1 0;");

      // center content vertically
      directionBox.setAlignment(Pos.CENTER_LEFT);

      // create back button
      button backButton = new button(
          "Back",
          "green",
          new Insets(12, 25, 12, 25));

      // on pressed, go back to table view
      backButton
          .getButton()
          .setOnMousePressed(e -> {
            backButton
                .getButton()
                .setOnAction((ActionEvent event) -> {
                  this.setTop(searchbar);
                  this.setCenter(table);
                });
          });

      directionBox.getChildren().add(backButton.getButton());

      // create a black square
      VBox contentSquare = new VBox();
      VBox.setVgrow(contentSquare, Priority.ALWAYS);
      HBox.setHgrow(contentSquare, Priority.ALWAYS);
      if (user.getTheme().equals("dark")) {
        contentSquare.setStyle("-fx-background-color: #000000;");
      } else {
        contentSquare.setStyle("-fx-background-color: #ffffff;");
      }

      FatRoom room = getfatRoom(id);

      // get the wrapper box
      VBox descriptionBox = new VBox();
      descriptionBox.setAlignment(Pos.TOP_CENTER);

      HBox nameContentBox = new HBox();

      // header displays user pressed name
      Label nameHeader = new Label(room.getRoom().getName() + ": ");
      if (user.getTheme().equals("dark")) {
        nameHeader
            .setStyle("-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;");
      } else {
        nameHeader
            .setStyle("-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #000000;");
      }
      nameHeader.setAlignment(Pos.CENTER);

      // HBox roomContents = getRoomContents(room.getRoom());

      nameContentBox.getChildren().addAll(nameHeader);
      nameContentBox.setPadding(new Insets(20, 0, 20, 0));
      nameContentBox.setSpacing(10);
      nameContentBox.setAlignment(Pos.CENTER);

      // buttonsbox horizontal
      HBox buttonsBox = new HBox();
      buttonsBox.setSpacing(10);
      buttonsBox.setAlignment(Pos.CENTER);
      buttonsBox.setPadding(new Insets(0, 0, 20, 0));

      // create edit button
      button editButton = new button(
          "Edit",
          "green",
          new Insets(10, 18, 10, 18));

      // delete button
      button deleteButton = new button(
          "Delete",
          "red",
          new Insets(9, 17, 8, 17));

      // on pressed, delete user
      deleteButton
          .getButton()
          .setOnMousePressed(e -> {
            deleteRoom(room.getRoom().getId());
            // toggle table view
            this.setTop(searchbar);
            this.setCenter(table);
          });

      editButton
          .getButton()
          .setOnMousePressed(e -> {
            // open edit user view
            // toggle edit view
            toggleEditView(room, false);
          });

      buttonsBox
          .getChildren()
          .addAll(editButton.getButton(), deleteButton.getButton());
      System.out.println(room.getRoom().get_cleaned_status());

      if (room.getRoom().get_cleaned_status() == 1) {

        button book_cleaning = new button("Book cleaning", "green", new Insets(10, 18, 10, 18));

        book_cleaning.getButton()
            .setOnMousePressed(e -> {
              boolean is_true = finalRequestHandler.set_cleaning_status(0, room.getRoom().getId());
              if (is_true) {
                Label click_text = new Label("Cleaning booked");
                if (user.getTheme().equals("dark")) {
                  click_text.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12px;");
                } else {
                  click_text.setStyle("-fx-text-fill: #000000; -fx-font-size: 12px;");
                }
                _rooms = fetchRooms();
                descriptionBox.getChildren().add(click_text);
              }
            });

        Label cleand = new Label("Room is clean");
        if (user.getTheme().equals("dark")) {
          cleand.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12px;");
        } else {
          cleand.setStyle("-fx-text-fill: #000000; -fx-font-size: 12px;");
        }
        descriptionBox.getChildren().addAll(nameContentBox, buttonsBox, cleand, book_cleaning.getButton());

      } else {

        button is_cleaned = new button("Mark as cleaned", "green", new Insets(10, 18, 10, 18));

        is_cleaned.getButton()
            .setOnMousePressed(e -> {
              boolean is_true = finalRequestHandler.set_cleaning_status(1, room.getRoom().getId());
              if (is_true) {
                Label click_text = new Label("Room marked as clean");
                if (user.getTheme().equals("dark")) {
                  click_text.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12px;");
                } else {
                  click_text.setStyle("-fx-text-fill: #000000; -fx-font-size: 12px;");
                }
                _rooms = fetchRooms();
                descriptionBox.getChildren().add(click_text);
              }
            });

        descriptionBox.getChildren().addAll(nameContentBox, buttonsBox, is_cleaned.getButton());
      }

      contentSquare.getChildren().add(descriptionBox);

      // replace the table with the black square
      this.setCenter(contentSquare);
      this.setTop(directionBox);
    } catch (Exception errors) {
      errors.printStackTrace();
    }
  }

  public void toggleEditView(FatRoom room, Boolean failed) {

    try {
      HBox directionBox = new HBox();
      directionBox.setMinHeight(50);
      directionBox.setStyle(
          "-fx-background-color: transparent; -fx-padding: 10px; -fx-border-color: #444444; -fx-border-width: 0 0 1 0;");

      // center content vertically
      directionBox.setAlignment(Pos.CENTER_LEFT);

      // create back button
      button backButton = new button(
          "Back",
          "green",
          new Insets(12, 25, 12, 25));

      // on pressed, go back to table view
      backButton
          .getButton()
          .setOnMousePressed(e -> {
            backButton
                .getButton()
                .setOnAction((ActionEvent event) -> {
                  toggleDetailView(room.getRoom().getId());
                });
          });

      directionBox.getChildren().add(backButton.getButton());

      // ---------- -Edit user form- ----------//

      // CSS Vars
      String Red_text = "-fx-font-size: 15px; -fx-text-fill: #FF0000;";
      String header_text = "";
      String background_color_black = "";

      if (user.getTheme().equals("dark")) {
        header_text = "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;";
        background_color_black = "-fx-background-color: #000000;";
      } else {
        header_text = "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #000000;";
        background_color_black = "-fx-background-color: #ffffff;";
      }

      VBox allContent = new VBox();
      allContent.setStyle(background_color_black);
      allContent.setAlignment(Pos.TOP_CENTER);

      Label header = new Label("Edit Room");
      header.setStyle(header_text);
      header.setPadding(new Insets(20, 0, 20, 0));
      header.setAlignment(Pos.CENTER);
      HBox fields = new HBox();
      fields.setSpacing(20); // add some spacing between columns
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
      ArrayList<Integer> remove_content = new ArrayList<>();

      ArrayList<RoomContent> content = finalRequestHandler.Fetch_all_room_content();
      HBox content_box = new HBox();
      for (int i = 0; i < content.size(); i++) {
        CheckBox c = new CheckBox(content.get(i).getcontent());
        if (user.getTheme().equals("dark")) {
          c.setStyle("-fx-text-fill: #FFFFFF;");
        } else {
          c.setStyle("-fx-text-fill: #000000;");
        }
        ArrayList<RoomContent> roomcont = room.getRoomContents();

        for (int x = 0; x < roomcont.size(); x++) {

          if (roomcont.get(x).getcontent().equals(content.get(i).getcontent())) {
            c.setSelected(true);
          }
        }
        final int jk = i;
        c.selectedProperty().addListener((o, oldValue, newValue) -> {
          if (newValue) {
            // TICKED
            try {
              remove_content.remove(content.get(jk).getId());
            } catch (Exception e) {
            }
            add_content.add(content.get(jk).getId());
          } else {
            // UnChecked
            try {
              add_content.remove(content.get(jk).getId());
            } catch (Exception e) {
            }
            remove_content.add(content.get(jk).getId());
          }
        });

        content_box.getChildren().addAll(c);
      }
      content_box.setAlignment(Pos.CENTER);
      content_box.setPadding(new Insets(20, 0, 20, 0));

      TextField name = new TextField(room.getRoom().getName());
      name.setFont(new Font(15));
      name.setMinHeight(30);

      TextField description = new TextField(room.getRoom().getDesc());
      description.setFont(new Font(15));
      description.setMinHeight(30);

      TextField price = new TextField(room.getRoom().getPrice() + "");
      price.setFont(new Font(15));
      price.setMinHeight(30);

      VBox spacer = new VBox();
      spacer.setPadding(new Insets(20, 0, 35, 0));
      spacer.setVisible(false);
      button edit = new button(
          "Apply changes",
          "green",
          new Insets(10, 25, 10, 25));

      HBox buttonBox = new HBox();
      buttonBox.getChildren().addAll(edit.getButton());

      // buttonbox transparent background and border
      buttonBox.setStyle(
          "-fx-background-color: transparent; -fx-border-color: transparent;");

      column1.getChildren().addAll(text1, name, text3, price, buttonBox);
      column2.getChildren().addAll(text2, description);

      fields.getChildren().addAll(column1, column2, column3);
      fields.setAlignment(Pos.TOP_CENTER);
      outer_fields.getChildren().addAll(fields, content_box);
      allContent.getChildren().addAll(header, outer_fields);

      // ---------- -End Edit room form- -------//

      if (failed) {
        Label alreadyExists = new Label("Room already exists!");
        text1.setStyle(Red_text);
        text1.setTranslateY(5);
        alreadyExists.setStyle(Red_text);
        column2.getChildren().add(alreadyExists);
        this.setCenter(allContent);
      }

      edit
          .getButton()
          .setOnMousePressed(eedit -> {
            edit
                .getButton()
                .setOnAction((ActionEvent event_edit) -> {
                  if (price.getText() == null ||
                      price.getText().isEmpty() ||
                      price.getText().isBlank()) {
                    try {
                      column2.getChildren().remove(2); // Removes column2 object at index 2 which should be Label
                      // alreadyExists text
                    } catch (Exception ignored) {
                    }

                    Label alreadyExists = new Label("Enter a valid price!");
                    text2.setStyle(Red_text);
                    text2.setTranslateY(5);
                    alreadyExists.setStyle(Red_text);
                    column2.getChildren().add(alreadyExists);
                    this.setCenter(allContent);
                  } else {
                    try {
                      room.getRoom().setName(name.getText());
                      room.getRoom().setDesc(description.getText());

                      String strPrice = price.getText();
                      int intPrice = Integer.parseInt(strPrice);
                      room.getRoom().setPrice(intPrice);
                      Boolean remove_bool = true;
                      for (int i = 0; i < remove_content.size(); i++) {
                        Boolean tmp = finalRequestHandler.removeContent_from_room(room.getRoom().getId(),
                            remove_content.get(i));
                        if (!tmp) {
                          remove_bool = false;
                        }
                      }

                      Boolean add_bool = true;
                      for (int i = 0; i < add_content.size(); i++) {
                        Boolean tmp = finalRequestHandler.addContent_to_room(room.getRoom().getId(),
                            add_content.get(i));
                        if (!tmp) {
                          add_bool = false;
                        }
                      }
                      Boolean bol_test = finalRequestHandler.updateRoom(room.getRoom());
                      if (bol_test || remove_bool || add_bool) {
                        this.setTop(searchbar);
                        this.setCenter(table);
                        refreshPage();
                      } else {
                        toggleEditView(room, true);
                      }
                    } catch (Exception errors) {
                    }
                  }
                  refreshPage();
                });
          });

      this.setCenter(allContent);
      this.setTop(directionBox);
    } catch (Exception errors) {
      errors.printStackTrace();
    }
  }

  // Refresh page (change the view to itself)
  public void refreshPage() {
    App.setView("Rooms");
  }
}
