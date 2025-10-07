package javaproject.com.DB.tables;

public class Booking {

  private int guestId;
  private int id;
  private String booking_note;
  private String checked_in;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getGuestId() {
    return guestId;
  }

  public void setGuestId(int guestId) {
    this.guestId = guestId;
  }

  public String getBooking_note() {
    return booking_note;
  }

  public void setBooking_note(String note) {
    booking_note = note;
  }

  public String getChecked_in() {
    return checked_in;
  }

  public String setChecked_in(String checked_in) {
    return this.checked_in = checked_in;
  }

  // to string
  @Override
  public String toString() {
    return "Booking{" + "guestId=" + guestId + ", id=" + id + '}';
  }
}
