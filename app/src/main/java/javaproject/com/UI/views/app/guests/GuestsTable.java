package javaproject.com.UI.views.app.guests;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javaproject.com.DB.tables.Guest;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.components.button;
import javaproject.com.UI.components.dynamictable.DynamicTable;
import javaproject.com.UI.components.popup.Popup;
import javaproject.com.UI.views.app.App;

public class GuestsTable extends DynamicTable {

  User user = App.getUser();

  ArrayList<Guest> _guests = fetchGuests();

  public GuestsTable() {
    String[] headers = { "ID", "First Name", "Last Name", "Email", "Phone" };
    this.addHeaders(headers);

    for (int ix = 0; ix < _guests.size(); ix++) {
      Guest guest = _guests.get(ix);
      String[] data = {
        String.valueOf(guest.getId()),
        guest.getFirstName(),
        guest.getLastName(),
        guest.getEmail(),
        guest.getPhone(),
      };
      this.addItem(data);
    }

    // on pressed, print id of cell
    table.setOnMousePressed(e -> {
      String id = table.getSelectionModel().getSelectedItem().get(0);
      int guestId = Integer.parseInt(id);
      // Booking detail view
      toggleDetailView(guestId);
    });
  }

  // Get request to fetch all users from database
  public ArrayList<Guest> fetchGuests() {
    try {
      ArrayList<Guest> guests = finalRequestHandler.getGuests();
      return guests;
    } catch (Exception errors) {
      errors.printStackTrace();
    }
    return null;
  }

  public void deleteGuest(int id) {
    try {
      finalRequestHandler.deleteGuest(id);
      Popup.ConfirmDialogBox("You have successfully deleted guest");
    } catch (Exception errors) {
      errors.printStackTrace();
      Popup.ErrorDialogBox("Deleting failed. Please try again.");
    }
  }

  // get user at index
  public Guest getGuest(int id) {
    try {
      Guest guest = finalRequestHandler.getGuest(id);
      return guest;
    } catch (Exception errors) {
      errors.printStackTrace();
    }
    return null;
  }

  // open detail view
  public void toggleDetailView(int id) {
    System.out.println(id);

    try {
      HBox directionBox = new HBox();
      directionBox.setMinHeight(50);
      directionBox.setStyle(
        "-fx-background-color: transparent; -fx-padding: 10px; -fx-border-color: #444444; -fx-border-width: 0 0 1 0;"
      );

      // center content vertically
      directionBox.setAlignment(Pos.CENTER_LEFT);

      // create back button
      button backButton = new button(
        "Back",
        "green",
        new Insets(12, 25, 12, 25)
      );

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
      if(user.getTheme().equals("dark")){
        contentSquare.setStyle("-fx-background-color: #000000;");
      }
      else {
        contentSquare.setStyle("-fx-background-color: #ffffff;");
      }

      Guest guest = getGuest(id);

      // get the wrapper box
      VBox descriptionBox = new VBox();
      descriptionBox.setSpacing(10);
      descriptionBox.setAlignment(Pos.TOP_CENTER);

      // header displays guest pressed name
      Label nameHeader = new Label(
        guest.getFirstName() + " " + guest.getLastName()
      );
      if(user.getTheme().equals("dark")){
        nameHeader.setStyle(
                "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;"
        );
      }
      else {
        nameHeader.setStyle(
                "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #000000;"
        );
      }
      nameHeader.setPadding(new Insets(20, 0, 20, 0));
      nameHeader.setAlignment(Pos.CENTER);

      // buttonsbox horizontal
      HBox buttonsBox = new HBox();
      buttonsBox.setSpacing(10);
      buttonsBox.setAlignment(Pos.CENTER);
      buttonsBox.setPadding(new Insets(0, 0, 20, 0));

      // create edit button
      button editButton = new button(
        "Edit",
        "green",
        new Insets(10, 18, 10, 18)
      );

      // delete button
      button deleteButton = new button(
        "Delete",
        "red",
        new Insets(9, 17, 8, 17)
      );

      // on pressed, delete user
      deleteButton
        .getButton()
        .setOnMousePressed(e -> {
          deleteGuest(id);
          refreshPage();
        });

      editButton
        .getButton()
        .setOnMousePressed(e -> {
          // open edit user view
          // toggle edit view
          toggleEditView(guest, false);
        });

      buttonsBox
        .getChildren()
        .addAll(editButton.getButton(), deleteButton.getButton());

      descriptionBox.getChildren().addAll(nameHeader, buttonsBox);

      contentSquare.getChildren().add(descriptionBox);

      // replace the table with the black square
      this.setCenter(contentSquare);
      this.setTop(directionBox);
    } catch (Exception errors) {
      errors.printStackTrace();
    }
  }

