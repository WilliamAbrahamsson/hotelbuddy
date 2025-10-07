package javaproject.com.UI.views.app.dashboard;

import java.util.*;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.Notes;
import javaproject.com.DB.tables.Room;
import javaproject.com.DB.tables.User;
import javaproject.com.DB.tables.joins.FatBooking;
import javaproject.com.UI.components.button;
import javaproject.com.UI.components.layout.TitleBox;
import javaproject.com.UI.components.popup.Popup;
import javaproject.com.UI.views.app.App;
import javaproject.com.UI.views.app.View;

public class DashboardView extends View {

  RequestHandler finalRequestHandler = App.getDBCON();
  HBox topbar;
  BorderPane borderpane;
  VBox wrapper = new VBox();

  User user = App.getUser();

  // constructor
  public DashboardView() {
    borderpane = getBorderPane();

    // hgrow
    HBox.setHgrow(wrapper, Priority.ALWAYS);

    ScrollPane scrollpane = getScrollPane();

    // ------------------ SEARCH BOOKING PANE ---------------------

    borderpane.setCenter(scrollpane);

    topbar = getTopBar();
    button newButton = new button(
      "New Note",
      "green",
      new Insets(10, 25, 10, 25)
    );
    button new_to_do_b = new button(
      "New to do",
      "green",
      new Insets(10, 25, 10, 25)
    );
    button refButton = new button(
      "Refresh",
      "transparent",
      new Insets(10, 25, 10, 25)
    );

    updateTopBar(false);
    // topbar.getChildren().add(new_to_do_b.getButton());
    topbar.getChildren().add(newButton.getButton());
    topbar.getChildren().add(refButton.getButton());
    newButton.getButton().toBack();
    new_to_do_b.getButton().toBack();
    refButton.getButton().toBack();

    newButton
      .getButton()
      .setOnMousePressed(e -> {
        newButton
          .getButton()
          .setOnAction((ActionEvent event) -> {
            // On button click function
            String note_text = (user.getTheme().equals("dark"))
              ? "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #ffffff;"
              : "-fx-font-weight: bold; -fx-alignment: CENTER; -fx-font-size: 25px; -fx-text-fill: #000000;";
            String background_color_black = (user.getTheme().equals("dark"))
              ? "-fx-background-color: #000000;"
              : "-fx-background-color: #ffffff;";

            VBox noteContent = new VBox();
            noteContent.setStyle(background_color_black);
            noteContent.setAlignment(Pos.TOP_CENTER);

            Label addnote = new Label("Add note");
            addnote.setStyle(note_text);
            addnote.setPadding(new Insets(20, 0, 20, 0));
            addnote.setAlignment(Pos.CENTER);
            HBox fields = new HBox();
            fields.setStyle(background_color_black);
            TextArea note = new TextArea();
            HBox butonbox = new HBox();
            butonbox.setPadding(new Insets(20, 0, 20, 0));

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
            //buton1.getChildren().add(back.getButton());
            //buton2.getChildren().add(create.getButton());
            butonbox.getChildren().addAll(create.getButton(), back.getButton());
            butonbox.setAlignment(Pos.CENTER);
            fields.getChildren().addAll(note);
            fields.setAlignment(Pos.TOP_CENTER);

            noteContent.getChildren().addAll(addnote, fields, butonbox);
            borderPane.setCenter(noteContent);

            back
              .getButton()
              .setOnMousePressed(eback -> {
                back
                  .getButton()
                  .setOnAction((ActionEvent event_back) -> {
                    borderpane.setTop(topbar);
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
                      Notes notes = new Notes();
                      notes.setNoteText(note.getText());
                      finalRequestHandler.saveNote(notes.getNoteText());
                      //refreshPage();
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

    new_to_do_b
      .getButton()
      .setOnMousePressed(e -> {
        new_to_do_b
          .getButton()
          .setOnAction((ActionEvent event) -> {
            // On button click function

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
  }

  // Refresh page (change the view to itself)
  public void refreshPage() {
    App.setView("Dashboard");
  }

  public ScrollPane getScrollPane() {
    ScrollPane scrollpane = new ScrollPane();

    // disable horizontal scrolling
    scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    // make the scrollpane ONLY vertical
    scrollpane.setFitToWidth(true);

    getTitlePane();
    getStatusPane();
    getSearchBookingPane();

    scrollpane.setContent(wrapper);

    // transparent scrollpane
    if (user.getTheme().equals("dark")) {
      scrollpane.setStyle("-fx-background: #000;");
    } else {
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

  public void getTitlePane() {
    // Title box
    TitleBox titleBox = new TitleBox("Dashboard", "");
    // padding left
    titleBox.setPadding(new javafx.geometry.Insets(10, 0, 0, 40));

    wrapper.getChildren().add(titleBox);
  }

  public void getStatusPane() {
    ArrayList<FatBooking> Arrivals = new ArrayList<>();
    ArrayList<FatBooking> Checkouts = new ArrayList<>();

    try {
      Arrivals = finalRequestHandler.Fetch_Arrivals();
    } catch (Exception errors) {
      errors.printStackTrace();
    }
    try {
      Checkouts = finalRequestHandler.Fetch_checkouts();
    } catch (Exception errors) {
      errors.printStackTrace();
    }

    VBox mainStatus = new VBox();

    mainStatus.setPrefHeight(150);
    mainStatus.setMaxHeight(150);
    mainStatus.setMinHeight(150);

    // background color
    if (user.getTheme().equals("dark")) {
      mainStatus.setStyle(
        "-fx-background-color: #111; -fx-border-width: 1px, 0px, 1px, 0px;"
      );
    } else {
      mainStatus.setStyle(
        "-fx-background-color: #b9b9b9; -fx-border-width: 1px, 0px, 1px, 0px;"
      );
    }

    // add margin
    VBox.setMargin(mainStatus, new Insets(0, 40, 20, 40));
    //mainStatus.setPadding(new Insets(40, 40, 0, 40));

    //checkin
    Label checkIn = new Label("Check-in Today");
    Integer number1 = Arrivals.size();
    Label checkInCount = new Label(number1.toString());

    VBox VcheckIn = new VBox();
    VcheckIn.getChildren().addAll(checkIn, checkInCount);
    VcheckIn.setPadding(new Insets(25, 60, 20, 40));
    VcheckIn.setAlignment(Pos.CENTER);

    //checkout
    Label checkOut = new Label("Check-out Today");
    Integer number2 = Checkouts.size();
    Label checkOutCount = new Label(number2.toString());

    VBox VcheckOut = new VBox();
    VcheckOut.getChildren().addAll(checkOut, checkOutCount);
    VcheckOut.setPadding(new Insets(25, 60, 20, 40));
    VcheckOut.setAlignment(Pos.CENTER);

    //Rooms
    Label rooms = new Label("Available Rooms");
    Integer number3 = finalRequestHandler.getAvailableRooms();
    Label roomcount = new Label(number3.toString());

    VBox Vroom = new VBox();
    Vroom.getChildren().addAll(rooms, roomcount);
    Vroom.setPadding(new Insets(25, 60, 20, 40));
    Vroom.setAlignment(Pos.CENTER);

    //Percentage
    Label percentage = new Label("Occupancy");

    ArrayList<Room> allrooms = new ArrayList<>();
    try {
      allrooms = finalRequestHandler.getRooms();
    } catch (Exception errors) {}
    double percentA = (double) number3 / (double) allrooms.size();
    double percentB = 1 - percentA;
    int percentC = (int) Math.round(percentB * 100);
    Integer percentD = (Integer) percentC;
    Label percentcount = new Label(percentD.toString() + " %");
    if (user.getTheme().equals("dark")) {
      checkIn.setStyle(
        "-fx-text-fill: #fff; -fx-text-fill: #fff;-fx-font-size: 15px;"
      );
      checkInCount.setStyle(
        "-fx-text-fill: #fff; -fx-text-fill: #fff;-fx-font-size: 60px;"
      );
      checkOut.setStyle(
        "-fx-text-fill: #fff; -fx-text-fill: #fff;-fx-font-size: 15px;"
      );
      checkOutCount.setStyle(
        "-fx-text-fill: #fff; -fx-text-fill: #fff;-fx-font-size: 60px;"
      );
      rooms.setStyle(
        "-fx-text-fill: #fff; -fx-text-fill: #fff;-fx-font-size: 15px;"
      );
      roomcount.setStyle(
        "-fx-text-fill: #fff; -fx-text-fill: #fff;-fx-font-size: 60px;"
      );
      percentage.setStyle(
        "-fx-text-fill: #fff; -fx-text-fill: #fff;-fx-font-size: 15px;"
      );
      percentcount.setStyle(
        "-fx-text-fill: #fff; -fx-text-fill: #fff;-fx-font-size: 60px;"
      );
    } else {
      checkIn.setStyle(
        "-fx-text-fill: #000000; -fx-text-fill: #000000;-fx-font-size: 15px;"
      );
      checkInCount.setStyle(
        "-fx-text-fill: #000000; -fx-text-fill: #000000;-fx-font-size: 60px;"
      );
      checkOut.setStyle(
        "-fx-text-fill: #000000; -fx-text-fill: #000000;-fx-font-size: 15px;"
      );
      checkOutCount.setStyle(
        "-fx-text-fill: #000000; -fx-text-fill: #000000;-fx-font-size: 60px;"
      );
      rooms.setStyle(
        "-fx-text-fill: #000000; -fx-text-fill: #000000;-fx-font-size: 15px;"
      );
      roomcount.setStyle(
        "-fx-text-fill: #000000; -fx-text-fill: #000000;-fx-font-size: 60px;"
      );
      percentage.setStyle(
        "-fx-text-fill: #000000; -fx-text-fill: #000000;-fx-font-size: 15px;"
      );
      percentcount.setStyle(
        "-fx-text-fill: #000000; -fx-text-fill: #000000;-fx-font-size: 60px;"
      );
    }

    VBox Vpercent = new VBox();

    Vpercent.getChildren().addAll(percentage, percentcount);
    Vpercent.setPadding(new Insets(25, 60, 20, 40));
    Vpercent.setAlignment(Pos.CENTER);
    HBox box = new HBox();

    box.getChildren().addAll(VcheckIn, VcheckOut, Vroom, Vpercent);
    box.setSpacing(50);
    HBox.setHgrow(VcheckIn, Priority.ALWAYS);
    HBox.setHgrow(VcheckOut, Priority.ALWAYS);
    HBox.setHgrow(Vroom, Priority.ALWAYS);
    HBox.setHgrow(Vpercent, Priority.ALWAYS);
    mainStatus.getChildren().addAll(box);
    wrapper.getChildren().add(mainStatus);
  }

  public void getSearchBookingPane() {
    VBox statusPaneWrapper = new VBox();

    // height 100
    statusPaneWrapper.setPrefHeight(400);
    statusPaneWrapper.setMaxHeight(400);
    statusPaneWrapper.setMinHeight(400);

    // background color
    if (user.getTheme().equals("dark")) {
      statusPaneWrapper.setStyle(
        "-fx-background-color: #000; -fx-border-width: 1px, 0px, 1px, 0px;"
      );
    } else {
      statusPaneWrapper.setStyle(
        "-fx-background-color: #ffffff; -fx-border-width: 1px, 0px, 1px, 0px;"
      );
    }

    // add margin
    VBox.setMargin(statusPaneWrapper, new Insets(0, 40, 20, 40));

    // Title box
    TitleBox titleBox = new TitleBox("Notes", "");
    // padding left
    titleBox.setPadding(new javafx.geometry.Insets(10, 0, 0, 50));

    statusPaneWrapper.getChildren().add(titleBox);

    postNotes note = new postNotes(200);

    try {
      ArrayList<Notes> noteList = finalRequestHandler.getNotes();
      for (Notes not : noteList) {
        note.addNoteBox(not);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    //HorizontalScrollpane post = new HorizontalScrollpane(150);
    statusPaneWrapper.getChildren().addAll(note);
    wrapper.getChildren().addAll(statusPaneWrapper);
  }
}
