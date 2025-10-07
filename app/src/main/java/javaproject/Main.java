package javaproject;

import javafx.application.Application;
import javafx.stage.Stage;
import javaproject.com.UI.views.login.Login;

public class Main {
  static Login login_screen = new Login();

  public static class RealApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
      login_screen.start(new Stage());
    }
  }

  public static void main(String[] args) {
    Application.launch(RealApplication.class);
  }
}
