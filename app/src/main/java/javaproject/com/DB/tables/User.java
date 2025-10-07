package javaproject.com.DB.tables;

public class User {

  private int id;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String email;
  private String privs;
  private String color;
  private String theme;

  // ID.

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  // Username.

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  // Password.

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  // First and last names.

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  // Email.

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  // Permissions.

  public String privileges() {
    return privs;
  }

  public void setPrivilegesStatus(String status) {
    this.privs = status;
  }

  // set the users color for this session (updated at login)
  public void setColor(String color) {
    this.color = color;
  }

  public String getColor() {
    return this.color;
  }

  // set the users preferred color theme
  public void setTheme(String theme) {
    this.theme = theme;
  }

  public String getTheme() {
    return this.theme;
  }

  // Get string representation.

  @Override
  public String toString() {
    return (
      "User{" +
      "ID='" +
      id +
      '\'' +
      ", username='" +
      username +
      '\'' +
      ", password='" +
      password +
      '\'' +
      ", firstName='" +
      firstName +
      '\'' +
      ", lastName='" +
      lastName +
      '\'' +
      ", email='" +
      email +
      '\'' +
      ", Privileges='" +
      privs +
      '\'' +
      '}'
    );
  }
}
