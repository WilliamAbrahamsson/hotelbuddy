package javaproject.com.DB.tables.joins;

import java.util.ArrayList;
import javaproject.com.DB.tables.Room;
import javaproject.com.DB.tables.RoomContent;

public class FatRoom {

  private Room room;
  private ArrayList<RoomContent> roomContents;

  // LOCAL GETTERS (AND SETTERS)

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public ArrayList<RoomContent> getRoomContents() {
    return roomContents;
  }

  public void setRoomContents(ArrayList<RoomContent> roomContents) {
    this.roomContents = roomContents;
  }

  // tostring
  public String toString() {
    return "FatRoom [room=" + room + ", roomContents=" + roomContents + "]";
  }
}
