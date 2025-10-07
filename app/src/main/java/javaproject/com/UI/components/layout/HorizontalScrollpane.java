package javaproject.com.UI.components.layout;

import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.views.app.App;

public class HorizontalScrollpane extends VBox {

  // scrollpane init
  ScrollPane scrollpane = new ScrollPane();
  HBox pane = new HBox();

  User user = App.getUser();

  public HorizontalScrollpane(int height) {
    // height
    setPrefHeight(height);
    setMinHeight(height);
    setMaxHeight(height);

    setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(this, Priority.ALWAYS);

    // add some padding
    setPadding(new javafx.geometry.Insets(10, 50, 10, 50));

    TitleBox paneDesc = new TitleBox("Current Status", "");

    scrollpane.setContent(pane);

    scrollpane.setPrefHeight(height);
    scrollpane.setMinHeight(height);
    scrollpane.setMaxHeight(height);

    // transparent scrollpane
    if(user.getTheme().equals("dark")){
      scrollpane.setStyle("-fx-background: #111;");
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

    // allow tap and drag to scroll
    scrollpane.setPannable(true);

    setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(scrollpane, Priority.ALWAYS);

    getChildren().addAll(paneDesc, scrollpane);
  }

  public void addBox() {
    HBox box = new HBox();
    box.setPrefWidth(200);
    box.setMinWidth(200);
    box.setMaxWidth(200);
    box.setPrefHeight(scrollpane.getPrefHeight());
    box.setMinHeight(scrollpane.getPrefHeight());
    box.setMaxHeight(scrollpane.getPrefHeight());
    box.setStyle("-fx-background-color: green;");

    // padding left and right
    box.setPadding(new javafx.geometry.Insets(0, 10, 0, 10));

    pane.getChildren().add(box);
  }
}
