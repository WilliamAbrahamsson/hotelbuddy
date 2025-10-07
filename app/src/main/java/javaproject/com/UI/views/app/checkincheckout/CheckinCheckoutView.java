package javaproject.com.UI.views.app.checkincheckout;

import java.util.*;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.User;
import javaproject.com.DB.tables.joins.FatBooking;
import javaproject.com.UI.components.button;
import javaproject.com.UI.components.dynamictable.DynamicTable;
import javaproject.com.UI.views.app.App;
import javaproject.com.UI.views.app.View;

public class CheckinCheckoutView extends View {

  RequestHandler finalRequestHandler = App.getDBCON();
  HBox topbar;

  User user = App.getUser();

  public CheckinCheckoutView() {
    HBox box = new HBox();
    // fill out horizontally
    HBox.setHgrow(box, Priority.ALWAYS);

    box.setStyle("-fx-spacing: 0;");

    // Adds all vboxes and hboxes together

    box.getChildren().addAll(getLeftWrapper(), getRightWrapper());

    borderPane.setCenter(box);

    topbar = getTopBar();
    button refButton = new button(
      "Refresh",
      "transparent",
      new Insets(10, 25, 10, 25)
    );
    button backButton = new button(
      "Back",
      "green",
      new Insets(10, 25, 10, 25)
    );
    updateTopBar(false);
    topbar.getChildren().add(refButton.getButton());
    topbar.getChildren().add(backButton.getButton());
    refButton.getButton().toBack();
    backButton.getButton().toBack();

    refButton
      .getButton()
      .setOnMousePressed(e -> {
        refButton.getButton().setOnAction((ActionEvent event) -> {});
      });

    backButton
        .getButton()
        .setOnMousePressed(e -> {
            backButton
            .getButton()
            .setOnAction((ActionEvent event) -> {
                App.setView("Bookings");
            });
        });
  }

  public void toggleDetailView(
    ArrayList<FatBooking> list,
    int i,
    boolean check_in
  ) {
    HBox directionBox = new HBox();
    directionBox.setStyle(
      "-fx-background-color: transparent; -fx-padding: 10px; -fx-border-color: #444444; -fx-border-width: 0 0 1 0;"
    );

    // center content left
    directionBox.setAlignment(Pos.TOP_LEFT);
    // create back button
    button backButton = new button("Back", "green", new Insets(12, 25, 12, 25));

    // on pressed, go back to check-in/out
    backButton
      .getButton()
      .setOnMousePressed(e -> {
        backButton
          .getButton()
          .setOnAction((ActionEvent event) -> {
            App.setView("Check-In/Out");
          });
      });

    directionBox.getChildren().add(backButton.getButton());

    HBox buttonsBox = new HBox();
    buttonsBox.setSpacing(10);
    buttonsBox.setAlignment(Pos.CENTER);
    buttonsBox.setPadding(new Insets(0, 0, 20, 0));

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
    // style
    allContent.setStyle(background_color_black);
    allContent.setAlignment(Pos.TOP_CENTER);

    Label header = new Label(
      check_in ? "Confirm Check-in" : "Confirm Check-out"
    );
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

    VBox spacer = new VBox();
    spacer.setPadding(new Insets(20, 0, 35, 0));
    spacer.setVisible(false);

    HBox text_fields = new HBox();
    Label name = new Label(
      list.get(i).getGuest().getFirstName() +
      " " +
      list.get(i).getGuest().getLastName()
    );
    if(user.getTheme().equals("dark")){
      name.setStyle("-fx-text-fill: #ffffff;");
    }
    else {
      name.setStyle("-fx-text-fill: #000000;");
    }
    text_fields.getChildren().add(name);

    Label name_text = new Label("Guest name: ");
    if(user.getTheme().equals("dark")){
      name_text.setStyle("-fx-text-fill: #ffffff;");
    }
    else {
      name_text.setStyle("-fx-text-fill: #000000;");
    }
    Label error_text = new Label(
      check_in ? "Already checked-in" : "Already checked out"
    );
    error_text.setStyle("-fx-text-fill: red;");

    HBox error_box = new HBox();
    error_box.setVisible(false);
    error_box.getChildren().add(error_text);

    button Check_in_Button = new button(
      "Confirm",
      "green_confirm",
      new Insets(9, 17, 8, 17)
    );
    Check_in_Button
      .getButton()
      .setOnMousePressed(e -> {
        int suc;
        try {
          if (check_in) {
            suc =
              finalRequestHandler.check_in_user(
                list.get(i).getGuest().getId(),
                list.get(i).getBooking().getId()
              );
          } else {
            suc =
              finalRequestHandler.check_out_user(
                list.get(i).getGuest().getId(),
                list.get(i).getBooking().getId()
              );
          }
        } catch (Exception error) {
          suc = 2;
        }
        if (suc == 0) {
          error_box.setVisible(true);
        } else if (suc == 1) {
          App.setView("Check-In/Out");
        } else {
          System.out.println("ERROR WITH CHECKING IN USER");
        }
      });

    HBox buttonBox = new HBox();
    buttonBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    buttonBox.getChildren().addAll(Check_in_Button.getButton());

    column1.getChildren().addAll(header, name_text, spacer, error_box);
    column2.getChildren().addAll(text_fields, buttonBox);

    fields.getChildren().addAll(column1, column2);
    fields.setAlignment(Pos.TOP_CENTER);

    allContent.getChildren().addAll(header, fields);
    borderPane.setCenter(allContent);
  }

