package javaproject.com.UI.views.app.bookings;

import javaproject.com.DB.tables.joins.FatRoom;

public class SelectedRoom {

  public FatRoom room;
  public String startDate;
  public String endDate;

  public SelectedRoom(FatRoom room, String startDate, String endDate) {
    this.room = room;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public FatRoom getRoom() {
    return room;
  }

  public String getStartDate() {
    return startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setRoom(FatRoom room) {
    this.room = room;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }
}
