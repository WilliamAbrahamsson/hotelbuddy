package javaproject.com.UI.views.app.bookings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.Guest;
import javaproject.com.DB.tables.RoomBooking;
import javaproject.com.DB.tables.RoomContent;
import javaproject.com.DB.tables.joins.FatRoom;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.components.PDF.PDF;
import javaproject.com.UI.components.button;
import javaproject.com.UI.views.app.App;
import java.time.temporal.ChronoUnit;

public class SelectedRoomsView extends BorderPane {

  User user = App.getUser();

  ArrayList<SelectedRoom> selectedRooms = new ArrayList<SelectedRoom>();
  VBox roomWrapper = new VBox();
  Node previousPage;
  Node btn;
  RequestHandler finalRequestHandler = App.getDBCON();

  // price label
  int price = 0;
  Label totalPriceLabel;

  // room count label
  int room_count = 0;
  Label roomCountLabel;

  // global dates.
  String startDate;
  String endDate;

  public SelectedRoomsView(String startDate, String endDate) {
    this.getStylesheets().add("/style/DynamicTableStyle.css");

    // full size
    this.setMaxWidth(Double.MAX_VALUE);
    this.setMaxHeight(Double.MAX_VALUE);

    // setting global dates equal to params.
    this.startDate = startDate;
    this.endDate = endDate;

    this.setCenter(getScrollPane());

    // add the top section
    this.setTop(getTopSection());

    HBox bottomBox = new HBox();

    // width 100% of wrapper
    HBox.setHgrow(bottomBox, Priority.ALWAYS);
    bottomBox.setAlignment(Pos.CENTER_RIGHT);

    // add top border
    bottomBox.setStyle(
      "-fx-border-color: #2b2b2b; -fx-border-width: 1 0 0 0; -fx-padding: 10 20 10 10;"
    );

    button continueButton = new button(
      "Continue",
      "green",
      new Insets(10, 10, 10, 10)
    );

    bottomBox.getChildren().addAll(continueButton.getButton());
    this.setBottom(bottomBox);

    // when button pressed, print the rooms selected
    continueButton
      .getButton()
      .setOnAction((ActionEvent ei) -> {
        guestInformationView(startDate, endDate);
        this.setBottom(null);
        this.setTop(null);
      });
  }

  public ScrollPane getScrollPane() {
    ScrollPane scrollpane = new ScrollPane();

    // margin top
    scrollpane.setPadding(new Insets(30, 20, 20, 20));

    // disable horizontal scrolling
    scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    // make the scrollpane ONLY vertical
    scrollpane.setFitToWidth(true);

    scrollpane.setContent(roomWrapper);

    // transparent scrollpane
    if(user.getTheme().equals("dark")){
      scrollpane.setStyle("-fx-background: #000;");
    }
    else {
      scrollpane.setStyle("-fx-background: #ffffff;");
    }

    // remove scrollpane border
    scrollpane.setBorder(null);
    scrollpane.getStyleClass().add("edge-to-edge");

    // disable vertical scroll
    scrollpane.setVbarPolicy(ScrollBarPolicy.NEVER);

    // hide horizontal scrollbar
    scrollpane.setHbarPolicy(ScrollBarPolicy.NEVER);

    return scrollpane;
  }