  // left wrapper
  public VBox getLeftWrapper() {
    VBox left_wrapper = new VBox();
    if(user.getTheme().equals("dark")){
      left_wrapper.setStyle("-fx-background-color: #000;");
    }
    else {
      left_wrapper.setStyle("-fx-background-color: #ffffff;");
    }

    left_wrapper.setPrefWidth(1000);
    left_wrapper.setMaxWidth(1000);
    left_wrapper.setMinWidth(100);

    // LEFT CHECK IN TABLE WITH CONTENT
    VBox check_in_box = new VBox();
    if(user.getTheme().equals("dark")){
      check_in_box.setStyle(
              "-fx-background-color: #111111; -fx-border-width: 0 0 0 0; -fx-border-color: #4444;"
      );
    }
    else {
      check_in_box.setStyle(
              "-fx-background-color: #ffffff; -fx-border-width: 0 0 0 0; -fx-border-color: #000000;"
      );
    }

    // Topbar inside Check in box

    HBox check_in_Topbar = new HBox();
    if(user.getTheme().equals("dark")){
      check_in_Topbar.setStyle("-fx-background-color: #111111; -fx-border-width: 1 0 0 0; -fx-border-color: #4444;");

    }
    else {
      check_in_Topbar.setStyle("-fx-background-color: #ffffff; -fx-border-width: 1 0 0 0; -fx-border-color: #000000;");

    }
    check_in_Topbar.setPrefHeight(50);
    check_in_Topbar.setPadding(new Insets(30, 30, 30, 30));

    Label Check_in_title = new Label("Checking in today");
    if(user.getTheme().equals("dark")){
      Check_in_title.setStyle("-fx-text-fill: white; -fx-font-size: 24pt;");
    }
    else {
      Check_in_title.setStyle("-fx-text-fill: #000000; -fx-font-size: 24pt;");
    }
    check_in_Topbar.getChildren().add(Check_in_title);
    check_in_Topbar.setAlignment(Pos.CENTER);
    HBox.setHgrow(check_in_Topbar, Priority.ALWAYS);

    ArrayList<FatBooking> Arrivals = new ArrayList<>();

    try {
      Arrivals = finalRequestHandler.Fetch_Arrivals();
    } catch (Exception errors) {
      errors.printStackTrace();
    }
    final ArrayList<FatBooking> Event_arrivals = Arrivals;
    DynamicTable check_in_table = new DynamicTable();
    String[] check_in_headers = { "Name", "phone", "Checked in" };
    check_in_table.addHeaders(check_in_headers);
    if (!Arrivals.isEmpty()) {
      for (int i = 0; i < Arrivals.size(); i++) {
        String[] data = {
          Arrivals.get(i).getGuest().getFirstName() +
          " " +
          Arrivals.get(i).getGuest().getLastName(),
          Arrivals.get(i).getGuest().getPhone(),
          Arrivals.get(i).getBooking().getChecked_in(),
          Integer.toString(i),
        };
        check_in_table.addItem(data);
      }
    }

    check_in_table
      .getTable()
      .setOnMousePressed(e -> {
        if (
          check_in_table.getTable().getSelectionModel().getSelectedItem() !=
          null
        ) {
          String id = check_in_table
            .getTable()
            .getSelectionModel()
            .getSelectedItem()
            .get(3);
          int i = Integer.parseInt(id);
          toggleDetailView(Event_arrivals, i, true);
        }
      });

    VBox.setVgrow(check_in_table, Priority.ALWAYS);
    VBox.setVgrow(check_in_box, Priority.ALWAYS);
    check_in_box.getChildren().addAll(check_in_Topbar, check_in_table);
    // add top border to table
      
    left_wrapper.getChildren().add(check_in_box);

    return left_wrapper;
  }

