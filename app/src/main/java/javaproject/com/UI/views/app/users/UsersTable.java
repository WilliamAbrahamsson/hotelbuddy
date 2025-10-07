package javaproject.com.UI.views.app.users;

import java.util.ArrayList;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.components.button;
import javaproject.com.UI.components.dynamictable.DynamicTable;
import javaproject.com.UI.components.popup.Popup;
import javaproject.com.UI.views.app.App;

public class UsersTable extends DynamicTable {

  ArrayList<User> _users = fetchUsers();
  User AppUser = App.getUser();

  public UsersTable() {
    String[] headers = {
      "ID",
      "Username",
      "First Name",
      "Last Name",
      "Status",
    };
    this.addHeaders(headers);

    for (int ix = 0; ix < _users.size(); ix++) {
      User user = _users.get(ix);
      String[] data = {
        String.valueOf(user.getId()),
        user.getUsername(),
        user.getFirstName(),
        user.getLastName(),
        user.privileges(),
      };
      this.addItem(data);
    }

    // on pressed, print id of cell
    table.setOnMousePressed(e -> {
      String id = table.getSelectionModel().getSelectedItem().get(0);
      int userId = Integer.parseInt(id);
      // Booking detail view
      toggleDetailView(userId);
    });
  }

  // Get request to fetch all users from database
  public ArrayList<User> fetchUsers() {
    try {
      ArrayList<User> users = finalRequestHandler.getUsers();
      return users;
    } catch (Exception errors) {
      errors.printStackTrace();
    }
    return null;
  }

  // Delete user at index
  public void deleteUser(int id) {
    try {
      finalRequestHandler.deleteUser(id);
      Popup.ConfirmDialogBox("You have successfully deleted user");
    } catch (Exception errors) {
      errors.printStackTrace();
      Popup.ErrorDialogBox("Deleting failed. Please try again.");
    }
  }