  // add actual room
  public void addRoom(FatRoom room) {
    HBox roomCell = new HBox();

    roomCell.setStyle("-fx-pref-height: 150px; -fx-spacing: 5;");

    // adds space between elements
    roomCell.setPadding(new Insets(0, 0, 5, 0));

    // remove horizontal spacing
    roomCell.setSpacing(0);

    // ------ INFO CELL ------ //
    VBox infoCell = new VBox();

    infoCell.setPadding(new Insets(0, 0, 0, 20));
    infoCell.setAlignment(Pos.CENTER_LEFT);
    if(user.getTheme().equals("dark")){
      infoCell.setStyle("-fx-background-color: #111;");
    }
    else {
      infoCell.setStyle("-fx-background-color: #ffffff;");
    }

    // padding between elements
    infoCell.setSpacing(14);

    // pref width
    infoCell.setPrefWidth(230);
    infoCell.setMinWidth(230);
    infoCell.setMaxWidth(230);

    // hgrow
    HBox.setHgrow(infoCell, Priority.ALWAYS);

    // name label
    Label roomName = new Label(room.getRoom().getName());
    roomName.setFont(new Font("Arial", 18));

    // price label
    Label roomPrice = new Label("$" + room.getRoom().getPrice());

    // details button
    button detailsButton = new button(
      "Details",
      "transparent",
      new Insets(10, 10, 10, 10)
    );

    // add to info cell
    infoCell
      .getChildren()
      .addAll(roomName, roomPrice, detailsButton.getButton());

    // on pressed
    detailsButton
      .getButton()
      .setOnAction((ActionEvent ei) -> {
        /*   // go to rooms view and toggle detail view function
        View view = App.setView("Rooms");
        RoomsView roomsView = (RoomsView) view;
        roomsView.toggleDetailView(4); */

      });

    // ------ CONTENTS CELL ------//
    VBox contentCell = new VBox();
    contentCell.setPadding(new Insets(0, 0, 0, 20));
    contentCell.setAlignment(Pos.CENTER_LEFT);
    if(user.getTheme().equals("dark")){
      contentCell.setStyle("-fx-background-color: #111;");
    }
    else {
      contentCell.setStyle("-fx-background-color: #ffffff;");
    }

    // width
    contentCell.setPrefWidth(170);
    contentCell.setMinWidth(170);

    // new grid that prioritizes horizontally then expands vertically
    GridPane grid = new GridPane();

    // position top left
    grid.setAlignment(Pos.TOP_LEFT);

    grid.setHgap(10);
    grid.setVgap(20);
    grid.setPadding(new Insets(0, 0, 0, 20));
    grid.setAlignment(Pos.CENTER_LEFT);

    ArrayList<RoomContent> contents = room.getRoomContents();

    int row = 0; // the current row in the grid
    int col = 0; // the current column in the grid

    // add image
    Image squareImage = new Image("/bookings/square.png");
    Image bedImage = new Image("/bookings/bed.png");

    for (RoomContent content : contents) {
      HBox labelHolder = new HBox();

      Label label = new Label(content.getcontent());

      if (content.getcontent().contains("Bed")) {
        ImageView bedImageView = new ImageView(bedImage);

        bedImageView.setFitHeight(20);
        bedImageView.setFitWidth(20);
        labelHolder.getChildren().addAll(bedImageView, label);
        grid.add(labelHolder, col, row);
      } else {
        ImageView squareImageView = new ImageView(squareImage);

        squareImageView.setFitHeight(20);
        squareImageView.setFitWidth(20);
        labelHolder.getChildren().addAll(squareImageView, label);
        grid.add(labelHolder, col, row);
      }

      col++;
      if (col == 3) {
        // start a new row
        col = 0;
        row++;
      }
    }

    // add grid to content cell
    contentCell.getChildren().add(grid);

    // hgrow
    HBox.setHgrow(contentCell, Priority.ALWAYS);

    // ------ DATE CELL ------//
    VBox dateCell = new VBox();
    dateCell.setPadding(new Insets(0, 20, 0, 10));
    dateCell.setAlignment(Pos.CENTER_LEFT);
    if(user.getTheme().equals("dark")){
      dateCell.setStyle("-fx-background-color: #111;");
    }
    else {
      dateCell.setStyle("-fx-background-color: #ffffff;");
    }

    // width
    dateCell.setPrefWidth(300);
    dateCell.setMinWidth(300);
    dateCell.setMaxWidth(300);
    // hgrow
    HBox.setHgrow(dateCell, Priority.ALWAYS);

    VBox dateWrapper = new VBox();

    // space between elements
    dateWrapper.setSpacing(20);

    dateWrapper.setPadding(new Insets(0, 0, 0, 20));
    dateWrapper.setAlignment(Pos.CENTER_LEFT);

    HBox datefieldBox = new HBox();

    VBox fromFieldWrapper = new VBox();

    fromFieldWrapper.setSpacing(5);

    // from label
    Label fromLabel = new Label("From");
    fromLabel.setFont(new Font("Arial", 12));

    // from date field
    DatePicker _from_date = new DatePicker();
    _from_date.setId("from-date-picker2");

    // turn string into date
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fromDate_Formatted = LocalDate.parse(startDate, formatter);
    _from_date.setValue(fromDate_Formatted);
    fromFieldWrapper.getChildren().addAll(fromLabel, _from_date);

    VBox toFieldWrapper = new VBox();

    toFieldWrapper.setSpacing(5);

    // to label
    Label toLabel = new Label("To");
    toLabel.setFont(new Font("Arial", 12));

    // to date field
    DatePicker _to_date = new DatePicker();
    _to_date.setId("to-date-picker");

    // to date
    LocalDate toDate_Formatted = LocalDate.parse(endDate, formatter);
    _to_date.setValue(toDate_Formatted);

    toFieldWrapper.getChildren().addAll(toLabel, _to_date);

    datefieldBox.getChildren().addAll(fromFieldWrapper, toFieldWrapper);

    // delete button
    button deleteButton = new button("Remove", "red", new Insets(7, 20, 7, 20));

    // on pressed
    deleteButton
      .getButton()
      .setOnAction((ActionEvent ei) -> {
        for (SelectedRoom sr : selectedRooms) {
          if (sr.getRoom() == room) {
            LocalDate curFromDate_Formatted = LocalDate.parse(sr.getStartDate(), formatter);
            LocalDate curToDate_Formatted = LocalDate.parse(sr.getEndDate(), formatter);
            updatePriceLabel(-sr.getRoom().getRoom().getPrice(), curFromDate_Formatted, curToDate_Formatted);
            break;
          }
        }
        
        // remove from selected rooms
        selectedRooms.remove(room);

        // remove from room wrapper
        roomWrapper.getChildren().remove(roomCell);

        // update room count label
        updateRoomCountLabel(-1);
      });

    dateWrapper.getChildren().addAll(datefieldBox, deleteButton.getButton());

    dateCell.getChildren().addAll(dateWrapper);

    roomCell.getChildren().addAll(infoCell, contentCell, dateCell);

    // ------ GLOBAL ------//

    // updates the price label.
    updatePriceLabel(room.getRoom().getPrice(), fromDate_Formatted, toDate_Formatted);
    updateRoomCountLabel(1);

    // add listeners to date pickers
    _from_date
      .valueProperty()
      .addListener((obs, oldVal, newVal) -> {
        // find the corresponding SelectedRoom object in the selectedRooms ArrayList
        for (SelectedRoom sr : selectedRooms) {
          if (sr.getRoom() == room) {
            
            // update the from date value and total price
            LocalDate oldFromDate_Formatted = LocalDate.parse(sr.getStartDate(), formatter);
            LocalDate oldToDate_Formatted = LocalDate.parse(sr.getEndDate(), formatter);
            updatePriceLabel(-sr.getRoom().getRoom().getPrice(), oldFromDate_Formatted, oldToDate_Formatted);
            
            sr.setStartDate(newVal.toString());
            LocalDate NewFromDate_Formatted = LocalDate.parse(sr.getStartDate(), formatter);
            LocalDate OldToDate_Formatted = LocalDate.parse(sr.getEndDate(), formatter);
            updatePriceLabel(sr.getRoom().getRoom().getPrice(), NewFromDate_Formatted, OldToDate_Formatted);
            break;
          }
        }
      });

    _to_date
      .valueProperty()
      .addListener((obs, oldVal, newVal) -> {
        // find the corresponding SelectedRoom object in the selectedRooms ArrayList
        for (SelectedRoom sr : selectedRooms) {
          if (sr.getRoom() == room) {
            
            // update the end date value and total price
            LocalDate oldFromDate_Formatted = LocalDate.parse(sr.getStartDate(), formatter);
            LocalDate oldToDate_Formatted = LocalDate.parse(sr.getEndDate(), formatter);
            updatePriceLabel(-sr.getRoom().getRoom().getPrice(), oldFromDate_Formatted, oldToDate_Formatted);
            
            sr.setEndDate(newVal.toString());
            LocalDate NewFromDate_Formatted = LocalDate.parse(sr.getStartDate(), formatter);
            LocalDate OldToDate_Formatted = LocalDate.parse(sr.getEndDate(), formatter);
            updatePriceLabel(sr.getRoom().getRoom().getPrice(), NewFromDate_Formatted, OldToDate_Formatted);
            break;
          }
        }
      });

    // add to selected room arraylist
    // check if there is already a SelectedRoom object for the given room
    SelectedRoom existingSelectedRoom = null;
    for (SelectedRoom sr : selectedRooms) {
      if (sr.getRoom() == room) {
        existingSelectedRoom = sr;
        break;
      }
    }

    if (existingSelectedRoom != null) {
      // update the existing SelectedRoom object with the new date values
      existingSelectedRoom.setStartDate(_from_date.getValue().toString());
      existingSelectedRoom.setEndDate(_to_date.getValue().toString());
    } else {
      // create a new SelectedRoom object and add it to the selectedRooms ArrayList
      selectedRooms.add(
        new SelectedRoom(
          room,
          _from_date.getValue().toString(),
          _to_date.getValue().toString()
        )
      );
    }
    roomWrapper.getChildren().addAll(roomCell);
  }

