package javaproject.com.UI.components.floatingchoices;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class FloatingChoices extends BorderPane {

  public FloatingChoices() {
    // padding bottom and right
    setPadding(new Insets(0, 40, 78, 0));

    VBox wrapper = new VBox();
    wrapper.setPrefWidth(50);
    wrapper.setMaxWidth(50);
    wrapper.setPrefHeight(200);
    wrapper.setMaxHeight(200);
    wrapper.setSpacing(10);

    // make wrapper inverted
    wrapper.setRotate(180);

    FloatingButton buttonLauncher = new FloatingButton("+");

    // on pressed
    buttonLauncher.setOnMousePressed(e -> {
      // make wrapper visible
      System.out.println("pressed");
    });

    wrapper.getChildren().add(buttonLauncher);

    // button 2
    FloatingButton button2 = new FloatingButton("2");
    wrapper.getChildren().add(button2);

    // button 3
    FloatingButton button3 = new FloatingButton("3");
    wrapper.getChildren().add(button3);

    setBottom(wrapper);
    setAlignment(wrapper, Pos.BOTTOM_RIGHT); // set alignment to bottom-right
  }
}
