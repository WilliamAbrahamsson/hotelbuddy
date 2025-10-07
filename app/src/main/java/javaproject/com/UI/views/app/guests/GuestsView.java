package javaproject.com.UI.views.app.guests;

import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.Guest;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.components.button;
import javaproject.com.UI.components.popup.Popup;
import javaproject.com.UI.views.app.App;
import javaproject.com.UI.views.app.View;

public class GuestsView extends View {

  User user = App.getUser();
  RequestHandler finalRequestHandler = App.getDBCON();
  HBox topbar;
  VBox contentTable;
  int rows = 0;
  int open_cell = 0;
  BorderPane borderpane;

  // CSS Vars
  String Red_text = "-fx-font-size: 15px; -fx-text-fill: #FF0000;";
  String checkbox_css =
    "-fx-text-alignment:left; -fx-font-size: 15px; -fx-text-fill: #c3c3c3;";
  String header_text = (user.getTheme().equals("dark")) ?
          "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;" :
          "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #000000;";
  String background_color_black = (user.getTheme().equals("dark")) ? "-fx-background-color: #000000;" : "-fx-background-color: #ffffff;";

  public GuestsView() {
    topbar = getTopBar();
    borderpane = getBorderPane();

    button newButton = new button(
      "New Customer",
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

            Label header = new Label("Register Customer");
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

            TextField FirstName = new TextField();
            FirstName.setFont(new Font(15));
            TextField LastName = new TextField();
            LastName.setFont(new Font(15));
            TextField Phone = new TextField();
            Phone.setFont(new Font(15));
            TextField Email = new TextField();
            Email.setFont(new Font(15));
            TextField Address = new TextField();
            Address.setFont(new Font(15));
            TextField Zip = new TextField();
            Zip.setFont(new Font(15));
            TextField City = new TextField();
            City.setFont(new Font(15));
            TextField Country = new TextField();
            Country.setFont(new Font(15));

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
                FirstName,
                text2,
                LastName,
                text5,
                Address,
                text7,
                City,
                back.getButton()
              );
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
                create.getButton()
              );

            fields.getChildren().addAll(column1, column2);
            fields.setAlignment(Pos.TOP_CENTER);

            allContent.getChildren().addAll(header, fields);
            borderpane.setCenter(allContent);

            // Create guest and Back button on click

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
                      Guest New_Guest = new Guest();
                      New_Guest.setFirstName(FirstName.getText());
                      New_Guest.setLastName(LastName.getText());
                      New_Guest.setPhone(Phone.getText());
                      New_Guest.setEmail(Email.getText());
                      New_Guest.setAddress(Address.getText());
                      New_Guest.setZip(Zip.getText());
                      New_Guest.setCity(City.getText());
                      New_Guest.setCountry(Country.getText());
                      finalRequestHandler.createGuest(New_Guest);
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

    // display guest table
    GuestsTable table = new GuestsTable();
    borderPane.setCenter(table);
  }

  public void refreshPage() {
    App.setView("Guests");
  }
}
