package javaproject.com.UI.views.app.bookings;

import java.util.ArrayList;
import javaproject.com.DB.tables.RoomBooking;
import javaproject.com.DB.tables.RoomContent;
import javaproject.com.DB.tables.joins.FatRoom;
import javaproject.com.UI.components.dynamictable.DynamicTable;

public class AddRoomTable extends DynamicTable {

  private BookingsTable bookingsTable; // add a SelectedRoomsView field

  public AddRoomTable(
    String start,
    String end,
    int bookingID,
    BookingsTable bookingsTable
  ) {
    this.Add_checkboxses_room_contains();
    this.bookingsTable = bookingsTable; // a way back :D

    ArrayList<FatRoom> fat_rooms = fetchFatRooms(start, end);

    // headers
    String[] headers = { "ID", "Name", "Price", "Contains", "Description" };
    this.addHeaders(headers);

    for (int ix = 0; ix < fat_rooms.size(); ix++) {
      FatRoom fat_room = fat_rooms.get(ix);

      ArrayList<RoomContent> roomContents = fat_rooms.get(ix).getRoomContents();

      String roomContentString = "";
      for (int jx = 0; jx < roomContents.size(); jx++) {
        roomContentString += roomContents.get(jx).getcontent();

        if (jx != roomContents.size() - 1) {
          roomContentString += ", ";
        }
      }

      String[] emptyRoomdata = {
        String.valueOf(fat_room.getRoom().getId()),
        fat_room.getRoom().getName(),
        String.valueOf(fat_room.getRoom().getPrice()),
        roomContentString,
        fat_room.getRoom().getDesc(),
      };
      this.addItem(emptyRoomdata);
    }

    // when pressed, print id of cell
    table.setOnMousePressed(e -> {
      String id = table.getSelectionModel().getSelectedItem().get(0);
      int roomID = Integer.parseInt(id);

      FatRoom roomObject;

      for (int i = 0; i < fat_rooms.size(); i++) {
        if (fat_rooms.get(i).getRoom().getId() == roomID) {
          roomObject = fat_rooms.get(i);

          // room_booking connector
          RoomBooking roomBooking = new RoomBooking();
          roomBooking.setBookingId(bookingID);
          roomBooking.setRoomId(roomID);
          roomBooking.setStartDate(start);
          roomBooking.setEndDate(end);

          // add to DB
          try {
            finalRequestHandler.createRoomBooking(roomBooking);
          } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }

          bookingsTable.addRoom(roomObject, roomBooking, start, end);
        }
      }

      // loop throught fat_rooms and print
      for (int i = 0; i < fat_rooms.size(); i++) {
        if (fat_rooms.get(i).getRoom().getId() == roomID) {
          System.out.println(fat_rooms.get(i).getRoom().getId());
          System.out.println(fat_rooms.get(i).getRoom().getName());
          System.out.println(fat_rooms.get(i).getRoom().getPrice());
          System.out.println(fat_rooms.get(i).getRoom().getDesc());

          ArrayList<RoomContent> roomContents = fat_rooms
            .get(i)
            .getRoomContents();

          for (int j = 0; j < roomContents.size(); j++) {
            System.out.println(roomContents.get(j).getId());
            System.out.println(roomContents.get(j).getcontent());
            System.out.println(roomContents.get(j).getType());
          }
        }
      }

      // Set the previous page as the center of the SelectedRoomsView object
      bookingsTable.setCenter(bookingsTable.previousPage);
      bookingsTable.setTop(bookingsTable.btn);
    });
  }

  private ArrayList<FatRoom> fetchFatRooms(String start, String end) {
    ArrayList<FatRoom> fat_rooms = new ArrayList<FatRoom>();

    try {
      fat_rooms = finalRequestHandler.getEmptyFatRooms(start, end);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    return fat_rooms;
  }
}
