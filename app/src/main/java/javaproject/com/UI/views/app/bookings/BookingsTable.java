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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javaproject.com.DB.tables.Guest;
import javaproject.com.DB.tables.Room;
import javaproject.com.DB.tables.RoomBooking;
import javaproject.com.DB.tables.RoomContent;
import javaproject.com.DB.tables.User;
import javaproject.com.DB.tables.joins.FatBooking;
import javaproject.com.DB.tables.joins.FatRoom;
import javaproject.com.UI.components.button;
import javaproject.com.UI.components.dynamictable.DynamicTable;
import javaproject.com.UI.components.popup.Popup;
import javaproject.com.UI.views.app.App;

public class BookingsTable extends DynamicTable {

  User user = App.getUser();

  ArrayList<FatBooking> fat_bookings = fetchFatBookings();

  ArrayList<SelectedRoom> selectedRooms = new ArrayList<SelectedRoom>();
  VBox roomWrapper = new VBox();
  Node previousPage;
  Node btn;
  // price label
  int price = 0;
  Label totalPriceLabel;

  // room count label
  int room_count = 0;
  Label roomCountLabel;

  public BookingsTable() {
    String[] headers = {
      "ID",
      "Guest Name",
      "Guest Email",
      "Guest Phone",
      "Rooms Booked",
    };
    this.addHeaders(headers);

    for (int ix = 0; ix < fat_bookings.size(); ix++) {
      FatBooking fat_booking = fat_bookings.get(ix);

      String[] fatBookingData = {
        String.valueOf(fat_booking.getBooking().getId()),
        fat_booking.getGuest().getFirstName() +
        " " +
        fat_booking.getGuest().getLastName(),
        fat_booking.getGuest().getEmail(),
        fat_booking.getGuest().getPhone(),
        getRoomIds(fat_booking.getRooms()),
      };
      addItem(fatBookingData);
    }

    // on pressed, print id of cell
    table.setOnMousePressed(e -> {
      String id = table.getSelectionModel().getSelectedItem().get(0);
      int bookingId = Integer.parseInt(id);
      // Booking detail view
      toggleDetailView(bookingId);
    });
  }

  public ArrayList<FatBooking> fetchFatBookings() {
    try {
      ArrayList<FatBooking> fat_bookings = finalRequestHandler.getFatBookings();
      return fat_bookings;
    } catch (Exception errors) {
      errors.printStackTrace();
    }
    return null;
  }

  private String getRoomIds(ArrayList<Room> rooms) {
    StringBuilder roomIds = new StringBuilder();

    for (int i = 0; i < rooms.size(); i++) {
      if (i > 0) {
        if (i == rooms.size() - 1) {
          roomIds.append(" and ");
        } else {
          roomIds.append(", ");
        }
      }

      roomIds.append(String.valueOf(rooms.get(i).getId()));
    }

    return roomIds.toString();
  }

  // open detail view
  public void toggleDetailView(int id) {
    selectedRooms.clear();

    System.out.println(id);

    try {
      HBox directionBox = new HBox();
      directionBox.setMinHeight(50);
      if(user.getTheme().equals("dark")){
        directionBox.setStyle(
                "-fx-background-color: #000; -fx-padding: 10px; -fx-border-color: #444444; -fx-border-width: 0 0 1 0;"
        );
      }
      else {
        directionBox.setStyle(
                "-fx-background-color: #ffffff; -fx-padding: 10px; -fx-border-color: #000000; -fx-border-width: 0 0 1 0;"
        );
      }

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
              /* this.setTop(searchbar);
              this.setCenter(table); */
              App.setView("Bookings");
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

      FatBooking fat_booking = getFatBooking(id);

      ScrollPane scroller = getScrollPane();
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
      scroller.setContent(roomWrapper);

      contentSquare.getChildren().addAll(getTopSection(fat_booking), scroller);

      for (Room room : fat_booking.getRooms()) {
        // get fat room at Room id
        FatRoom fat_room = finalRequestHandler.getFatRoomAtRoomID(room.getId());

        // room id
        int roomId = fat_room.getRoom().getId();
        int bookingId = fat_booking.getBooking().getId();

        RoomBooking connector = finalRequestHandler.getRoomBooking(
          roomId,
          bookingId
        );

        // dates
        String startDate = connector.getStartDate();
        String endDate = connector.getEndDate();

        addRoom(fat_room, connector, startDate, endDate);
      }

      // replace the table with the black square
      this.setCenter(contentSquare);
      this.setTop(directionBox);
    } catch (Exception errors) {
      errors.printStackTrace();
    }
  }

