package javaproject.com.UI.views.app.bookings;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.Room;
import javaproject.com.DB.tables.RoomBooking;
import javaproject.com.DB.tables.RoomService;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.components.button;
import javaproject.com.UI.views.app.App;

public class RoomServiceView {

  User user = App.getUser();
  private BookingsTable bookingsTable; // add a SelectedRoomsView field
  RoomBooking roomBooking;
  HBox content = new HBox();
  RequestHandler finalRequestHandler = App.getDBCON();

  // arraylist of room services
  ArrayList<RoomService> roomServices;

  public RoomServiceView(BookingsTable bookingsTable, RoomBooking roomBooking) {
    this.bookingsTable = bookingsTable; // a way back :D
    this.roomBooking = roomBooking;

    System.out.println(roomBooking.getId());
  }

  public VBox toggleDetailView() {
    VBox root = new VBox();

    // clear
    content.getChildren().clear();

    // fetch room services
    roomServices = fetchRoomServices(roomBooking.getId());

    // add some padding around root
    root.setPadding(new Insets(10, 10, 10, 10));

    // background
    if(user.getTheme().equals("dark")){
      root.setStyle("-fx-background-color: #000000;");
    }
    else {
      root.setStyle("-fx-background-color: #ffffff;");
    }

    // content VBox
    content.setSpacing(10);
    content.setPadding(new Insets(10, 10, 10, 10));

    // full width and height
    root.setPrefSize(1000, 1000);

    root
      .getChildren()
      .addAll(getDescBox(), getHorizontalScrollPane(), getControlBox());

    // for each roomservice
    // if room services not empty

    System.out.println("room booking id: " + roomBooking.getId());

    if (roomServices != null) {
      for (int ix = 0; ix < roomServices.size(); ix++) {
        RoomService roomService = roomServices.get(ix);

        addToScrollPane(
          roomService.getId(),
          roomService.getDescription(),
          roomService.getService_time()
        );
      }
    }

    return root;
  }

  public ArrayList<RoomService> fetchRoomServices(int roomBookingId) {
    try {
      ArrayList<RoomService> roomServices = finalRequestHandler.getRoomServicesById(
        roomBookingId
      );
      return roomServices;
    } catch (Exception errors) {
      errors.printStackTrace();
    }
    return null;
  }

  public HBox getDescBox() {
    HBox controlBox = new HBox();
    controlBox.setSpacing(20);
    controlBox.setPadding(new Insets(0, 20, 10, 20));

    controlBox.setPrefHeight(50);

    // width double max val
    controlBox.setPrefWidth(Double.MAX_VALUE);

    // get room name
    Room room;

    // fetch Room object via requesthandeler
    try {
      room = finalRequestHandler.getRoomById(roomBooking.getRoomId());

      // room name
      String roomName = room.getName();

      // hbox to hold label and button
      HBox header_box = new HBox();

      // center in box
      header_box.setAlignment(Pos.CENTER);

      // add a label
      Label label = new Label("Room service ordered for " + roomName);

      // add button with the add icon
      // add button.

      String transparent_css = "-fx-background-color: transparent;";

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
        bookingsTable.setCenter(orderRoomService());
      });

      // hover
      addButton.setOnMouseEntered(e -> {
        if(user.getTheme().equals("dark")){
          addButton.setStyle("-fx-background-color: #111111;");
        }
        else {
          addButton.setStyle("-fx-background-color: #b9b9b9;");
        }
      });

      addButton.setOnMouseExited(e -> {
        addButton.setStyle(transparent_css);
      });

      // label white larger
      if(user.getTheme().equals("dark")){
        label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px;");
      }
      else {
        label.setStyle("-fx-text-fill: #000000; -fx-font-size: 20px;");
      }

      // add label and button to hbox
      header_box.getChildren().addAll(label, addButton);

