package javaproject.com.DB.tables;

public class Room {

  private int id;
  private String name;
  private String description;
  private int price;
  private int cleaned;
  private String roomContents;
  private String bedContents;

  // ID.

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  // Name.

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  // Desc.

  public String getDesc() {
    return description;
  }

  public void setDesc(String desc) {
    this.description = desc;
  }

  // Price.

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  // Room contents.

  public String getContents() {
    if (roomContents == null) {
      return "Null";
    }

    return roomContents;
  }

  public void setContents(String contents) {
    this.roomContents = contents;
  }

  public String getBeds() {
    if (bedContents == null) {
      return "Null";
    }

    return bedContents;
  }

  public void setBeds(String contents) {
    this.bedContents = contents;
  }

  // Cleaned status.

  public int get_cleaned_status() {
    return cleaned;
  }

  public void set_cleaned_status(int cleaned) {
    this.cleaned = cleaned;
  }

  // Get string representation.

  @Override
  public String toString() {
    return (
      "User{" +
      "ID='" +
      id +
      '\'' +
      ", name='" +
      name +
      '\'' +
      ", description='" +
      description +
      '\'' +
      ", price='" +
      price +
      '\'' +
      ", contents='" +
      roomContents +
      '\'' +
      '}'
    );
  }
}
