package javaproject.com.UI.components.dynamictable;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javaproject.com.DB.requests.RequestHandler;
import javaproject.com.DB.tables.RoomContent;
import javaproject.com.DB.tables.User;
import javaproject.com.UI.views.app.App;

public class DynamicTable extends BorderPane {

  Boolean show_content_box;
  protected TableView<ObservableList<String>> table;
  protected VBox searchbar;
  private TextField searchField;
  protected RequestHandler finalRequestHandler = App.getDBCON();
  private VBox tmp;
  // if the search bar is not empty, show the data that matches the search bar
  FilteredList<ObservableList<String>> filteredData;
  ArrayList<RoomContent> constraints;
  String current_search;

  User user = App.getUser();

  public DynamicTable() {
    current_search = "";
    show_content_box = false;
    // load stylesheet
    if(user.getTheme().equals("dark")){
      this.getStylesheets().add("/style/DynamicTableStyle.css");
    }
    else {
      this.getStylesheets().add("/style/LightDynamicTableStyle.css");
    }

    if(user.getTheme().equals("dark")){
      setStyle("-fx-background-color: #111111;");
    }
    else {
      setStyle("-fx-background-color: #ffffff;");
    }
    table = new TableView<>();

    // resizeing horizontally
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    // remove selecting cells
    table.getSelectionModel().setCellSelectionEnabled(false);

 

    filteredData = new FilteredList<>(
        table.getItems(),
        p -> true);

    constraints = new ArrayList<RoomContent>();
    // read anmd print out the data of the search bar

    searchbar = getSearchBar();
    searchField
        .textProperty()
        .addListener((observable, oldValue, newValue) -> {
          current_search = newValue;
          update_search(newValue);
        });

    tmp = new VBox();
    tmp.getChildren().add(searchbar);
    setTop(tmp);
    setCenter(table);
    VBox.setVgrow(table, Priority.ALWAYS);
  }

  // Add constraints to search example (Only King size beds)
  private void add_constraints(RoomContent content) {
    constraints.add(content);
  }

  private void remove_constraints(RoomContent content) {
    constraints.remove(content);
  }

  // Update the active search
  private void update_search(String newValue) {
    filteredData.setPredicate(row -> {
      Boolean inner = false;
      for (int i = 0; i < row.size(); i++) {
        String cell = row.get(i);
        if (constraints.size() == 0) {
          if (cell.toLowerCase().contains(newValue.toLowerCase())) {
            return true;
          }
        }

        if (cell.toLowerCase().contains(newValue.toLowerCase())) {
          inner = true;
        }
      }
      int tmp = 0;
      for (int x = 0; x < constraints.size(); x++) {
        if (row.get(3).toLowerCase().contains(constraints.get(x).getcontent().toLowerCase())) {
          tmp++;
        }
      }

      if (inner && tmp == constraints.size()) {
        return true;
      }

      return false;
    });

    // updates the table
    table.setItems(filteredData);
  }

  protected ArrayList<RoomContent> fetch_content() {
    return App.getDBCON().Fetch_all_room_content();
  }

  // create a search bar
  private VBox getSearchBar() {
    VBox searchBar = new VBox();

    searchField = new TextField();
    searchField.setPromptText("Search");
    searchField.setStyle("-fx-padding: 13 13 13 13;");

    // WIDTH 100%
    HBox.setHgrow(searchField, Priority.ALWAYS);

    searchBar.getChildren().addAll(searchField);
    return searchBar;
  }

  // add headers to the table
  public void addHeaders(String[] columns) {
    for (String column : columns) {
      TableColumn<ObservableList<String>, String> col = new TableColumn<>(
          column);

      final int columnIndex = table.getColumns().size();
      col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
      table.getColumns().add(col);
    }
  }

  // get the headers of the table
  public String[] getHeaders() {
    String[] headers = new String[table.getColumns().size()];
    for (int i = 0; i < table.getColumns().size(); i++) {
      headers[i] = table.getColumns().get(i).getText();
    }
    return headers;
  }

  // get the items of the table
  private ObservableList<ObservableList<String>> getItems() {
    return table.getItems();
  }

  // add an item to the table
  public void addItem(String[] row) {
    // create a new list to hold the row data
    ObservableList<String> rowData = FXCollections.observableArrayList();

    // add the data for each column
    for (String data : row) {
      rowData.add(data);
    }

    // add the row data to the table
    table.getItems().add(rowData);
  }

  public TableView<ObservableList<String>> getTable() {
    return this.table;
  }

  public void Add_checkboxses_room_contains() {
    ArrayList<RoomContent> content = fetch_content();
    VBox outer_content = new VBox();
    HBox content_box = new HBox();

    HBox.setHgrow(content_box, Priority.ALWAYS);

    // content box pos center left
    content_box.setAlignment(Pos.CENTER);

    // margin left and right
    outer_content.setPadding(new Insets(0, 10, 0, 10));

    for (int i = 0; i < content.size(); i++) {

      HBox checkboxWrapper = new HBox();

      // prefered width
      checkboxWrapper.setPrefWidth(200);

      CheckBox c = new CheckBox(content.get(i).getcontent());

      if(user.getTheme().equals("dark")){
        c.setStyle("-fx-text-fill: #FFFFFF;");
      }
      else {
        c.setStyle("-fx-text-fill: #545454;");
      }
      final int tmp = i;
      c.selectedProperty().addListener((o, oldValue, newValue) -> {
        if (newValue) {
          add_constraints(content.get(tmp));
          update_search(current_search);
        } else {
          remove_constraints(content.get(tmp));
          update_search(current_search);
        }
      });

      checkboxWrapper.getChildren().add(c);

      content_box.getChildren().addAll(checkboxWrapper);
    }
    content_box.setStyle("-fx-padding: 13 13 13 13;");
    

    outer_content.getChildren().add(content_box);
    HBox.setHgrow(outer_content, Priority.ALWAYS);
    tmp.getChildren().clear();
    tmp.getChildren().addAll(outer_content, searchbar);

  }
}
