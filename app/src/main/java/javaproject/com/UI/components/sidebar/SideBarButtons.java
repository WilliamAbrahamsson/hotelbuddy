package javaproject.com.UI.components.sidebar;

import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.views.app.App;

public class SideBarButtons extends VBox {

  User user = App.getUser();

  // declare and initialize currentButton as null
  final Button[] currentButton = new Button[] { null };

  // Create the buttons
  Button button0 = new Button("Dashboard");
  Button button2 = new Button("Bookings");
  Button button3 = new Button("Rooms");
  Button button4 = new Button("Guests");
  Button button5 = new Button("Employees");

  public SideBarButtons() {
    // Set the sidebar properties.
    setPrefWidth(200);
    setSpacing(8);

    // icons
    Image dashIcon = null;
    Image arrowIcon = null;
    Image bookingIcon = null;
    Image roomIcon = null;
    Image guestIcon = null;
    Image employeeIcon = null;
    Image checkinCheckoutIcon = null;

    if(user.getTheme().equals("dark")){
      dashIcon = new Image("/menu/dashboard.png");
      arrowIcon = new Image("/menu/arrow.png");
      bookingIcon = new Image("/menu/booking.png");
      roomIcon = new Image("/menu/room.png");
      guestIcon = new Image("/menu/guest.png");
      employeeIcon = new Image("/menu/employee.png");
      checkinCheckoutIcon = new Image("/menu/checkincheckout.png");
    }
    else {
      dashIcon = new Image("/menu/dark_dashboard.png");
      arrowIcon = new Image("/menu/dark_arrow.png");
      bookingIcon = new Image("/menu/dark_booking.png");
      roomIcon = new Image("/menu/dark_room.png");
      guestIcon = new Image("/menu/dark_guest.png");
      employeeIcon = new Image("/menu/dark_employee.png");
      checkinCheckoutIcon = new Image("/menu/dark_checkincheckout.png");
    }

    // add buttons to a Vbox
    VBox buttons = new VBox();
    buttons
      .getChildren()
      .addAll(button0, button2, button3, button4, button5);

    // add some margin above the buttons box
    VBox.setMargin(buttons, new Insets(64, 0, 0, 0));

    // css design for buttons
    if(user.getTheme().equals("dark")){
      buttons.getStylesheets().add("/style/Buttons.css");
    }
    else {
      buttons.getStylesheets().add("/style/LightButtons.css");
    }

    // For every button
    for (Button button : Arrays.asList(
      button0,
      button2,
      button3,
      button4,
      button5
    )) {
      button.setPadding(new Insets(8, 10, 8, 10));
      button.setId("buttonStyle");
      button.setTextFill(Color.WHITE);
      button.setMaxWidth(Double.MAX_VALUE);

      // Set the dashboard logo to the first button
      if (button == button0) {
        // Arrow logo view
        ImageView logoView = new ImageView();
        logoView.setImage(dashIcon);

        // Set the size of the image view
        logoView.setFitWidth(45);
        logoView.setFitHeight(45);

        button.setGraphic(logoView);
      } 
       else if (button == button2) {
        // Arrow logo view
        ImageView logoView = new ImageView();
        logoView.setImage(bookingIcon);

        // Set the size of the image view
        logoView.setFitWidth(45);
        logoView.setFitHeight(45);

        button.setGraphic(logoView);
      } else if (button == button3) {
        // Arrow logo view
        ImageView logoView = new ImageView();
        logoView.setImage(roomIcon);

        // Set the size of the image view
        logoView.setFitWidth(45);
        logoView.setFitHeight(45);

        button.setGraphic(logoView);
      } else if (button == button4) {
        // Arrow logo view
        ImageView logoView = new ImageView();
        logoView.setImage(guestIcon);

        // Set the size of the image view
        logoView.setFitWidth(45);
        logoView.setFitHeight(45);

        button.setGraphic(logoView);
      } else if (button == button5) {
        // Arrow logo view
        ImageView logoView = new ImageView();
        logoView.setImage(employeeIcon);

        // Set the size of the image view
        logoView.setFitWidth(45);
        logoView.setFitHeight(45);

        button.setGraphic(logoView);
      } else {
        // Arrow logo view
        ImageView arrowLogoView = new ImageView();
        arrowLogoView.setImage(arrowIcon);

        // Set the size of the image view
        arrowLogoView.setFitWidth(45);
        arrowLogoView.setFitHeight(45);

        button.setGraphic(arrowLogoView);
      }

      // set the dash button to be selected when program starts
      currentButton[0] = button0;
      button0.setId("buttonStylePressed");

      // on mouse hover, give user a visual feedback
      button.setOnMouseEntered(e -> {
        if (button != currentButton[0]) {
          button.setId("buttonStyleHover");
        }
      });

      // on mouse exit, give user a visual feedback
      button.setOnMouseExited(e -> {
        if (button != currentButton[0]) {
          button.setId("buttonStyle");
        }
      });

      // When a button is pressed
      button.setOnMousePressed(e -> {
        // Set the current button variable to the pressed button
        currentButton[0] = button;

        // Set the region in the App class
        button.setOnAction((ActionEvent event) -> {
          // Call the function in the other class
          App.setView(currentButton[0].getText());
          // change to rotate
        });

        // Update the colors of all buttons
        for (Button btn : Arrays.asList(
          button0,
          button2,
          button3,
          button4,
          button5
        )) {
          if (btn == currentButton[0]) {
            // Set the color of the current button to green
            btn.setId("buttonStylePressed");
          } else {
            // Set the color of all other buttons to the original color
            btn.setId("buttonStyle");
          }
        }
      });

      button.setFocusTraversable(true);
    }

    buttons.setId("buttons");

    // Add the buttons to the vbox
    getChildren().addAll(buttons);
  }

  public void removeSelection() {
    for (Button button : Arrays.asList(
      button0,
      button2,
      button3,
      button4,
      button5
    )) {
      button.setId("buttonStyle");
    }
  }

  public void setSelection(String buttonName) {
    for (Button button : Arrays.asList(
      button0,
      button2,
      button3,
      button4,
      button5
    )) {
      if (button.getText().equals(buttonName)) {
        button.setId("buttonStylePressed");

        App.setView(button.getText());
        currentButton[0] = button;
      }
    }
  }
}
