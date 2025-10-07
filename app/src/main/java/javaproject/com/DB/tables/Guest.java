package javaproject.com.DB.tables;

public class Guest {

  private int id;
  private String first_name;
  private String last_name;
  private String email;
  private String phone;
  private String address;
  private String zip;
  private String city;
  private String country;

  // Getters and setters

  // ID.
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  // First and last names.
  public String getFirstName() {
    return first_name;
  }

  public void setFirstName(String first_name) {
    this.first_name = first_name;
  }

  public String getLastName() {
    return last_name;
  }

  public void setLastName(String last_name) {
    this.last_name = last_name;
  }

  // Email.
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  // Phone.
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  // Address.
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  // Zip.
  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  // City.
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  // Country.
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  // to string
  @Override
  public String toString() {
    return (
      "Guest [id=" +
      id +
      ", first_name=" +
      first_name +
      ", last_name=" +
      last_name +
      ", email=" +
      email +
      ", phone=" +
      phone +
      ", address=" +
      address +
      ", zip=" +
      zip +
      ", city=" +
      city +
      ", country=" +
      country +
      "]"
    );
  }
}
