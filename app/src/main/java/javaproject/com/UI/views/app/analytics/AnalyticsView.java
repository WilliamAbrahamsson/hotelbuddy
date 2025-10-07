package javaproject.com.UI.views.app.analytics;


// This view will demonstrate check-ins and check-outs
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javaproject.com.UI.components.button;
import javaproject.com.UI.views.app.View;
import javafx.geometry.*;

public class AnalyticsView extends View {
    HBox topbar;

    public AnalyticsView() {

        topbar = getTopBar();
        button refButton = new button("Refresh", "transparent",new Insets(10, 25, 10, 25));
        updateTopBar(false);
        topbar.getChildren().add(refButton.getButton());
        refButton.getButton().toBack();

        refButton.getButton().setOnMousePressed(e -> {
            refButton.getButton().setOnAction((ActionEvent event) -> {
            });
        });
    }

}
