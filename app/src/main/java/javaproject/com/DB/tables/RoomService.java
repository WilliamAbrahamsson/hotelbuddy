package javaproject.com.DB.tables;

public class RoomService {

  private int id;
  private String description;
  private int room_booking_id;
  private String service_time; // ordered time

  // ID.
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  // Description.
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  // Room_booking_id.
  public int getRoom_booking_id() {
    return room_booking_id;
  }

  public void setRoom_booking_id(int room_booking_id) {
    this.room_booking_id = room_booking_id;
  }

  // Service_time.
  public String getService_time() {
    return service_time;
  }

  public void setService_time(String service_time) {
    this.service_time = service_time;
  }

  @Override
  public String toString() {
    return (
      "RoomService{" +
      "ID='" +
      id +
      '\'' +
      ", description='" +
      description +
      '\'' +
      ", room_booking_id='" +
      room_booking_id +
      '\'' +
      ", service_time='" +
      service_time +
      '\'' +
      '}'
    );
  }
}
