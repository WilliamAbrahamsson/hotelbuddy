package javaproject.com.DB.tables;

public class RoomBooking {

  private int id;
  private String startDate;
  private String endDate;
  private int roomID;
  private int bookingID;

  // ID.

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String date) {
    startDate = date;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String date) {
    endDate = date;
  }

  public int getRoomId() {
    return roomID;
  }

  public void setRoomId(int id) {
    roomID = id;
  }

  public int getBookingId() {
    return bookingID;
  }

  public void setBookingId(int id) {
    bookingID = id;
  }


  @Override
  public String toString() {
    return (
      "Booking{" +
      "ID='" +
      id +
      '\'' +
      ", start='" +
      startDate +
      '\'' +
      ", end='" +
      endDate +
      '\'' +
      ", roomID='" +
      roomID +
      '\'' +
      ", bookingID='" +
      bookingID +
      '\'' +
      '}'
    );
  }
}