  // Right wrapper
  public VBox getRightWrapper() {
    // RIGHT CHECK OUT TABLE WITH CONTENT
    VBox right_wrapper = new VBox();

    right_wrapper.setPrefWidth(1000);
    right_wrapper.setMaxWidth(1000);
    right_wrapper.setMinWidth(100);

    if(user.getTheme().equals("dark")){
      right_wrapper.setStyle("-fx-background-color: #000;");
    }
    else {
      right_wrapper.setStyle("-fx-background-color: #ffffff;");
    }

    VBox check_out_box = new VBox();
    if(user.getTheme().equals("dark")){
      check_out_box.setStyle(
              "-fx-background-color: #111111; -fx-border-width: 0 0 0 0; -fx-border-color: #4444;"
      );
    }
    else {
      check_out_box.setStyle(
              "-fx-background-color: #ffffff; -fx-border-width: 0 0 0 0; -fx-border-color: #000000;"
      );
    }

    // Topbar inside Checkout box

    HBox check_out_Topbar = new HBox();
    if(user.getTheme().equals("dark")){
      check_out_Topbar.setStyle("-fx-background-color: #111111;");
    }
    else {
      check_out_Topbar.setStyle("-fx-background-color: #ffffff;");
    }
    check_out_Topbar.setPrefHeight(50);
    check_out_Topbar.setPadding(new Insets(30, 30, 30, 30));

    Label Check_out_title = new Label("Checking out today");
    if(user.getTheme().equals("dark")){
      Check_out_title.setStyle("-fx-text-fill: white; -fx-font-size: 24pt;");
    }
    else {
      Check_out_title.setStyle("-fx-text-fill: #000000; -fx-font-size: 24pt;");
    }
    check_out_Topbar.getChildren().add(Check_out_title);
    check_out_Topbar.setAlignment(Pos.CENTER);
    HBox.setHgrow(check_out_Topbar, Priority.ALWAYS);

    ArrayList<FatBooking> Checkouts = new ArrayList<>();

    try {
      Checkouts = finalRequestHandler.Fetch_checkouts();
    } catch (Exception errors) {
      errors.printStackTrace();
    }
    final ArrayList<FatBooking> Event_checkouts = Checkouts;
    DynamicTable checkout_table = new DynamicTable();
    String[] Checkouts_headers = { "Name", "phone", "Checked in" };
    checkout_table.addHeaders(Checkouts_headers);
    if (!Checkouts.isEmpty()) {
      for (int i = 0; i < Checkouts.size(); i++) {
        String[] data = {
          Checkouts.get(i).getGuest().getFirstName() +
          " " +
          Checkouts.get(i).getGuest().getLastName(),
          Checkouts.get(i).getGuest().getPhone(),
          Checkouts.get(i).getBooking().getChecked_in(),
          Integer.toString(i),
        };
        checkout_table.addItem(data);
      }
    }
    checkout_table
      .getTable()
      .setOnMousePressed(e -> {
        if (
          checkout_table.getTable().getSelectionModel().getSelectedItem() !=
          null
        ) {
          String id = checkout_table
            .getTable()
            .getSelectionModel()
            .getSelectedItem()
            .get(3);
          int i = Integer.parseInt(id);
          toggleDetailView(Event_checkouts, i, false);
        }
      });

    VBox.setVgrow(checkout_table, Priority.ALWAYS);
    VBox.setVgrow(check_out_box, Priority.ALWAYS);
    check_out_box.getChildren().addAll(check_out_Topbar, checkout_table);
    right_wrapper.getChildren().add(check_out_box);

    return right_wrapper;
  }
}