  // top section
  private VBox getTopSection() {
    VBox vertical_wrapper = new VBox();

    // add padding top
    vertical_wrapper.setPadding(new Insets(30, 0, 0, 0));

    HBox wrapper = new HBox();
    wrapper.setMaxWidth(Double.MAX_VALUE);
    wrapper.setPrefHeight(90); // set preferred height to 100
    // green background
    wrapper.setStyle("-fx-background-color: transparent");
    wrapper.setAlignment(Pos.CENTER_LEFT); // divide space

    wrapper
      .getChildren()
      .addAll(getNameSection(), getButtonsSection(), getAnalyticsSection());

    vertical_wrapper.getChildren().addAll(wrapper);

    return vertical_wrapper;
  }

  // top section part
  private HBox getNameSection() {
    HBox wrapper = getPartSection(250);
    wrapper.setAlignment(Pos.CENTER); // center.

    // align label and logo vertically.
    VBox vertical_wrapper = new VBox();
    vertical_wrapper.setAlignment(Pos.CENTER_LEFT); // center.

    // booking engine text.
    Label booking_engine_label = new Label("Booking Engine");
    booking_engine_label.setStyle("-fx-font-size: 28px; -fx-text-fill: #fff");

    // hotelbuddy logo.
    Image hbImage = new Image("hotelbuddy_left_align.png");
    ImageView hbImageView = new ImageView(hbImage);
    hbImageView.setFitWidth(100);
    hbImageView.setFitHeight(15);

    vertical_wrapper.getChildren().addAll(booking_engine_label, hbImageView);

    wrapper.getChildren().add(vertical_wrapper);

    return wrapper;
  }