  // get user at index
  public User getUser(int id) {
    try {
      User user = finalRequestHandler.getUser(id);
      return user;
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
      if(AppUser.getTheme().equals("dark")){
        contentSquare.setStyle("-fx-background-color: #000000;");
      }
      else {
        contentSquare.setStyle("-fx-background-color: #ffffff;");
      }

      User user = getUser(id);

      // get the wrapper box
      VBox descriptionBox = new VBox();
      descriptionBox.setSpacing(10);
      descriptionBox.setAlignment(Pos.TOP_CENTER);

      // header displays user pressed name
      Label nameHeader = new Label(
        user.getFirstName() + " " + user.getLastName()
      );
      if(AppUser.getTheme().equals("dark")){
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
          deleteUser(id);
          refreshPage();
        });

      editButton
        .getButton()
        .setOnMousePressed(e -> {
          // open edit user view
          // toggle edit view
          toggleEditView(user, false);
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

  public void toggleEditView(User user, Boolean failed) {
    System.out.println(user.getId());

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
              toggleDetailView(user.getId());
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

      if(AppUser.getTheme().equals("dark")){
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

      Label header = new Label("Edit Employee");
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

      if(AppUser.getTheme().equals("dark")){
        style = "-fx-font-size: 15px; -fx-text-fill: #c3c3c3;";
      }
      else {
        style = "-fx-font-size: 15px; -fx-text-fill: #545454;";
      }

      Label text1 = new Label("Username");
      text1.setStyle(style);
      text1.setTranslateY(5);
      Label text2 = new Label("Password");
      text2.setStyle(style);
      text2.setTranslateY(5);
      Label text3 = new Label("First Name");
      text3.setStyle(style);
      text3.setTranslateY(5);
      Label text4 = new Label("Last Name");
      text4.setStyle(style);
      text4.setTranslateY(5);
      Label text5 = new Label("Phone");
      text5.setStyle(style);
      text5.setTranslateY(5);
      Label text6 = new Label("Email");
      text6.setStyle(style);
      text6.setTranslateY(5);

      TextField Username = new TextField(user.getUsername());
      Username.setFont(new Font(15));
      Username.setMinHeight(30);
      PasswordField Password = new PasswordField();
      Password.setFont(new Font(15));
      Password.setMinHeight(30);
      TextField FirstName = new TextField(user.getFirstName());
      FirstName.setFont(new Font(15));
      TextField LastName = new TextField(user.getLastName());
      LastName.setFont(new Font(15));
      TextField Phone = new TextField();
      Phone.setFont(new Font(15));
      TextField Email = new TextField(user.getEmail());
      Email.setFont(new Font(15));

      CheckBox Admin_checkbox = new CheckBox("Admin");
      if (Objects.equals(user.privileges(), "Admin")) {
        Admin_checkbox.setSelected(true);
      }
      Admin_checkbox.setStyle(checkbox_css);
      Admin_checkbox.setPadding(new Insets(10, 0, 10, 0));

      CheckBox spacer = new CheckBox("Admin");
      spacer.setStyle(checkbox_css);
      spacer.setPadding(new Insets(10, 0, 10, 0));
      spacer.setVisible(false);
      button _edit = new button(
        "Apply changes",
        "green",
        new Insets(10, 25, 10, 25)
      );

      column1
        .getChildren()
        .addAll(
          text1,
          Username,
          text3,
          FirstName,
          text5,
          Phone,
          Admin_checkbox,
          spacer
        );
      column2
        .getChildren()
        .addAll(
          text2,
          Password,
          text4,
          LastName,
          text6,
          Email,
          spacer,
          _edit.getButton()
        );

      fields.getChildren().addAll(column1, column2);
      fields.setAlignment(Pos.TOP_CENTER);

      allContent.getChildren().addAll(header, fields);
      this.setCenter(allContent);

      if (failed) {
        Label alreadyExists = new Label("Username already exists!");
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

          // print out all the firleds
          System.out.println("Username: " + Username.getText());
          System.out.println("Password: " + Password.getText());
          System.out.println("First Name: " + FirstName.getText());
          System.out.println("Last Name: " + LastName.getText());
          System.out.println("Phone: " + Phone.getText());
          System.out.println("Email: " + Email.getText());
          System.out.println("Admin: " + Admin_checkbox.isSelected());

          _edit
            .getButton()
            .setOnAction((ActionEvent event_create) -> {
              if (
                Password.getText() == null ||
                Password.getText().isEmpty() ||
                Password.getText().isBlank()
              ) {
                try {
                  column2.getChildren().remove(8); // Removes column2 object at index 8 which should be Label alreadyExists text
                } catch (Exception ignored) {}

                Label alreadyExists = new Label("Enter a valid password!");
                alreadyExists.setStyle(Red_text);
                text2.setStyle(Red_text);
                text2.setTranslateY(5);
                alreadyExists.setStyle(Red_text);
                column2.getChildren().add(alreadyExists);
                this.setCenter(allContent);
              } else {
                try {
                  user.setUsername(Username.getText());
                  user.setPassword(Password.getText());
                  user.setFirstName(FirstName.getText());
                  user.setLastName(LastName.getText());
                  user.setEmail(Email.getText());

                  if (Admin_checkbox.isSelected()) {
                    user.setPrivilegesStatus("Admin");
                  } else {
                    user.setPrivilegesStatus("User");
                  }

                  Boolean bol_test = finalRequestHandler.updateUser(user);
                  if (bol_test) {
                    this.setTop(searchbar);
                    this.setCenter(table);
                    System.out.println(
                      "User: " +
                      Username.getText() +
                      " at id: " +
                      user.getId() +
                      " edited."
                    );
                    refreshPage();
                  } else {
                    toggleEditView(user, true);
                  }
                } catch (Exception errors) {
                  errors.printStackTrace();
                }
              }
            });
        });

      // ---------- -End Edit user form- -------//

      this.setCenter(allContent);
      this.setTop(directionBox);
    } catch (Exception errors) {
      errors.printStackTrace();
    }
  }

  public void refreshPage() {
    App.setView("Employees");
  }
}
