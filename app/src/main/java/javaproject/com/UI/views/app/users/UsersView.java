package javaproject.com.UI.views.app.users;

import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.components.button;
import javaproject.com.UI.components.popup.Popup;
import javaproject.com.UI.views.app.App;
import javaproject.com.UI.views.app.View;

public class UsersView extends View {

  User user = App.getUser();
  RequestHandler finalRequestHandler = App.getDBCON();
  // ArrayList<User> _users = fetchUsers();
  HBox topbar;
  VBox contentTable;
  int rows = 0;
  int open_cell = 0;
  BorderPane borderpane;

  // CSS Vars
  String Red_text = "-fx-font-size: 15px; -fx-text-fill: #FF0000;";
  String checkbox_css = (user.getTheme().equals("dark")) ?
    "-fx-text-alignment:left; -fx-font-size: 15px; -fx-text-fill: #c3c3c3;" :
    "-fx-text-alignment:left; -fx-font-size: 15px; -fx-text-fill: #000000;";
  String header_text = (user.getTheme().equals("dark")) ?
    "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;" :
    "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #000000;";
  String background_color_black = (user.getTheme().equals("dark")) ? "-fx-background-color: #000000;" : "-fx-background-color: #ffffff;";

  public UsersView(BorderPane root) {
    topbar = getTopBar();
    borderpane = getBorderPane();
    button newButton = new button(
      "Register Employee",
      "green",
      new Insets(10, 25, 10, 25)
    );
    button refButton = new button(
      "Refresh",
      "transparent",
      new Insets(10, 25, 10, 25)
    );
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
            allContent.setStyle(background_color_black);
            allContent.setAlignment(Pos.TOP_CENTER);

            Label header = new Label("Register Employee");
            header.setStyle(header_text);
            header.setPadding(new Insets(20, 0, 20, 0));
            header.setAlignment(Pos.CENTER);
            HBox fields = new HBox();
            fields.setSpacing(20);
            fields.setStyle(background_color_black);
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

            TextField Username = new TextField();
            Username.setFont(new Font(15));
            Username.setMinHeight(30);
            PasswordField Password = new PasswordField();
            Password.setFont(new Font(15));
            Password.setMinHeight(30);
            TextField FirstName = new TextField();
            FirstName.setFont(new Font(15));
            TextField LastName = new TextField();
            LastName.setFont(new Font(15));
            TextField Phone = new TextField();
            Phone.setFont(new Font(15));
            TextField Email = new TextField();
            Email.setFont(new Font(15));

            CheckBox Admin_checkbox = new CheckBox("Admin");
            Admin_checkbox.setStyle(checkbox_css);
            Admin_checkbox.setPadding(new Insets(10, 0, 10, 0));
            if (!user.privileges().equals("Admin")) {
              Admin_checkbox.setVisible(false);
            }
            CheckBox spacer = new CheckBox("Admin");
            spacer.setStyle(checkbox_css);
            spacer.setPadding(new Insets(10, 0, 10, 0));
            spacer.setVisible(false);
            button create = new button(
              "Create",
              "green",
              new Insets(10, 25, 10, 25)
            );
            button back = new button(
              "Go Back",
              "transparent",
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
                back.getButton()
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
                create.getButton()
              );

            fields.getChildren().addAll(column1, column2);
            fields.setAlignment(Pos.TOP_CENTER);

            allContent.getChildren().addAll(header, fields);
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
                      User New_User = new User();
                      New_User.setUsername(Username.getText());
                      New_User.setPassword(Password.getText());
                      New_User.setFirstName(FirstName.getText());
                      New_User.setLastName(LastName.getText());
                      New_User.setEmail(Email.getText());
                      New_User.setTheme("dark");
                      if (Admin_checkbox.isSelected()) {
                        New_User.setPrivilegesStatus("Admin");
                      } else {
                        New_User.setPrivilegesStatus("User");
                      }
                      Boolean bol_test = finalRequestHandler.createUser(
                        New_User
                      );
                      if (bol_test == true) {
                        borderpane.setTop(topbar);
                        borderpane.setCenter(contentTable);
                        System.out.println(
                          "User: " + Username.getText() + " Created."
                        );
                      } else {
                        Label alreadyExists = new Label(
                          "Username already exists!"
                        );
                        text1.setStyle(Red_text);
                        text1.setTranslateY(5);
                        alreadyExists.setStyle(Red_text);
                        column2.getChildren().add(alreadyExists);
                        borderpane.setCenter(allContent);
                      }
                    } catch (Exception errors) {
                      Popup.ErrorDialogBox(
                        "Something is wrong, adding user failed"
                      );
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

    // display user table
    UsersTable table = new UsersTable();
    borderPane.setCenter(table);
  }

  // Refresh page (change the view to itself)
  public void refreshPage() {
    App.setView("Employees");
  }
}