  // top section part
  private HBox getButtonsSection() {
    HBox wrapper = getPartSection(40);

    if(user.getTheme().equals("dark")){
      wrapper.setStyle("fx-background-color: #000");
    }
    else {
      wrapper.setStyle("fx-background-color: #ffffff");
    }

    // css for buttons
    String transparent_css = "-fx-background-color: transparent;";

    wrapper.setAlignment(Pos.CENTER); // center.

    // add button.
    Button addButton = new Button();
    Image addImage = new Image("/bookings/add.png");
    ImageView addImageView = new ImageView(addImage);

    addImageView.setFitWidth(30);
    addImageView.setFitHeight(30);
    addButton.setGraphic(addImageView);
    addButton.setStyle(transparent_css);

    // when pressed
    addButton.setOnAction(e -> {
      RoomSelectorTable roomSelectorTable = new RoomSelectorTable(
        startDate,
        endDate,
        SelectedRoomsView.this
      );
      previousPage = this.getCenter();
      btn = this.getTop();
      this.setCenter(roomSelectorTable);
      this.setTop(null);
    });

    // on mouse enter and exit
    if(user.getTheme().equals("dark")){
      addButton.setOnMouseEntered(e -> {
        addButton.setStyle("-fx-background-color: #171717;");
      });
    }
    else {
      addButton.setOnMouseEntered(e -> {
        addButton.setStyle("-fx-background-color: #ffffff;");
      });
    }

    addButton.setOnMouseExited(e -> {
      addButton.setStyle(transparent_css);
    });

    wrapper.getChildren().addAll(addButton);
    return wrapper;
  }

