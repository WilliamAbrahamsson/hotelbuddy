package javaproject.com.UI.views.app;

import java.util.ArrayList;
import java.util.Objects;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.components.bottombar.QuickActionCell;
import javaproject.com.UI.components.sidebar.SideBar;
import javaproject.com.UI.views.app.bookings.BookingsView;
import javaproject.com.UI.views.app.checkincheckout.CheckinCheckoutView;
import javaproject.com.UI.views.app.dashboard.DashboardView;
import javaproject.com.UI.views.app.guests.GuestsView;
import javaproject.com.UI.views.app.rooms.RoomsView;
import javaproject.com.UI.views.app.settings.SettingsView;
import javaproject.com.UI.views.app.users.UsersView;
import javaproject.com.UI.views.login.Login;

public class App extends Application {

  static User user;
  static RequestHandler finalRequestHandler;
  static BorderPane root;

  static SideBar sidebar;
  static HBox bottomBar;

  static BorderPane bottomPane;

  static HBox tabContainer;

  static Stage thisStage;

  // get screen dimensions
  double primaryScreenWidth = Screen.getPrimary().getBounds().getWidth();
  double primaryScreenHeight = Screen.getPrimary().getBounds().getHeight();

  // tab arraylist
  static ArrayList<String> active_tabs = new ArrayList<>();

  @Override
  public void start(Stage primaryStage) {
    // StackPane stackpane = new StackPane();

    root = new BorderPane();

    // FloatingChoices floatingChoices = new FloatingChoices();

    // add both to stackpane
    // stackpane.getChildren().add(root);
    // stackpane.getChildren().add(floatingChoices);

    thisStage = primaryStage;

    // set prefered window size
    /* primaryStage.setWidth(primaryScreenWidth * 0.4);
    primaryStage.setHeight(primaryScreenHeight * 0.6);

    // set minimum window size
    primaryStage.setMinWidth(primaryScreenWidth * 0.4);
    primaryStage.setMinHeight(primaryScreenHeight * 0.6); */

    // set width and height in pixels
    primaryStage.setWidth(1200);
    primaryStage.setHeight(800);

    // set minimum window size
    primaryStage.setMinWidth(1200);
    primaryStage.setMinHeight(800);

    user = (User) primaryStage.getUserData();

    try {
      primaryStage
        .getIcons()
        .add(
          new Image(
            Objects.requireNonNull(
              getClass().getClassLoader().getResourceAsStream("hotelbuddy.png")
            )
          )
        );
    } catch (Exception e) {
      System.out.println("icon fail");
      System.out.println(e);
    }

    Scene scene = loadBars();

    primaryStage.setTitle("Hotelbuddy");
    primaryStage.setScene(scene);
    primaryStage.setResizable(true);

    primaryStage.show();
  }

  public static Scene loadBars(){
    sidebar = new SideBar();
    // Set the sidebar properties
    sidebar.setPrefWidth(100);

    root.setLeft(sidebar);

    View activeView = setView("Dashboard");

    if(user.getTheme().equals("dark")){
      activeView.getStylesheets().add("/style/ViewStyle.css");
    }
    else {
      activeView.getStylesheets().add("/style/LightViewStyle.css");
    }

    activeView.setMaxWidth(Double.MAX_VALUE);
    activeView.setMaxHeight(Double.MAX_VALUE);
    activeView.setMinWidth(0);
    activeView.setMinHeight(0);

    root.setCenter(activeView);

    // create bottombar for tabs
    bottomBar = new HBox();
    root.setBottom(bottomBar);

    // add some color and height 60px
    bottomBar.setId("bottombar");
    bottomBar.setPrefHeight(0);

    // min height
    bottomBar.setMinHeight(0);

    // make the width the whole screen
    bottomBar.setMaxWidth(Double.MAX_VALUE);

    // create borderpane for the tabs
    bottomPane = new BorderPane();

    // create hbox to hold the tabs
    tabContainer = new HBox();

    // position center
    tabContainer.setAlignment(Pos.CENTER);

    // set height
    tabContainer.setPrefHeight(40);

    // create tab1
    QuickActionCell tab1 = new QuickActionCell("Quick Booking");


    // when tab1 is clicked
    tab1.setOnMouseClicked(event -> {
      sidebar.removeSelection();
      sidebar.pressButton("Bookings");

      View bookingsView = setView("Bookings");

      ((BookingsView) bookingsView).createNewBooking();
    });

    // add tabs to tab container
    // tabContainer.getChildren().addAll(tab1);

    bottomPane.setCenter(tabContainer);
    bottomBar.getChildren().add(bottomPane);
    if(user.getTheme().equals("dark")){
      bottomBar.setStyle(
              "-fx-border-color: #2b2b2b; -fx-border-width: 1 0 0 0; -fx-background-color: #111111;"
      );
    }
    else {
      bottomBar.setStyle(
              "-fx-border-color: #000000; -fx-border-width: 1 0 0 0; -fx-background-color: #ffffff;"
      );
    }

    Scene scene = null;
    try {
      scene = new Scene(root);
    }catch (Exception ignored){
    }

    root.setId("pane");

    if(user.getTheme().equals("dark")){
      root.getStylesheets().add("/style/AppStyle.css");
    }
    else {
      root.getStylesheets().add("/style/LightAppStyle.css");
    }

    return scene;
  }

  public static void setRequestHandler(RequestHandler req) {
    finalRequestHandler = req;
  }

  public static void main(String[] args) {
    launch(args);
  }

  // get current view string
  public static String getCurrentView() {
    String fullName = root.getCenter().getClass().getSimpleName();

    // remove last 4 characters
    String simpleName = fullName.substring(0, fullName.length() - 4);
    return simpleName;
  }

  // Change view
  public static View setView(String region) {
    switch (region) {
      case "Dashboard":
        DashboardView dashboard = new DashboardView();
        root.setCenter(dashboard);
        return dashboard;
      case "Check-In/Out":
        CheckinCheckoutView checkincheckout = new CheckinCheckoutView();
        root.setCenter(checkincheckout);
        return checkincheckout;
      case "Bookings":
        BookingsView bookings = new BookingsView();
        root.setCenter(bookings);
        return bookings;
      case "Rooms":
        RoomsView rooms = new RoomsView();
        root.setCenter(rooms);
        return rooms;
      case "Guests":
        GuestsView customers = new GuestsView();
        root.setCenter(customers);
        return customers;
      case "Employees":
        UsersView users = new UsersView(root);
        root.setCenter(users);
        return users;
      case "Settings":
        SettingsView settings = new SettingsView();
        root.setCenter(settings);
        return settings;
      default:
        break;
    }
    return null;
  }

  public static void logout() {
    // close the current stage
    thisStage.close();

    Login login = new Login();
    try {
      login.start(new Stage());
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public static User getUser() {
    return user;
  }

  public static void refreshUser(){
    user = App.getUser();
  }

  public static RequestHandler getDBCON() {
    return finalRequestHandler;
  }

  public static void removeSidebarSelection() {
    SideBar.removeSelection();
  }
}
