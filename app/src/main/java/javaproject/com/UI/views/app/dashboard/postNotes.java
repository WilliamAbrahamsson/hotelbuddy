package javaproject.com.UI.views.app.dashboard;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.Notes;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.components.button;
import javaproject.com.UI.components.layout.TitleBox;
import javaproject.com.UI.views.app.App;

public class postNotes extends VBox {

  BorderPane borderpane;
  ScrollPane scrollpane = new ScrollPane();
  HBox pane = new HBox();
  RequestHandler finalRequestHandler = App.getDBCON();

  User user = App.getUser();

  public postNotes(int height) {
    // height
    setPrefHeight(height);
    setMinHeight(height);
    setMaxHeight(height);

    setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(this, Priority.ALWAYS);

    // add some padding
    setPadding(new javafx.geometry.Insets(10, 50, 10, 50));

    TitleBox paneDesc = new TitleBox("Note", "");

    scrollpane.setContent(pane);

    scrollpane.setPrefHeight(height);
    scrollpane.setMinHeight(height);
    scrollpane.setMaxHeight(height);

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

    // allow tap and drag to scroll
    scrollpane.setPannable(true);

    setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(scrollpane, Priority.ALWAYS);

    getChildren().addAll(scrollpane);
  }

  public void addNoteBox(Notes note) throws Exception {
    char firstChar;
    VBox box = new VBox();
    Text text = new Text(note.getNoteText());

    //text.setTextAlignment(Pos.TOP_LEFT);
    if (user.getTheme().equals("dark")) {
      text.setFill(Color.WHITE);
    } else {
      text.setFill(Color.BLACK);
    }
    text.setWrappingWidth(0);
    button detail = new button("Details", "green", new Insets(5, 30, 5, 30));

    button delete = new button("Remove", "red", new Insets(5, 30, 5, 30));

    box.setAlignment(Pos.CENTER);
    box.setPrefWidth(260);
    box.setMinWidth(260);
    box.setMaxWidth(260);
    box.setPrefHeight(scrollpane.getPrefHeight());
    box.setMinHeight(scrollpane.getPrefHeight());
    box.setMaxHeight(scrollpane.getPrefHeight());
    if (user.getTheme().equals("dark")) {
      box.setStyle("-fx-background-color: #111;");
    } else {
      box.setStyle("-fx-background-color: #b9b9b9;");
    }

    double prehieght = box.getPrefHeight() / 3;

    // Hbox for contents on top
    HBox topBox = new HBox();
    //topBox.setFill(Color.GREEN);
    Button logo = new Button();
    logo.setShape(new Circle(18));
    logo.setMinSize(2 * 18, 2 * 18);
    logo.setMaxSize(2 * 18, 2 * 18);

    User noteCreator = finalRequestHandler.getUser(note.getUserId());

    firstChar = noteCreator.getFirstName().charAt(0);

    logo.setText(Character.toString(firstChar));

    if (noteCreator.getId() == user.getId()) {
      logo.setStyle(user.getColor());
    } else {
      logo.setStyle(randomColor());
    }

    logo.setTextFill(Color.WHITE);

    HBox logoBox = new HBox(logo);
    logoBox.setAlignment(Pos.CENTER_RIGHT);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate creationDate = LocalDate.parse(note.creationDate());
    String formatDate = creationDate.format(DateTimeFormatter.ofPattern("dd"));

    LocalDate currentDate = LocalDate.now();
    Long daysAgo = ChronoUnit.DAYS.between(currentDate, creationDate);
    String daysAgoLogo = (daysAgo == 0) ? "Today" : (daysAgo + " days ago");

    Text datetext = new Text(daysAgoLogo);
    if (user.getTheme().equals("dark")) {
      datetext.setFill(Color.WHITE);
    } else {
      datetext.setFill(Color.BLACK);
    }
    HBox dateBox = new HBox(datetext);
    dateBox.setAlignment(Pos.CENTER_LEFT);

    topBox.getChildren().addAll(dateBox, logoBox);
    HBox.setHgrow(dateBox, Priority.ALWAYS);
    HBox.setHgrow(logoBox, Priority.ALWAYS);
    HBox.setMargin(dateBox, new Insets(0, 0, 0, 10));
    HBox.setMargin(logoBox, new Insets(0, 10, 0, 0));
    topBox.setMinHeight(prehieght);
    topBox.setMaxHeight(prehieght);

    // Hbox for notes
    HBox notebox = new HBox();
    notebox.getChildren().add(text);
    notebox.setAlignment(Pos.CENTER_LEFT);
    notebox.setMinHeight(prehieght);
    notebox.setMaxHeight(prehieght);

    // margin left
    HBox.setMargin(text, new Insets(0, 0, 0, 10));

    //Hbox for bottom buttons
    HBox buttonBox = new HBox();
    buttonBox.setMinHeight(prehieght);
    buttonBox.setMaxHeight(prehieght);
    buttonBox.getChildren().add(detail.getButton());
    buttonBox.getChildren().add(delete.getButton());
    buttonBox.setAlignment(Pos.BOTTOM_CENTER);
    buttonBox.setPadding(new Insets(0, 0, 10, 0));

    VBox.setVgrow(topBox, Priority.NEVER);
    VBox.setVgrow(notebox, Priority.NEVER);
    VBox.setVgrow(buttonBox, Priority.NEVER);

    box.getChildren().addAll(topBox, notebox, buttonBox);
    // padding left and right
    box.setPadding(new javafx.geometry.Insets(0, 10, 0, 10));

    delete
      .getButton()
      .setOnAction(e -> {
        try {
          App.getDBCON().deleteNotes(note.getNoteText());
          pane.getChildren().remove(box);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      });

    pane.getChildren().add(box);
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
