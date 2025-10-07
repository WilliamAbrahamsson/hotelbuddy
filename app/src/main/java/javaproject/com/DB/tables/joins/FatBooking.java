package javaproject.com.DB.tables.joins;

import java.util.ArrayList;
import javaproject.com.DB.tables.Booking;
import javaproject.com.DB.tables.Guest;
import javaproject.com.DB.tables.Room;
import javaproject.com.DB.tables.RoomBooking;

// This class is used to join the tables Booking, Guest and Room into one large object (More efficient).

public class FatBooking {

  private Booking booking;
  private Guest guest;
  private RoomBooking roomBooking;
  private ArrayList<Room> rooms;

  // LOCAL GETTERS (AND SETTERS)

  public Booking getBooking() {
    return booking;
  }

  public void setBooking(Booking booking) {
    this.booking = booking;
  }

  public Guest getGuest() {
    return guest;
  }

  public void setGuest(Guest guest) {
    this.guest = guest;
  }

  public ArrayList<Room> getRooms() {
    return rooms;
  }

  public void setRooms(ArrayList<Room> rooms) {
    this.rooms = rooms;
  }

  public void setRoomBooking(RoomBooking roomBooking) {
    this.roomBooking = roomBooking;
  }

  public void getRoomBooking(RoomBooking roomBooking) {
    this.roomBooking = roomBooking;
  }
}
