package javaproject.com.UI.views.login;

import com.sun.jdi.AbsentInformationException;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.views.app.App;

public class Login extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    // Window title
    primaryStage.setTitle("Login");

    // Initialize new logo image
    Image logo = new Image("/hotelbuddy_alpha.png");

    // Add logo to window title
    try {
      primaryStage.getIcons().add(logo);
    } catch (Exception e) {
      System.out.println("icon fail");
      System.out.println(e);
    }

    // Size of window can't be changed
    primaryStage.setResizable(false);

    // Create a grid pane to hold the login form and logo, set the background color.
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    // Imagefield with our logo (NOTE: Background is just the same color as image)
    ImageView logoView = new ImageView();
    logoView.setImage(logo);
    logoView.setFitHeight(400);
    logoView.setFitWidth(400);

    // This label text is updated from empty string when user credentials are wrong
    // or successful.
    final Label res = new Label();

    // Username, Password, Login Button
    TextField usernameField = new TextField();
    usernameField.setPromptText("Enter Username");
    usernameField.setText("");

    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText("Enter Password");
    passwordField.setText("");

    usernameField.setId("field");
    passwordField.setId("field");

    Button loginButton = new Button("Login");

    // When enter is pressed on username field, move to password field.
    usernameField.setOnAction(e -> {
      passwordField.requestFocus();
    });

    // When enter is pressed on password field, call login button.
    passwordField.setOnAction(e -> {
      loginButton.fire();
    });

    // Connect to DB
    RequestHandler requestHandler = null;
    try {
      requestHandler = new RequestHandler();
    } catch (Exception e) {
      System.out.println("Database connection failed");
      System.out.println(e);
    }

    // When loginbutton is pressed, a new requesthandler is initialized and user
    // login is done.
    RequestHandler finalRequestHandler = requestHandler;

    loginButton.setOnAction(e -> {
      try {
        // Check if there is a user with the same credentials
        User user = finalRequestHandler.getUser(
          usernameField.getText(),
          passwordField.getText()
        );

        // There is not.
        if (user == null) {
          throw new AbsentInformationException("User not found");
        }

        // Set response text
        res.setText("Login Successful");

        // Else, close primary stage and open the program.
        primaryStage.close();

        // Initialize next stage
        App app = new App();
        Stage stage = new Stage();

        // Parse the User object to next stage
        stage.setUserData(user);

        // use the randomcolor function
        String color = randomColor();

        // save the color in the user
        user.setColor(color);

        // Start next stage
        app.setRequestHandler(finalRequestHandler);
        app.start(stage);
      } catch (Exception ex) {
        System.out.println("Outer EX:" + ex);
        // Set response text
        res.setText("Invalid username or password. Please try again.");

        // Clear password field
        passwordField.clear();

        // Set focus on username field
        usernameField.requestFocus();
      }
    });

    // Setup HBox so the fields are aligned horizontally
    HBox formBox = new HBox();
    formBox.getChildren().addAll(usernameField, passwordField, loginButton);
    formBox.getStyleClass().add("hbox");

    VBox verticalBox = new VBox();
    verticalBox.getChildren().addAll(logoView, res, formBox);

    // Add both logo, response and box to the grid.
    grid.add(verticalBox, 1, 2);

    // Set the scene and show the stage & add style sheet
    Scene scene = new Scene(grid);
    scene.getStylesheets().add("/style/LoginStyle.css");

    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public String randomColor() {
    String[] colors = {
      "#FFB300",
      "#803E75",
      "#FF6800",
      "#C10020",
      "#007D34",
      "#00538A",
      "#53377A",
      "#FF8E00",
      "#B32851",
      "#F4C800",
    };
    Random rand = new Random();
    int randomIndex = rand.nextInt(colors.length);
    String randomColor = "-fx-background-color: " + colors[randomIndex] + ";";

    return randomColor;
  }
}