  // top section part
  private HBox getAnalyticsSection() {
    HBox wrapper = getPartSection(310);
    wrapper.setPadding(new Insets(20, 0, 20, 0));
    wrapper.setStyle("-fx-background-color: transparent");

    // two analytics objects for price and number of rooms resp.
    wrapper
      .getChildren()
      .addAll(getPriceAnalyticsObject(), getNumberRoomsAnalyticsObject());

    return wrapper;
  }

  // top section part
  private HBox getPriceAnalyticsObject() {
    HBox wrapper = getPartSection(150);
    if(user.getTheme().equals("dark")){
      wrapper.setStyle("-fx-background-color: #111");
    }
    else {
      wrapper.setStyle("-fx-background-color: #ffffff");
    }

    totalPriceLabel = new Label("Total price: $" + price);
    if(user.getTheme().equals("dark")){
      totalPriceLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px;");
    }
    else {
      totalPriceLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 14px;");
    }
    wrapper.getChildren().addAll(totalPriceLabel);
    wrapper.setAlignment(Pos.CENTER); // center.

    return wrapper;
  }

  // top section part
  private HBox getNumberRoomsAnalyticsObject() {
    HBox wrapper = getPartSection(180);
    if(user.getTheme().equals("dark")){
      wrapper.setStyle("-fx-background-color: #111");
    }
    else {
      wrapper.setStyle("-fx-background-color: #ffffff");
    }

    roomCountLabel = new Label("Number of rooms: " + room_count);
    if(user.getTheme().equals("dark")){
      roomCountLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px;");
    }
    else {
      roomCountLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 14px;");
    }
    wrapper.getChildren().addAll(roomCountLabel);
    wrapper.setAlignment(Pos.CENTER); // center.

    return wrapper;
  }

  // top section part
  private HBox getPartSection(int width) {
    HBox wrapper = new HBox();
    wrapper.setPrefHeight(90); // set preferred height to 100
    wrapper.setPrefWidth(width);
    // wrapper.setStyle("-fx-background-color: blue");

    wrapper.setPadding(new Insets(0, 2, 0, 2));

    return wrapper;
  }

  // update the price label.
  private void updatePriceLabel(int price_addon, LocalDate from, LocalDate to) {
    
    long days = ChronoUnit.DAYS.between(from, to);
    int new_price = price += (price_addon * days);
    totalPriceLabel.setText("Total price: $" + new_price);
  }

  // update the price label.
  private void updateRoomCountLabel(int change) {
    int new_count = room_count += change;
    roomCountLabel.setText("Number of rooms: " + new_count);
  }

  // -------------------- GUEST INFORMATION VIEW --------------------