      // add label to control box
      controlBox.getChildren().add(header_box);
    } catch (Exception errors) {
      errors.printStackTrace();
    }

    return controlBox;
  }

  private VBox orderRoomService() {
    VBox root = new VBox();

    // add some padding around root
    root.setPadding(new Insets(10, 10, 10, 10));

    // background
    if(user.getTheme().equals("dark")){
      root.setStyle("-fx-background-color: #000000;");
    }
    else {
      root.setStyle("-fx-background-color: #ffffff;");
    }

    // content VBox
    content.setSpacing(10);
    content.setPadding(new Insets(10, 10, 10, 10));

    // full width and height
    root.setPrefSize(1000, 1000);

    // Large label "order room service"
    Label label = new Label("Order description");


    if(user.getTheme().equals("dark")){
      label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px;");
    }
    else {
      label.setStyle("-fx-text-fill: #000000; -fx-font-size: 20px;");
    }

    // margin to label
    label.setPadding(new Insets(0, 0, 10, 0));

    // large text field
    TextArea textArea = new TextArea();
    textArea.setPrefHeight(200);
    textArea.setPrefWidth(400);

    if(user.getTheme().equals("dark")){
      textArea.setStyle("-fx-border-color: #444");
    }
    else {
      textArea.setStyle("-fx-border-color: #000");
    }

    HBox buttonwrapper = new HBox();

    // align right
    buttonwrapper.setAlignment(Pos.CENTER_RIGHT);

    // margin top
    buttonwrapper.setPadding(new Insets(15, 0, 15, 0));

    // add button
    button btn1 = new button(
      "Order room service",
      "transparent",
      new Insets(12, 42, 12, 42)
    );

    // when pressed
    btn1
      .getButton()
      .setOnAction(e -> {
        // get text from text area
        String description = textArea.getText();

        // create room service object
        RoomService roomService = new RoomService();

        // set description
        roomService.setRoom_booking_id(roomBooking.getId());
        roomService.setDescription(description);

        // date is auto increment in SQL

        // send to server
        try {
          finalRequestHandler.createRoomService(roomService);
        } catch (Exception errors) {
          errors.printStackTrace();
        }

        // go back to detail view

        bookingsTable.setCenter(toggleDetailView());
      });

    // add to wrapper
    buttonwrapper.getChildren().add(btn1.getButton());

    // add text area to root
    root.getChildren().addAll(label, textArea, buttonwrapper);

    return root;
  }

  private ScrollPane getHorizontalScrollPane() {
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setPannable(true);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);

    // remove blue border when selected
    scrollPane.setBorder(null);
    scrollPane.getStyleClass().add("edge-to-edge");

    // transparent scrollpane
    if (user.getTheme().equals("dark")) {
      scrollPane.setStyle("-fx-background: #000;");
    } else {
      scrollPane.setStyle("-fx-background: #ffffff;");
    }

    scrollPane.setPrefHeight(200);
    scrollPane.setPrefWidth(Double.MAX_VALUE);

    // add content to scrollpane
    scrollPane.setContent(content);

    return scrollPane;
  }

  public void addToScrollPane(int id, String description, String time) {
    VBox box = new VBox();

    // background to box
    if(user.getTheme().equals("dark")){
      box.setStyle("-fx-background-color: #111111;");
    }
    else {
      box.setStyle("-fx-background-color: #b9b9b9;");
    }

    box.setMinWidth(300);
    box.setPrefHeight(200);

    // top hbox with label displaying time
    HBox top = new HBox();
    top.setPrefHeight(50);
    top.setPrefWidth(300);
    top.setAlignment(Pos.CENTER_LEFT);
    Label timeLabel = new Label(time.substring(11, 16));
    // add margin to left of label
    HBox.setMargin(timeLabel, new Insets(0, 0, 0, 17));
    top.getChildren().add(timeLabel);

    // middle HBOX displaying description
    HBox middle = new HBox();
    middle.setPrefHeight(100);
    middle.setPrefWidth(300);
    middle.setAlignment(Pos.TOP_LEFT);
    Label descriptionLabel = new Label(description);

    // add margin to left of label
    HBox.setMargin(descriptionLabel, new Insets(10, 0, 0, 17));
    middle.getChildren().add(descriptionLabel);

    // bottom HBOX containing two buttons that take up same space
    HBox bottom = new HBox();
    bottom.setMinHeight(60);
    bottom.setPrefWidth(300);
    bottom.setAlignment(Pos.CENTER);

    String btn1Color = "";

    if(user.getTheme().equals("dark")){
      btn1Color = "transparent";
    }
    else {
      btn1Color = "green";
    }

    // create buttons
    button btn1 = new button(
      "Details",
      btn1Color,
      new Insets(12, 42, 12, 42)
    );

    button btn2 = new button("Resolve", "green", new Insets(12, 42, 12, 42));

    // when resolve button is pressed
    btn2
      .getButton()
      .setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            // remove from scrollpane

            // remove from database
            try {
              finalRequestHandler.removeRoomService(id);

              // remove from scrollpane
              content.getChildren().remove(box);
            } catch (Exception errors) {
              errors.printStackTrace();
            }
          }
        }
      );

    // add buttons to bottom HBOX
    bottom.getChildren().addAll(btn1.getButton(), btn2.getButton());

    // add top, middle and bottom HBOX to VBox
    box.getChildren().addAll(top, middle, bottom);

    content.getChildren().add(box);
  }

  public HBox getControlBox() {
    HBox controlBox = new HBox();
    controlBox.setSpacing(10);
    controlBox.setPadding(new Insets(10, 10, 10, 10));

    controlBox.setPrefHeight(100);

    // width double max val
    controlBox.setPrefWidth(Double.MAX_VALUE);

    // green background
    // controlBox.setStyle("-fx-background-color: #00ff00;");

    return controlBox;
  }

  public HBox getDirectionBox(BookingsTable bookingsTable) {
    HBox directionBox = new HBox();
    directionBox.setMinHeight(50);
    if (user.getTheme().equals("dark")) {
      directionBox.setStyle(
        "-fx-background-color: #000; -fx-padding: 10px; -fx-border-color: #444444; -fx-border-width: 0 0 1 0;"
      );
    } else {
      directionBox.setStyle(
        "-fx-background-color: #ffffff; -fx-padding: 10px; -fx-border-color: #000000; -fx-border-width: 0 0 1 0;"
      );
    }

    // center content vertically
    directionBox.setAlignment(Pos.CENTER_LEFT);

    // create back button
    button backButton = new button("Back", "green", new Insets(12, 25, 12, 25));

    // on pressed, go back to table view
    backButton
      .getButton()
      .setOnMousePressed(e -> {
        backButton
          .getButton()
          .setOnAction((ActionEvent event) -> {
            /* this.setTop(searchbar);
              this.setCenter(table); */
            bookingsTable.setCenter(bookingsTable.previousPage);
            bookingsTable.setTop(bookingsTable.btn);
          });
      });

    directionBox.getChildren().add(backButton.getButton());

    return directionBox;
  }
}