  // get fat booking at id
  public FatBooking getFatBooking(int id) {
    for (int i = 0; i < fat_bookings.size(); i++) {
      if (fat_bookings.get(i).getBooking().getId() == id) {
        return fat_bookings.get(i);
      }
    }
    return null;
  }

  public void deleteBooking(int id) {
    try {
      finalRequestHandler.deleteBooking(id);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // public ScrollPane getBottomSection(FatBooking fat_booking) {

  //   return scroller;
  // }

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
  public void addRoom(
    FatRoom room,
    RoomBooking connector,
    String start_date,
    String end_date
  ) {
    HBox roomCell = new HBox();

    String startDate = start_date.substring(0, 10);
    String endDate = end_date.substring(0, 10);

    System.out.println("Room: " + room.getRoom().getName());

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
      infoCell.setStyle("-fx-background-color: #b9b9b9;");
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
    String roomServiceButtonColor = null;

    if(user.getTheme().equals("dark")){
      roomServiceButtonColor = "transparent";
    }
    else {
      roomServiceButtonColor = "grey";
    }

    button roomServiceButton = new button(
      "Room Service",
      roomServiceButtonColor,
      new Insets(10, 10, 10, 10)
    );

    // add to info cell
    infoCell
      .getChildren()
      .addAll(roomName, roomPrice, roomServiceButton.getButton());

    // on pressed
    roomServiceButton
      .getButton()
      .setOnAction((ActionEvent ei) -> {
      

        RoomServiceView roomServiceView = new RoomServiceView(BookingsTable.this, connector);
        previousPage = this.getCenter();

        // parse id and this current instance oif bookingstable
        this.setCenter(roomServiceView.toggleDetailView());
        this.setTop(roomServiceView.getDirectionBox(BookingsTable.this));

      });

    // ------ CONTENTS CELL ------//
    VBox contentCell = new VBox();
    contentCell.setPadding(new Insets(0, 0, 0, 20));
    contentCell.setAlignment(Pos.CENTER_LEFT);
    if(user.getTheme().equals("dark")){
      contentCell.setStyle("-fx-background-color: #111;");
    }
    else {
      contentCell.setStyle("-fx-background-color: #b9b9b9;");
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
    Image squareImage = null;
    Image bedImage = null;

    if(user.getTheme().equals("dark")){
      squareImage = new Image("/bookings/square.png");
      bedImage = new Image("/bookings/bed.png");
    }
    else {
      squareImage = new Image("/bookings/dark_square.png");
      bedImage = new Image("/bookings/dark_bed.png");
    }

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
      dateCell.setStyle("-fx-background-color: #b9b9b9;");
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
    _to_date.setId("to-date-picker2");

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
        // remove from selected rooms
        selectedRooms.remove(room);
        Popup popup = new Popup();

        popup.ErrorDialogBox("Room removed from booking.");

        // remove from db
        try {
          finalRequestHandler.deleteRoomBooking(connector.getId());
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        // remove from room wrapper
        roomWrapper.getChildren().remove(roomCell);

        // update price label
        updatePriceLabel(-room.getRoom().getPrice());
        // update room count label
        updateRoomCountLabel(-1);
      });

    HBox buttonsWrapper = new HBox();
    buttonsWrapper.setSpacing(20);
    buttonsWrapper.getChildren().addAll(deleteButton.getButton());

    dateWrapper.getChildren().addAll(datefieldBox, buttonsWrapper);

    dateCell.getChildren().addAll(dateWrapper);

    roomCell.getChildren().addAll(infoCell, contentCell, dateCell);

    // ------ GLOBAL ------//

    // updates the price label.
    updatePriceLabel(room.getRoom().getPrice());
    updateRoomCountLabel(1);

    // apply button
    button applyButton = new button(
      "Update",
      "green",
      new Insets(8, 25, 8, 25)
    );

    // add listeners to date pickers
    _from_date
      .valueProperty()
      .addListener((obs, oldVal, newVal) -> {
        // find the corresponding SelectedRoom object in the selectedRooms ArrayList
        for (SelectedRoom sr : selectedRooms) {
          if (sr.getRoom() == room) {
            // update the start date value
            sr.setStartDate(newVal.toString());

            // check if apply button is already added
            if (
              !buttonsWrapper.getChildren().contains(applyButton.getButton())
            ) {
              buttonsWrapper.getChildren().add(applyButton.getButton());
            }

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
            // update the end date value
            sr.setEndDate(newVal.toString());

            // check if apply button is already added
            if (
              !buttonsWrapper.getChildren().contains(applyButton.getButton())
            ) {
              buttonsWrapper.getChildren().add(applyButton.getButton());
            }

            break;
          }
        }
      });

    // on pressed apply button
    applyButton
      .getButton()
      .setOnAction((ActionEvent ei) -> {
        // remove from selected rooms
        Popup popup = new Popup();

        popup.ErrorDialogBox("Dates updated.");

        // edit from db
        try {
          // update the start and end value
          connector.setStartDate(_from_date.getValue().toString());
          connector.setEndDate(_to_date.getValue().toString());

          finalRequestHandler.editRoomBooking(connector);

          // remove apply button
          buttonsWrapper.getChildren().remove(applyButton.getButton());
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
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
  private VBox getTopSection(FatBooking fat_booking) {
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
      .addAll(
        getNameSection(),
        getButtonsSection(fat_booking),
        getAnalyticsSection()
      );

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
    if(user.getTheme().equals("dark")){
      booking_engine_label.setStyle("-fx-font-size: 28px; -fx-text-fill: #fff");
    }
    else {
      booking_engine_label.setStyle("-fx-font-size: 28px; -fx-text-fill: #000000");
    }

    // hotelbuddy logo.
    Image hbImage = null;
    if(user.getTheme().equals("dark")){
      hbImage = new Image("hotelbuddy_left_align.png");
    }
    else {
      hbImage = new Image("dark_hotelbuddy_left_align.png");
    }

    ImageView hbImageView = new ImageView(hbImage);
    hbImageView.setFitWidth(100);
    hbImageView.setFitHeight(15);

    vertical_wrapper.getChildren().addAll(booking_engine_label, hbImageView);

    wrapper.getChildren().add(vertical_wrapper);

    return wrapper;
  }

  // top section part
  private HBox getButtonsSection(FatBooking fat_booking) {
    HBox wrapper = getPartSection(100);

    if(user.getTheme().equals("dark")){
      wrapper.setStyle("fx-background-color: #000");
    }
    else {
      wrapper.setStyle("fx-background-color: #ffffff");
    }

    // css for buttons
    String transparent_css = "-fx-background-color: transparent;";

    wrapper.setAlignment(Pos.CENTER); // center.

    // guest button.
    Button guestButton = new Button();
    Image guestImage = null;
    if(user.getTheme().equals("dark")){
      guestImage = new Image("/bookings/guest.png");
    }
    else {
      guestImage = new Image("/bookings/dark_guest.png");
    }
    ImageView guestImageView = new ImageView(guestImage);

    guestImageView.setFitWidth(35);
    guestImageView.setFitHeight(35);
    guestButton.setGraphic(guestImageView);
    guestButton.setStyle(transparent_css);

    // print button.
    Button printButton = new Button();
    Image printImage = null;
    if(user.getTheme().equals("dark")){
      printImage = new Image("/bookings/printer.png");
    }
    else {
      printImage = new Image("/bookings/dark_printer.png");
    }
    ImageView printImageView = new ImageView(printImage);

    printImageView.setFitWidth(25);
    printImageView.setFitHeight(25);
    printButton.setGraphic(printImageView);
    printButton.setStyle(transparent_css);

    // add button.
    Button addButton = new Button();
    Image addImage = null;
    if(user.getTheme().equals("dark")){
      addImage = new Image("/bookings/add.png");
    }
    else {
      addImage = new Image("/bookings/dark_add.png");
    }
    ImageView addImageView = new ImageView(addImage);

    addImageView.setFitWidth(30);
    addImageView.setFitHeight(30);
    addButton.setGraphic(addImageView);
    addButton.setStyle(transparent_css);

    // when pressed
    addButton.setOnAction(e -> {
      // SPECIFY DATES VIEW, then ...
      showDateSelector(fat_booking, this.getCenter());
    });

    // on mouse enter and exit
    addButton.setOnMouseEntered(e -> {
      if(user.getTheme().equals("dark")){
        addButton.setStyle("-fx-background-color: #171717;");
      }
      else {
        addButton.setStyle("-fx-background-color: #b9b9b9;");
      }
    });

    addButton.setOnMouseExited(e -> {
      addButton.setStyle(transparent_css);
    });

    // guestbutton
    guestButton.setOnMouseEntered(e -> {
      if(user.getTheme().equals("dark")){
        guestButton.setStyle("-fx-background-color: #171717;");
      }
      else {
        guestButton.setStyle("-fx-background-color: #b9b9b9;");
      }
    });

    guestButton.setOnMouseExited(e -> {
      guestButton.setStyle(transparent_css);
    });

    // pressed
    guestButton.setOnMousePressed(e -> {
      Guest guest = fat_booking.getGuest();
      this.setTop(null);
      this.setCenter(getGuestDetailView(guest, false));
    });

    // print button
    printButton.setOnMouseEntered(e -> {
      if(user.getTheme().equals("dark")){
        printButton.setStyle("-fx-background-color: #171717;");
      }
      else {
        printButton.setStyle("-fx-background-color: #b9b9b9;");
      }
    });

    printButton.setOnMouseExited(e -> {
      printButton.setStyle(transparent_css);
    });

    wrapper.getChildren().addAll(guestButton, printButton, addButton);
    return wrapper;
  }

  private void showDateSelector(FatBooking fat_booking, Node prev) {
    VBox outerWrapper = new VBox();

    // add padding top
    outerWrapper.setPadding(new Insets(30, 0, 0, 0));

    // center horizontally
    outerWrapper.setAlignment(Pos.TOP_CENTER);

    HBox pickersWrapper = new HBox();

    // black background style
    if(user.getTheme().equals("dark")){
      outerWrapper.setStyle("-fx-background-color: #000");
    }
    else {
      outerWrapper.setStyle("-fx-background-color: #ffffff");
    }

    pickersWrapper.setPadding(new Insets(15, 0, 0, 0));
    pickersWrapper.setSpacing(10);
    pickersWrapper.setAlignment(Pos.TOP_CENTER);

    // from date
    DatePicker _from_date_picker = new DatePicker();
    _from_date_picker.setPrefWidth(150);
    _from_date_picker.setPromptText("From date");
    _from_date_picker.setValue(LocalDate.now());

    // to date
    DatePicker _to_date_picker = new DatePicker();
    _to_date_picker.setPrefWidth(150);
    _to_date_picker.setPromptText("To date");
    _to_date_picker.setValue(LocalDate.now().plusDays(7));

    // add to wrapper
    pickersWrapper.getChildren().addAll(_from_date_picker, _to_date_picker);

    // label specify the dates
    Label specify_dates_label = new Label("Specify the dates");
    if(user.getTheme().equals("dark")){
      specify_dates_label.setStyle("-fx-text-fill: #fff; -fx-font-size: 18px;");
    }
    else {
      specify_dates_label.setStyle("-fx-text-fill: #000000; -fx-font-size: 18px;");
    }
    specify_dates_label.setPadding(new Insets(0, 0, 15, 0));
    if(user.getTheme().equals("dark")){
      specify_dates_label.setStyle(
              "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;"
      );
    }
    else {
      specify_dates_label.setStyle(
              "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #000000;"
      );
    }

    HBox bottomBox4 = new HBox();
    HBox.setHgrow(bottomBox4, Priority.ALWAYS);
    bottomBox4.setAlignment(Pos.CENTER_RIGHT);

    if(user.getTheme().equals("dark")){
      bottomBox4.setStyle(
              "-fx-border-color: #444; -fx-background-color: #000; -fx-border-width: 1 0 0 0; -fx-padding: 10 20 10 10;"
      );
    }
    else {
      bottomBox4.setStyle(
              "-fx-border-color: #b9b9b9; -fx-background-color: #ffffff; -fx-border-width: 1 0 0 0; -fx-padding: 10 20 10 10;"
      );
    }

    button show = new button("Show rooms", "green", new Insets(10, 10, 10, 10));

    // add show button to bottom box
    bottomBox4.getChildren().add(show.getButton());

    // add to outer wrapper
    outerWrapper.getChildren().addAll(specify_dates_label, pickersWrapper);

    // set center
    this.setCenter(outerWrapper);
    this.setBottom(bottomBox4);

    // dates
    LocalDate from_date = _from_date_picker.getValue();
    LocalDate to_date = _to_date_picker.getValue();

    // string dates
    String from_date_string = from_date.toString();
    String to_date_string = to_date.toString();

    // booking id
    int booking_id = fat_booking.getBooking().getId();

    // when show button is pressed
    show
      .getButton()
      .setOnAction(e -> {
        AddRoomTable addRoomTable = new AddRoomTable(
          from_date_string,
          to_date_string,
          booking_id,
          BookingsTable.this
        );
        previousPage = prev;
        btn = this.getTop();
        this.setCenter(addRoomTable);
        this.setTop(null);
      });
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
    wrapper.setMinHeight(46); // set preferred height to 100
    wrapper.setPrefWidth(width);

    // alignment center
    wrapper.setAlignment(Pos.CENTER);



    wrapper.setPadding(new Insets(0, 2, 0, 2));

    return wrapper;
  }

  // update the price label.
  private void updatePriceLabel(int price_addon) {
    int new_price = price += price_addon;
    totalPriceLabel.setText("Total price: $" + new_price);
  }

  // update the price label.
  private void updateRoomCountLabel(int change) {
    int new_count = room_count += change;
    roomCountLabel.setText("Number of rooms: " + new_count);
  }

  public VBox getGuestDetailView(Guest guest, Boolean failed) {
    // ---------- -Edit user form- ----------//

    // CSS Vars
    String Red_text = "-fx-font-size: 15px; -fx-text-fill: #FF0000;";
    String checkbox_css =
      "-fx-text-alignment:left; -fx-font-size: 15px; -fx-text-fill: #c3c3c3;";

    String header_text = null;
    String background_color_black = null;
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

    String style = null;
    if(user.getTheme().equals("dark")){
      style = "-fx-font-size: 15px; -fx-text-fill: #c3c3c3;";
    }
    else {
      style = "-fx-font-size: 15px; -fx-text-fill: #000000;";
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

    button _edit = new button("Done", "green", new Insets(10, 25, 10, 25));

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
                  App.setView("Bookings");
                } else {
                  getGuestDetailView(guest, true);
                }
              } catch (Exception errors) {
                errors.printStackTrace();
              }
            }
          });
      });

    return allContent;
  }
}