  public void guestInformationView(String start, String stop) {
    BorderPane finalizePane = new BorderPane();

    // new page
    VBox finalizeBookingV = new VBox();
    finalizeBookingV.setSpacing(10);
    finalizeBookingV.setAlignment(Pos.TOP_CENTER);

    // Title: Enter guest credentials
    Label header3 = new Label("Enter guest credentials");
    if(user.getTheme().equals("dark")){
      header3.setStyle(
              "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;"
      );
    }
    else {
      header3.setStyle(
              "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #000000;"
      );
    }
    header3.setPadding(new Insets(20, 0, 20, 0));
    header3.setAlignment(Pos.CENTER);

    // center horizontally
    HBox.setHgrow(header3, Priority.ALWAYS);

    // ------- HERE COMES THE FIELDS FOR GUEST CREDENTIALS -------

    // First name
    Label firstNameText = new Label("First name");
    firstNameText.setTranslateY(5);
    TextField firstNameField = new TextField();
    firstNameField.setFont(new Font(15));
    firstNameField.setMinHeight(30);

    // Last name
    Label lastNameText = new Label("Last name");
    lastNameText.setTranslateY(5);
    TextField lastNameField = new TextField();
    lastNameField.setFont(new Font(15));
    lastNameField.setMinHeight(30);

    // email
    Label emailText = new Label("Email");
    emailText.setTranslateY(5);
    TextField emailField = new TextField();
    emailField.setFont(new Font(15));
    emailField.setMinHeight(30);

    // phone
    Label phoneText = new Label("Phone");
    phoneText.setTranslateY(5);
    TextField phoneField = new TextField();
    phoneField.setFont(new Font(15));
    phoneField.setMinHeight(30);

    // address
    Label addressText = new Label("Address");
    addressText.setTranslateY(5);
    TextField addressField = new TextField();
    addressField.setFont(new Font(15));
    addressField.setMinHeight(30);

    // city
    Label cityText = new Label("City");
    cityText.setTranslateY(5);
    TextField cityField = new TextField();
    cityField.setFont(new Font(15));
    cityField.setMinHeight(30);

    // country
    Label countryText = new Label("Country");
    countryText.setTranslateY(5);
    TextField countryField = new TextField();
    countryField.setFont(new Font(15));
    countryField.setMinHeight(30);

    // zip
    Label zipText = new Label("Zip");
    zipText.setTranslateY(5);
    TextField zipField = new TextField();
    zipField.setFont(new Font(15));
    zipField.setMinHeight(30);

    if(user.getTheme().equals("dark")){
      firstNameText.setStyle("-fx-font-size: 15px; -fx-text-fill: #c3c3c3;");
      lastNameText.setStyle("-fx-font-size: 15px; -fx-text-fill: #c3c3c3;");
      emailText.setStyle("-fx-font-size: 15px; -fx-text-fill: #c3c3c3;");
      phoneText.setStyle("-fx-font-size: 15px; -fx-text-fill: #c3c3c3;");
      addressText.setStyle("-fx-font-size: 15px; -fx-text-fill: #c3c3c3;");
      cityText.setStyle("-fx-font-size: 15px; -fx-text-fill: #c3c3c3;");
      countryText.setStyle("-fx-font-size: 15px; -fx-text-fill: #c3c3c3;");
      zipText.setStyle("-fx-font-size: 15px; -fx-text-fill: #c3c3c3;");
    }
    else {
      firstNameText.setStyle("-fx-font-size: 15px; -fx-text-fill: #32323f;");
      lastNameText.setStyle("-fx-font-size: 15px; -fx-text-fill: #32323f;");
      emailText.setStyle("-fx-font-size: 15px; -fx-text-fill: #32323f;");
      phoneText.setStyle("-fx-font-size: 15px; -fx-text-fill: #32323f;");
      addressText.setStyle("-fx-font-size: 15px; -fx-text-fill: #32323f;");
      cityText.setStyle("-fx-font-size: 15px; -fx-text-fill: #32323f;");
      countryText.setStyle("-fx-font-size: 15px; -fx-text-fill: #32323f;");
      zipText.setStyle("-fx-font-size: 15px; -fx-text-fill: #32323f;");
    }

    // create grid pane
    GridPane grid = new GridPane();
    grid.setHgap(20);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setAlignment(Pos.CENTER);

    // add fields to grid
    grid.add(firstNameText, 0, 0);
    grid.add(firstNameField, 0, 1);
    grid.add(lastNameText, 1, 0);
    grid.add(lastNameField, 1, 1);
    grid.add(emailText, 0, 2);
    grid.add(emailField, 0, 3);
    grid.add(phoneText, 1, 2);
    grid.add(phoneField, 1, 3);
    grid.add(addressText, 0, 4);
    grid.add(addressField, 0, 5);
    grid.add(cityText, 1, 4);
    grid.add(cityField, 1, 5);
    grid.add(countryText, 0, 6);
    grid.add(countryField, 0, 7);
    grid.add(zipText, 1, 6);
    grid.add(zipField, 1, 7);

    HBox bottomBox2 = new HBox();
    // HBox.setHgrow(bottomBox, Priority.ALWAYS);
    bottomBox2.setAlignment(Pos.CENTER_RIGHT);
    if(user.getTheme().equals("dark")){
      bottomBox2.setStyle(
              "-fx-border-color: #2b2b2b; -fx-border-width: 1 0 0 0; -fx-padding: 10 20 10 10;"
      );
    }
    else {
      bottomBox2.setStyle(
              "-fx-border-color: #545454; -fx-border-width: 1 0 0 0; -fx-padding: 10 20 10 10;"
      );
    }

    button finalizeButton = new button(
      "Create booking",
      "green",
      new Insets(10, 10, 10, 10)
    );

    // on pressed print hello
    finalizeButton
      .getButton()
      .setOnMousePressed(ex -> {
        int realGuestID = -1;

        try {
          // check if the guest email already exists.
          Guest guest = finalRequestHandler.getGuestByEmail(
            emailField.getText()
          );

          if (guest == null) {
            // create a new guest
            guest = new Guest();
            guest.setFirstName(firstNameField.getText());
            guest.setLastName(lastNameField.getText());
            guest.setEmail(emailField.getText());
            guest.setPhone(phoneField.getText());
            guest.setAddress(addressField.getText());
            guest.setCity(cityField.getText());
            guest.setCountry(countryField.getText());
            guest.setZip(zipField.getText());

            // add the guest to the database
            int guestID = finalRequestHandler.createGuest(guest);

            // given a successful creation
            if (guestID != -1) {
              // print the id
              realGuestID = guestID;
            }
          } else {
            // print guest id
            realGuestID = guest.getId();
          }

          // --------------- CREATING A NEW BOOKING WITH THE GUEST ID ----------------

          // create a string with guest id
          String realGuestIDString = String.valueOf(realGuestID);

          // add the booking to the database
          int bookingID = finalRequestHandler.createBooking(realGuestIDString);
          // for every room id selected, create a new room_booking with the booking id and the room id

          // loop through selected room ids
          for (int i = 0; i < selectedRooms.size(); i++) {
            RoomBooking roomBooking = new RoomBooking();
            roomBooking.setBookingId(bookingID);
            roomBooking.setRoomId(
              selectedRooms.get(i).getRoom().getRoom().getId()
            );

            // set start and end date
            roomBooking.setStartDate(selectedRooms.get(i).getStartDate());
            roomBooking.setEndDate(selectedRooms.get(i).getEndDate());

            // create a new room booking
            boolean successful_roombooking = finalRequestHandler.createRoomBooking(
              roomBooking
            );
          }

          // ---------Confirmation view------------------------
          VBox confirmationV = new VBox();
          confirmationV.setSpacing(10);
          confirmationV.setAlignment(Pos.TOP_CENTER);
          confirmationV.setPadding(new Insets(40, 0, 0, 0));

          Label header4 = new Label("Booking Successful");
          if(user.getTheme().equals("dark")){
            header4.setStyle(
                    "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;"
            );
          }
          else {
            header4.setStyle(
                    "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #000000;"
            );
          }
          header4.setPadding(new Insets(20, 0, 20, 0));
          header4.setAlignment(Pos.CENTER);

          HBox theButtons = new HBox();
          theButtons.setAlignment(Pos.TOP_CENTER);

          button sendInvoice = new button(
            "Show Invoice",
            "green",
            new Insets(10, 10, 10, 10)
          );

          button sendConfirmation = new button(
            "Show Confirmation",
            "green",
            new Insets(10, 10, 10, 10)
          );

          //INVOICE button
          sendInvoice
            .getButton()
            .setOnMousePressed(invoiceE -> {
              sendInvoice
                .getButton()
                .setOnAction((ActionEvent invoiceEv) -> {
                  try {
                    PDF pdf = new PDF(price);
                    pdf.open();
                  } catch (Exception er) {}
                });
            });

          //CONFIRMATION button
          sendConfirmation
            .getButton()
            .setOnMousePressed(confirmationE -> {
              sendConfirmation
                .getButton()
                .setOnAction((ActionEvent confirmationEv) -> {
                  try {
                    PDF pdf = new PDF(start, price);
                    pdf.open();
                  } catch (Exception er) {}
                });
            });

          //Build the confirmation view
          theButtons
            .getChildren()
            .addAll(sendConfirmation.getButton(), sendInvoice.getButton());
          confirmationV.getChildren().addAll(header4, theButtons);
          this.setCenter(confirmationV);
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      });

    bottomBox2.getChildren().add(finalizeButton.getButton());
    finalizeBookingV.getChildren().addAll(header3, grid);
    finalizePane.setTop(finalizeBookingV);
    finalizePane.setBottom(bottomBox2);

    // color red
    finalizeBookingV.setStyle("-fx-background-color: transparent;");

    this.setCenter(finalizePane);
  }
}