  public void toggleEditView(Guest guest, Boolean failed) {
    System.out.println(guest.getId());

    try {
      HBox directionBox = new HBox();
      directionBox.setMinHeight(50);
      directionBox.setStyle(
        "-fx-background-color: transparent; -fx-padding: 10px; -fx-border-color: #444444; -fx-border-width: 0 0 1 0;"
      );

      // center content vertically
      directionBox.setAlignment(Pos.CENTER_LEFT);

      // create back button
      button backButton = new button(
        "Back",
        "green",
        new Insets(12, 25, 12, 25)
      );

      // on pressed, go back to table view
      backButton
        .getButton()
        .setOnMousePressed(e -> {
          backButton
            .getButton()
            .setOnAction((ActionEvent event) -> {
              toggleDetailView(guest.getId());
            });
        });

      directionBox.getChildren().add(backButton.getButton());

      // ---------- -Edit user form- ----------//

      // CSS Vars
      String Red_text = "-fx-font-size: 15px; -fx-text-fill: #FF0000;";
      String checkbox_css =
        "-fx-text-alignment:left; -fx-font-size: 15px; -fx-text-fill: #c3c3c3;";
      String header_text = "";
      String background_color_black = "";

      if(user.getTheme().equals("dark")){
        header_text =
                "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;";
        background_color_black = "-fx-background-color: #000000;";
      }
      else {
        header_text =
                "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #000000;";
        background_color_black = "-fx-background-color: #ffffff;";
      }

      VBox allContent = new VBox();
      allContent.setStyle(background_color_black);
      allContent.setAlignment(Pos.TOP_CENTER);

      Label header = new Label("Edit Guest");
      header.setStyle(header_text);
      header.setPadding(new Insets(20, 0, 20, 0));
      header.setAlignment(Pos.CENTER);
      HBox fields = new HBox();
      fields.setSpacing(20); // add some spacing between columns
      VBox column1 = new VBox();
      column1.setSpacing(10);
      VBox column2 = new VBox();
      column2.setSpacing(10);

      String style = "";

      if(user.getTheme().equals("dark")){
        style = "-fx-font-size: 15px; -fx-text-fill: #c3c3c3;";
      }
      else {
        style = "-fx-font-size: 15px; -fx-text-fill: #545454;";
      }

      Label text1 = new Label("First Name");
      text1.setStyle(style);
      text1.setTranslateY(5);
      Label text2 = new Label("Last Name");
      text2.setStyle(style);
      text2.setTranslateY(5);
      Label text3 = new Label("Phone");
      text3.setStyle(style);
      text3.setTranslateY(5);
      Label text4 = new Label("Email");
      text4.setStyle(style);
      text4.setTranslateY(5);
      Label text5 = new Label("Address");
      text5.setStyle(style);
      text5.setTranslateY(5);
      Label text6 = new Label("Zip");
      text6.setStyle(style);
      text6.setTranslateY(5);
      Label text7 = new Label("City");
      text7.setStyle(style);
      text7.setTranslateY(5);
      Label text8 = new Label("Country");
      text8.setStyle(style);
      text8.setTranslateY(5);

      TextField FirstName = new TextField(guest.getFirstName());
      FirstName.setFont(new Font(15));
      TextField LastName = new TextField(guest.getLastName());
      LastName.setFont(new Font(15));
      TextField Phone = new TextField(guest.getPhone());
      Phone.setFont(new Font(15));
      TextField Email = new TextField(guest.getEmail());
      Email.setFont(new Font(15));
      TextField Address = new TextField(guest.getAddress());
      Address.setFont(new Font(15));
      TextField Zip = new TextField(guest.getZip());
      Zip.setFont(new Font(15));
      TextField City = new TextField(guest.getCity());
      City.setFont(new Font(15));
      TextField Country = new TextField(guest.getCountry());
      Country.setFont(new Font(15));

      button _edit = new button(
        "Apply changes",
        "green",
        new Insets(10, 25, 10, 25)
      );

      column1
        .getChildren()
        .addAll(text1, FirstName, text2, LastName, text5, Address, text7, City);
      column2
        .getChildren()
        .addAll(
          text3,
          Phone,
          text4,
          Email,
          text6,
          Zip,
          text8,
          Country,
          _edit.getButton()
        );

      fields.getChildren().addAll(column1, column2);
      fields.setAlignment(Pos.TOP_CENTER);

      allContent.getChildren().addAll(header, fields);
      this.setCenter(allContent);

      if (failed) {
        Label alreadyExists = new Label(
          "Guest details overlap with another guest!"
        );
        text1.setStyle(Red_text);
        text1.setTranslateY(5);
        alreadyExists.setStyle(Red_text);
        column2.getChildren().add(alreadyExists);
        this.setCenter(allContent);
      }

      _edit
        .getButton()
        .setOnMousePressed(eedit -> {
          System.out.println("Edit button pressed"); // <--- Add this line

          // print out all the fields
          System.out.println("First Name: " + FirstName.getText());
          System.out.println("Last Name: " + LastName.getText());
          System.out.println("Phone: " + Phone.getText());
          System.out.println("Email: " + Email.getText());
          System.out.println("Address: " + Address.getText());
          System.out.println("Zip: " + Zip.getText());
          System.out.println("City: " + City.getText());
          System.out.println("Country: " + Country.getText());

          _edit
            .getButton()
            .setOnAction((ActionEvent event_create) -> {
              if (
                FirstName.getText() == null ||
                FirstName.getText().isEmpty() ||
                FirstName.getText().isBlank()
              ) {
                try {
                  column2.getChildren().remove(9); // Removes column2 object at index 8 which should be Label alreadyExists text
                } catch (Exception ignored) {}

                Label alreadyExists = new Label("Enter a valid Name!");
                alreadyExists.setStyle(Red_text);
                text2.setStyle(Red_text);
                text2.setTranslateY(5);
                alreadyExists.setStyle(Red_text);
                column2.getChildren().add(alreadyExists);
                this.setCenter(allContent);
              } else {
                try {
                  guest.setFirstName(FirstName.getText());
                  guest.setLastName(LastName.getText());
                  guest.setEmail(Email.getText());
                  guest.setPhone(Phone.getText());
                  guest.setAddress(Address.getText());
                  guest.setZip(Zip.getText());
                  guest.setCity(City.getText());
                  guest.setCountry(Country.getText());

                  Boolean bol_test = finalRequestHandler.updateGuest(guest);
                  if (bol_test) {
                    this.setTop(searchbar);
                    this.setCenter(table);
                    System.out.println(
                      "Guest: " +
                      FirstName.getText() +
                      " " +
                      LastName.getText() +
                      " at id: " +
                      guest.getId() +
                      " edited."
                    );
                    refreshPage();
                  } else {
                    toggleEditView(guest, true);
                  }
                } catch (Exception errors) {
                  errors.printStackTrace();
                }
              }
            });
        });

      // ---------- -End Edit guest form- -------//

      this.setCenter(allContent);
      this.setTop(directionBox);
    } catch (Exception errors) {
      errors.printStackTrace();
    }
  }

  public void refreshPage() {
    App.setView("Guests");
  }
}
