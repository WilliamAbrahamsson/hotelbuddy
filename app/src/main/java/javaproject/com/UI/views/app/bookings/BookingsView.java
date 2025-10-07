package javaproject.com.UI.views.app.bookings;

import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.*;
import javaproject.com.UI.components.button;
import javaproject.com.UI.views.app.App;
import javaproject.com.UI.views.app.View;

public class BookingsView extends View {

  HBox topbar;
  User user = App.getUser();
  RequestHandler finalRequestHandler = App.getDBCON();
  // ArrayList<FatBooking> fat_bookings = fetchFatBookings();
  VBox contentTable;
  int rows = 0;
  int open_cell = 0;
  BorderPane borderpane;

  // constructor
  public BookingsView() {
    if(user.getTheme().equals("dark")){
      this.getStylesheets().add("/style/DynamicTableStyle.css");
    }
    else {
      this.getStylesheets().add("/style/LightDynamicTableStyle.css");
    }

    borderpane = getBorderPane();
    topbar = getTopBar();

    button newButton = new button(
      "New Booking",
      "green",
      new Insets(10, 25, 10, 25)
    );
    button refButton = new button(
      "Refresh",
      "transparent",
      new Insets(10, 25, 10, 25)
    );

    button checkinOutButton = new button(
      "Checking-In/Out Today",
      "transparent",
      new Insets(10, 25, 10, 25)
    );

    updateTopBar(false);
    topbar.getChildren().add(refButton.getButton());
    topbar.getChildren().add(newButton.getButton());
    topbar.getChildren().add(checkinOutButton.getButton());

    refButton.getButton().toBack();
    newButton.getButton().toBack();
    checkinOutButton.getButton().toBack();

    newButton
      .getButton()
      .setOnMousePressed(e -> {
        newButton
          .getButton()
          .setOnAction((ActionEvent event) -> {
            createNewBooking();
          });
      });

    refButton
      .getButton()
      .setOnMousePressed(e -> {
        refButton
          .getButton()
          .setOnAction((ActionEvent event) -> {
            // On button click function
          });
      });

    checkinOutButton
      .getButton()
      .setOnMousePressed(e -> {
        checkinOutButton
          .getButton()
          .setOnAction((ActionEvent event) -> {
            // On button click function
            App.setView("Check-In/Out");
          });
      });

    // Create an HBox to represent the bottom bar and add the quickBookingButton to it
    HBox bottomBar = new HBox();
    if(user.getTheme().equals("dark")){
      bottomBar.setStyle("-fx-background-color: #000000;");
    }
    else {
      bottomBar.setStyle("-fx-background-color: #ffffff;");
    }

    // bottomBar.setPadding(new Insets(10, 20, 10, 20));
    bottomBar.setAlignment(Pos.CENTER_RIGHT);

    // Add the bottom bar to the borderpane
    borderpane.setBottom(bottomBar);

    BookingsTable table = new BookingsTable();

    borderPane.setCenter(table);
  }

  public void createNewBooking() {
    HBox dateV = new HBox();
    if(user.getTheme().equals("dark")){
      dateV.setStyle("-fx-background-color: #000000;");
    }
    else {
      dateV.setStyle("-fx-background-color: #ffffff;");
    }
    dateV.setSpacing(25);
    dateV.setAlignment(Pos.TOP_CENTER);

    Label header1 = new Label("Specify the dates");
    if(user.getTheme().equals("dark")){
      header1.setStyle(
              "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;"
      );
    }
    else {
      header1.setStyle(
              "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #000000;"
      );
    }
    header1.setPadding(new Insets(20, 0, 20, 0));
    header1.setAlignment(Pos.CENTER);
    /*  Label header2 = new Label("Booking Engine");
    header2.setStyle(
      "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;"
    );
    header2.setPadding(new Insets(20, 0, 20, 0));
    header2.setAlignment(Pos.CENTER); */

    HBox dateFields = new HBox();
    dateFields.setSpacing(20); // add some spacing between columns

    // dateFields background color
    if(user.getTheme().equals("dark")){
      dateFields.setStyle("-fx-background-color: #000000;");
    }
    else {
      dateFields.setStyle("-fx-background-color: #ffffff;");
    }

    // -------- DATE FIELDS -----------

    DatePicker from_date = new DatePicker();
    from_date.setId("from-date-picker");

    DatePicker to_date = new DatePicker();
    to_date.setId("to-date-picker");

    // set default date
    from_date.setValue(LocalDate.now());
    to_date.setValue(LocalDate.now().plusDays(7));

    // -------- DATE FIELDS END -----------

    dateFields.getChildren().addAll(from_date, to_date);

    HBox bottomBox3 = new HBox();
    HBox.setHgrow(bottomBox3, Priority.ALWAYS);
    bottomBox3.setAlignment(Pos.CENTER_RIGHT);

    if(user.getTheme().equals("dark")){
      bottomBox3.setStyle(
              "-fx-border-color: #2b2b2b; -fx-border-width: 1 0 0 0; -fx-padding: 10 20 10 10;"
      );
    }
    else {
      bottomBox3.setStyle(
              "-fx-border-color: #b9b9b9; -fx-border-width: 1 0 0 0; -fx-padding: 10 20 10 10;"
      );
    }

    button show = new button("Show rooms", "green", new Insets(10, 10, 10, 10));

    // add show button to bottom box
    bottomBox3.getChildren().add(show.getButton());

    VBox column1 = new VBox();
    column1.setSpacing(10);
    column1.setAlignment(Pos.TOP_CENTER);

    VBox bookingV = new VBox();

    bookingV.setSpacing(10);
    bookingV.setAlignment(Pos.TOP_CENTER);
    // bookingV.getChildren().add(header2);

    // date selection border pane
    BorderPane dateselectionPane = new BorderPane();

    // width to cover whole x

    column1.getChildren().addAll(header1, dateFields);

    dateV.getChildren().add(column1);
    dateselectionPane.setCenter(dateV);
    dateselectionPane.setBottom(bottomBox3);

    borderpane.setCenter(dateselectionPane);

    show
      .getButton()
      .setOnMousePressed(ecreate -> {
        show
          .getButton()
          .setOnAction((ActionEvent event_create) -> {
            //-----------AFTER DATES IS CHOOSEN----------------

            LocalDate start_date = from_date.getValue();
            LocalDate stop_date = to_date.getValue();

            String start = start_date.toString();
            String stop = stop_date.toString();

            //  ROOMS
            String style = "-fx-font-size: 15px; -fx-text-fill: #c3c3c3;";
            HBox roomList = new HBox();
            if(user.getTheme().equals("dark")){
              roomList.setStyle("-fx-border-color: #131419");
            }
            else {
              roomList.setStyle("-fx-border-color: #ffffff");
            }
            roomList.setAlignment(Pos.CENTER);

            // Where empty rooms should be able to be selected -------------

            BorderPane roomlistWrapper = new BorderPane();

            // extend maximum width and height that is possible
            VBox.setVgrow(roomlistWrapper, Priority.ALWAYS);
            HBox.setHgrow(roomlistWrapper, Priority.ALWAYS);

            // ------------- Room Details -----------

            SelectedRoomsView selectedRoomsView = new SelectedRoomsView(
              start,
              stop
            );

            // add
            roomlistWrapper.setCenter(selectedRoomsView);

            bookingV.getChildren().add(roomlistWrapper);
            borderpane.setCenter(bookingV);
          });
      });
  }

  // Refresh page (change the view to itself)
  public void refreshPage() {
    App.setView("Bookings");
  }
}
