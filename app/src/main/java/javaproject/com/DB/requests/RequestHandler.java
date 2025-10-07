package javaproject.com.DB.requests;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import javaproject.com.DB.tables.Booking;
import javaproject.com.DB.tables.Guest;
import javaproject.com.DB.tables.Notes;
import javaproject.com.DB.tables.Room;
import javaproject.com.DB.tables.RoomBooking;
import javaproject.com.DB.tables.RoomContent;
import javaproject.com.DB.tables.RoomService;
import javaproject.com.DB.tables.User;
import javaproject.com.DB.tables.joins.FatBooking;
import javaproject.com.DB.tables.joins.FatRoom;
import javaproject.com.UI.views.app.App;

public class RequestHandler {

  public String URL = "jdbc:mysql://";
  public final String username;
  public final String password;
  public Connection con;

  // Initialize new 'RequestHandler' object
  public RequestHandler() throws Exception {
    getDBCon tmp = new getDBCon();
    tmp.get_db_con();
    URL = URL + tmp.ip_con;
    username = tmp.username;
    password = tmp.password;

    con = DriverManager.getConnection(URL, username, password);
  }

  // ------------------------- USER ACCOUNTS AND PASSWORD HASHING
  // ------------------------- //

  private String hashPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] messageDigest = md.digest(password.getBytes());
      BigInteger no = new BigInteger(1, messageDigest);
      String hashtext = no.toString(16);
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }
      return hashtext;
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
  }

  // Create user.
  public Boolean createUser(User New_user) throws Exception {
    try {
      String query =
        "INSERT INTO users (username, password, first_name,last_name,email,privileges,theme) SELECT * FROM (SELECT ? as username,? as password,? as first_name,? as last_name,? as email,? as privileges,? as theme) AS tmp WHERE NOT EXISTS ( SELECT username FROM users WHERE username = ?) LIMIT 1;";
      PreparedStatement preStatment = con.prepareStatement(query);
      preStatment.setString(1, New_user.getUsername());
      preStatment.setString(2, hashPassword(New_user.getPassword()));
      preStatment.setString(3, New_user.getFirstName());
      preStatment.setString(4, New_user.getLastName());
      preStatment.setString(5, New_user.getEmail());
      preStatment.setString(6, New_user.privileges());
      preStatment.setString(7, New_user.getUsername());
      preStatment.setString(8, New_user.getTheme());
      int count = preStatment.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong!!!");
      System.out.println(e.getMessage());

      return false;
    }
    return true;
  }

  // Update user at index.
  public Boolean updateUser(User user) throws Exception {
    try {
      String query =
        "UPDATE users SET username = ? , password = ?, first_name = ?, last_name = ?, email = ?, privileges = ?, theme = ? WHERE id = ? AND NOT EXISTS ( SELECT * FROM users WHERE username = ? AND NOT id = ?);";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setString(1, user.getUsername());
      preStatement.setString(2, hashPassword(user.getPassword()));
      preStatement.setString(3, user.getFirstName());
      preStatement.setString(4, user.getLastName());
      preStatement.setString(5, user.getEmail());
      preStatement.setString(6, user.privileges());
      preStatement.setString(7, user.getTheme());
      preStatement.setString(8, user.getId() + "");
      preStatement.setString(9, user.getUsername());
      preStatement.setString(10, user.getId() + "");
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong.");
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  // Update user at index.
  public Boolean updateUserTheme(User user) throws Exception {
    try {
      String query = "UPDATE users SET theme = ? WHERE id = ?;";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setString(1, user.getTheme());
      preStatement.setString(2, user.getId() + "");
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong.");
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  // Delete user at index.
  public void deleteUser(int id) throws Exception {
    String query = "DELETE FROM users WHERE id = ?";
    PreparedStatement preStatment = con.prepareStatement(query);
    preStatment.setInt(1, id);
    preStatment.executeUpdate();
  }

  // Get user at username and password.
  // Call this function on login button click
  public User getUser(String username, String password) throws Exception {
    String query =
      "SELECT id, username, password, first_name, last_name, email, privileges, theme FROM users WHERE username = ? AND password=?";
    PreparedStatement preStatment = con.prepareStatement(query);
    preStatment.setString(1, username);
    preStatment.setString(2, hashPassword(password));

    ResultSet results = preStatment.executeQuery();

    User user = new User();
    try {
      if (results.next()) {
        // Initializing user object for each user in response.
        user.setId(results.getInt(1));
        user.setUsername(results.getString(2));
        user.setPassword(results.getString(3));
        user.setFirstName(results.getString(4));
        user.setLastName(results.getString(5));
        user.setEmail(results.getString(6));
        user.setPrivilegesStatus(results.getString(7));
        user.setTheme(results.getString(8));
      } else {
        return null;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return null;
    }
    return user;
  }

  // Get user at index.
  public User getUser(int id) throws Exception {
    String query =
      "SELECT id, username, password, first_name, last_name, email, privileges FROM users WHERE id = ?";
    PreparedStatement preStatment = con.prepareStatement(query);
    preStatment.setInt(1, id);

    ResultSet results = preStatment.executeQuery();

    User user = new User();
    try {
      if (results.next()) {
        // Initializing user object for each user in response.
        user.setId(results.getInt(1));
        user.setUsername(results.getString(2));
        user.setPassword(results.getString(3));
        user.setFirstName(results.getString(4));
        user.setLastName(results.getString(5));
        user.setEmail(results.getString(6));
        user.setPrivilegesStatus(results.getString(7));
      } else {
        return null;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return null;
    }
    return user;
  }

  // Get all users.
  public ArrayList<User> getUsers() throws Exception {
    Statement databaseConnection = con.createStatement();
    String query =
      "SELECT id, Username, first_name, last_name, email, privileges FROM users";
    ResultSet rs = databaseConnection.executeQuery(query);
    ArrayList<User> users = new ArrayList<User>();

    try {
      while (rs.next()) {
        // Initializing user object for each user in response.
        User user = new User();
        user.setId(rs.getInt(1));
        user.setUsername(rs.getString(2));
        user.setFirstName(rs.getString(3));
        user.setLastName(rs.getString(4));
        user.setEmail(rs.getString(5));
        user.setPrivilegesStatus(rs.getString(6));

        // Add to 'users' ArrayList.
        users.add(user);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return users;
  }

  // ------------------------ GUESTS (ALSO CALLED CUSTOMERS IN SOME PLACES)
  // ------------//

  // get all guests
  public ArrayList<Guest> getGuests() throws Exception {
    Statement databaseConnection = con.createStatement();
    String query =
      "SELECT id, first_name, last_name, email, phone, address, zip, city, country FROM guest";
    ResultSet rs = databaseConnection.executeQuery(query);
    ArrayList<Guest> guests = new ArrayList<Guest>();

    try {
      while (rs.next()) {
        Guest guest = new Guest();
        guest.setId(rs.getInt(1));
        guest.setFirstName(rs.getString(2));
        guest.setLastName(rs.getString(3));
        guest.setEmail(rs.getString(4));
        guest.setPhone(rs.getString(5));
        guest.setAddress(rs.getString(6));
        guest.setZip(rs.getString(7));
        guest.setCity(rs.getString(8));
        guest.setCountry(rs.getString(9));

        // Add to 'users' ArrayList.
        guests.add(guest);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return guests;
  }

  public Guest getGuest(int id) throws Exception {
    String query =
      "SELECT id, first_name, last_name, email, phone, address, zip, city, country FROM guest WHERE id = ?";
    PreparedStatement preStatement = con.prepareStatement(query);
    preStatement.setInt(1, id);
    ResultSet rs = preStatement.executeQuery();

    Guest guest = null;
    try {
      if (rs.next()) {
        guest = new Guest();
        guest.setId(rs.getInt(1));
        guest.setFirstName(rs.getString(2));
        guest.setLastName(rs.getString(3));
        guest.setEmail(rs.getString(4));
        guest.setPhone(rs.getString(5));
        guest.setAddress(rs.getString(6));
        guest.setZip(rs.getString(7));
        guest.setCity(rs.getString(8));
        guest.setCountry(rs.getString(9));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return guest;
  }

  // get guest by email string
  public Guest getGuestByEmail(String email) throws Exception {
    String query =
      "SELECT id, first_name, last_name, email, phone, address, zip, city, country FROM guest WHERE email = ?";
    PreparedStatement preStatement = con.prepareStatement(query);
    preStatement.setString(1, email);
    ResultSet rs = preStatement.executeQuery();

    Guest guest = null;
    try {
      if (rs.next()) {
        guest = new Guest();
        guest.setId(rs.getInt(1));
        guest.setFirstName(rs.getString(2));
        guest.setLastName(rs.getString(3));
        guest.setEmail(rs.getString(4));
        guest.setPhone(rs.getString(5));
        guest.setAddress(rs.getString(6));
        guest.setZip(rs.getString(7));
        guest.setCity(rs.getString(8));
        guest.setCountry(rs.getString(9));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return guest;
  }

  // create guest --> return guest id
  public Integer createGuest(Guest guest) throws Exception {
    Integer guestId = -1;
    try {
      String query =
        "INSERT INTO guest (first_name, last_name, email, phone, address, zip, city, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement preStatement = con.prepareStatement(
        query,
        Statement.RETURN_GENERATED_KEYS
      );
      preStatement.setString(1, guest.getFirstName());
      preStatement.setString(2, guest.getLastName());
      preStatement.setString(3, guest.getEmail());
      preStatement.setString(4, guest.getPhone());
      preStatement.setString(5, guest.getAddress());
      preStatement.setString(6, guest.getZip());
      preStatement.setString(7, guest.getCity());
      preStatement.setString(8, guest.getCountry());

      int count = preStatement.executeUpdate();
      if (count == 1) {
        ResultSet generatedKeys = preStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
          guestId = generatedKeys.getInt(1);
        }
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong!!!");
      System.out.println(e.getMessage());
    }
    return guestId;
  }

  // Delete guest at index.
  public void deleteGuest(int id) throws Exception {
    String query = "DELETE FROM guest WHERE id = ?";
    PreparedStatement preStatment = con.prepareStatement(query);
    preStatment.setInt(1, id);
    preStatment.executeUpdate();
  }

  // Update user at index.
  public Boolean updateGuest(Guest guest) throws Exception {
    try {
      String query =
        "UPDATE guest SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?,  zip = ?, city = ?, country = ? WHERE id = ?";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setString(1, guest.getFirstName());
      preStatement.setString(2, guest.getLastName());
      preStatement.setString(3, guest.getEmail());
      preStatement.setString(4, guest.getPhone());
      preStatement.setString(5, guest.getAddress());
      preStatement.setString(6, guest.getZip());
      preStatement.setString(7, guest.getCity());
      preStatement.setString(8, guest.getPhone());
      preStatement.setString(9, guest.getId() + "");
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong.");
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  // ------------------- ROOMS ---------------------------//

  // Get all rooms.
  public ArrayList<Room> getRooms() throws Exception {
    Statement databaseConnection = con.createStatement();
    String query =
      "SELECT id, name, description, price, room_contents, cleaned, bed_contents FROM rooms";
    ResultSet rs = databaseConnection.executeQuery(query);
    ArrayList<Room> rooms = new ArrayList<Room>();

    try {
      while (rs.next()) {
        // Initializing user object for each user in response.
        Room room = new Room();
        room.setId(rs.getInt(1));
        room.setName(rs.getString(2));
        room.setDesc(rs.getString(3));
        room.setPrice(rs.getInt(4));
        room.setContents(rs.getString(5));
        room.set_cleaned_status(rs.getInt(6));
        room.setBeds(rs.getString(7));

        // Add to 'users' ArrayList.
        rooms.add(room);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return rooms;
  }

  // get single room at id
  public Room getRoomById(Integer id) throws Exception {
    String query =
      "SELECT id, name, description, price, room_contents, cleaned, bed_contents FROM rooms WHERE id = ?";
    PreparedStatement preStatement = con.prepareStatement(query);
    preStatement.setInt(1, id);
    ResultSet rs = preStatement.executeQuery();

    Room room = null;
    try {
      if (rs.next()) {
        room = new Room();
        room.setId(rs.getInt(1));
        room.setName(rs.getString(2));
        room.setDesc(rs.getString(3));
        room.setPrice(rs.getInt(4));
        room.setContents(rs.getString(5));
        room.set_cleaned_status(rs.getInt(6));
        room.setBeds(rs.getString(7));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return room;
  }

  // Get rooms by IDs.
  public ArrayList<Room> getRoomsByIds(ArrayList<Integer> roomIds)
    throws Exception {
    if (roomIds.isEmpty()) {
      return new ArrayList<Room>(); // Return an empty list if there are no room IDs to fetch
    }

    // Convert list of room IDs to comma-separated string for SQL query
    String roomIdsString = roomIds
      .stream()
      .map(Object::toString)
      .collect(Collectors.joining(", ", "(", ")"));

    Statement databaseConnection = con.createStatement();
    String query =
      "SELECT id, name, description, price, room_contents, cleaned, bed_contents FROM rooms WHERE id IN " +
      roomIdsString;
    ResultSet rs = databaseConnection.executeQuery(query);
    ArrayList<Room> rooms = new ArrayList<Room>();

    try {
      while (rs.next()) {
        // Initializing room object for each room in response.
        Room room = new Room();
        room.setId(rs.getInt(1));
        room.setName(rs.getString(2));
        room.setDesc(rs.getString(3));
        room.setPrice(rs.getInt(4));
        room.setContents(rs.getString(5));
        room.set_cleaned_status(rs.getInt(6));
        room.setBeds(rs.getString(7));

        // Add to 'rooms' ArrayList.
        rooms.add(room);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return rooms;
  }

  // get available rooms
  public int getAvailableRooms() {
    int availableRooms = 0;

    try {
      Statement databaseConnection = con.createStatement();
      String query =
        "SELECT COUNT(*) AS available_rooms FROM rooms WHERE id NOT IN (SELECT room_id FROM room_booking WHERE CURDATE() BETWEEN DATE(start) AND DATE(end))";
      ResultSet rs = databaseConnection.executeQuery(query);

      if (rs.next()) {
        availableRooms = rs.getInt("available_rooms");
      }
    } catch (Exception e) {
      System.out.println("Error getting available rooms");
    }

    return availableRooms;
  }

  // create room
  public Boolean createRoom(Room new_room) throws Exception {
    try {
      String query =
        "INSERT INTO rooms (name, description, price) VALUES (?, ?, ?)";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setString(1, new_room.getName());
      preStatement.setString(2, new_room.getDesc());
      preStatement.setInt(3, new_room.getPrice());

      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong!!!");
      System.out.println(e.getMessage());

      return false;
    }
    return true;
  }

  public int createRoom_with_id(Room new_room) throws Exception {
    int id = 0;
    try {
      String query =
        "INSERT INTO rooms (name, description, price) VALUES (?, ?, ?)";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setString(1, new_room.getName());
      preStatement.setString(2, new_room.getDesc());
      preStatement.setInt(3, new_room.getPrice());

      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return 0;
      }

      String getQuery = "SELECT LAST_INSERT_ID() AS id";
      Statement databaseConnection = con.createStatement();
      ResultSet result = databaseConnection.executeQuery(getQuery);

      if (result.next()) {
        id = result.getInt("id");
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong!!!");
      System.out.println(e.getMessage());

      return id;
    }
    return id;
  }

  // update/edit room
  public Boolean updateRoom(Room room) throws Exception {
    try {
      String query =
        "UPDATE rooms SET name = ? , description = ?, price = ? WHERE id = ? AND NOT EXISTS ( SELECT * FROM rooms WHERE name = ? AND NOT id = ?);";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setString(1, room.getName());
      preStatement.setString(2, room.getDesc());
      preStatement.setString(3, room.getPrice() + "");
      preStatement.setString(4, room.getId() + "");
      preStatement.setString(5, room.getName());
      preStatement.setString(6, room.getId() + "");
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong.");
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  public Boolean removeContent_from_room(int id, int content_id)
    throws Exception {
    try {
      String query =
        "DELETE FROM `roomcontentconnector` WHERE room_id = ? AND content_id = ?";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setInt(1, id);
      preStatement.setInt(2, content_id);
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong.");
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  public Boolean removeContent(int content_id) throws Exception {
    try {
      String query = "DELETE FROM `roomcontent` WHERE id = ?";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setInt(1, content_id);
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong.");
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  public Boolean addContent_to_room(int id, int content_id) throws Exception {
    try {
      String query =
        "INSERT INTO `roomcontentconnector` (room_id,content_id) VALUES(?,?)";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setInt(1, id);
      preStatement.setInt(2, content_id);
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong.");
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  public Boolean addContent(String content, String type) throws Exception {
    if (type == "Other") {
      type = null;
    }
    try {
      String query = "INSERT INTO `roomcontent` (content,type) VALUES(?,?)";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setString(1, content);
      preStatement.setString(2, type);
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong.");
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  // Delete room at index
  public void deleteRoom(int id) throws Exception {
    String query = "DELETE FROM rooms WHERE id = ?";
    PreparedStatement preStatment = con.prepareStatement(query);
    preStatment.setInt(1, id);
    preStatment.executeUpdate();
  }

  // Get all empty room IDS
  public ArrayList<Integer> getEmptyRooms(String start, String end)
    throws Exception {
    String query =
      "SELECT rooms.id, rooms.name FROM rooms WHERE rooms.id NOT IN (SELECT room_booking.room_id FROM room_booking WHERE (room_booking.start >= ? AND room_booking.start <= ?) OR room_booking.end >=? AND room_booking.end <= ?)";
    PreparedStatement preStatement = con.prepareStatement(query);
    preStatement.setString(1, start);
    preStatement.setString(2, end);
    preStatement.setString(3, start);
    preStatement.setString(4, end);
    ResultSet rs = preStatement.executeQuery();

    ArrayList<Integer> rooms = new ArrayList<Integer>();
    try {
      while (rs.next()) {
        rooms.add(rs.getInt(1));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return rooms;
  }

  public ArrayList<FatRoom> getEmptyFatRooms(String start, String end)
    throws SQLException {
    String query =
      "SELECT r.id AS room_id, r.name, r.description, r.price, r.cleaned, rc.id AS rc_id, rc.content, rc.type " +
      "FROM rooms r " +
      "JOIN roomcontentconnector c ON r.id = c.room_id " +
      "JOIN roomcontent rc ON c.content_id = rc.id " +
      "WHERE r.id NOT IN ( " +
      " SELECT rb.room_id " +
      " FROM room_booking rb " +
      " WHERE (rb.start >= ? AND rb.start <= ?) OR (rb.end >= ? AND rb.end <= ?) " +
      ");";
    PreparedStatement statement = con.prepareStatement(query);
    statement.setString(1, start);
    statement.setString(2, end);
    statement.setString(3, start);
    statement.setString(4, end);

    ResultSet rs = statement.executeQuery();
    ArrayList<FatRoom> fatRooms = new ArrayList<FatRoom>();

    while (rs.next()) {
      // Room object
      Room room = new Room();
      room.setId(rs.getInt("room_id"));
      room.setName(rs.getString("name"));
      room.setDesc(rs.getString("description"));
      room.setPrice(rs.getInt("price"));

      // Room content object
      RoomContent roomContent = new RoomContent();
      roomContent.setId(rs.getInt("rc_id"));
      roomContent.setContent(rs.getString("content"));
      roomContent.setType(rs.getString("type"));

      boolean roomExists = false;
      for (FatRoom fatRoom : fatRooms) {
        if (fatRoom.getRoom().getId() == room.getId()) {
          fatRoom.getRoomContents().add(roomContent);
          roomExists = true;
          break;
        }
      }

      if (!roomExists) {
        FatRoom fatRoom = new FatRoom();
        fatRoom.setRoom(room);

        ArrayList<RoomContent> roomContents = new ArrayList<RoomContent>();

        roomContents.add(roomContent);
        fatRoom.setRoomContents(roomContents);
        fatRooms.add(fatRoom);
      }
    }
    return fatRooms;
  }

  public ArrayList<FatRoom> getAllFatRooms() throws SQLException {
    Statement databaseConnection = con.createStatement();

    String query =
      "SELECT r.id AS room_id, r.name, r.description, r.price, r.cleaned, rc.id AS rc_id, rc.content, rc.type " +
      "FROM rooms r " +
      "JOIN roomcontentconnector c ON r.id = c.room_id " +
      "JOIN roomcontent rc ON c.content_id = rc.id ";

    ResultSet rs = databaseConnection.executeQuery(query);
    ArrayList<FatRoom> fatRooms = new ArrayList<FatRoom>();

    while (rs.next()) {
      // Room object
      Room room = new Room();
      room.setId(rs.getInt("room_id"));
      room.setName(rs.getString("name"));
      room.setDesc(rs.getString("description"));
      room.setPrice(rs.getInt("price"));
      room.set_cleaned_status(rs.getInt("cleaned"));

      // Room content object
      RoomContent roomContent = new RoomContent();
      roomContent.setId(rs.getInt("rc_id"));
      roomContent.setContent(rs.getString("content"));

      // if not null
      if (rs.getString("type") != null) {
        roomContent.setType(rs.getString("type"));
      }

      boolean roomExists = false;
      for (FatRoom fatRoom : fatRooms) {
        if (fatRoom.getRoom().getId() == room.getId()) {
          fatRoom.getRoomContents().add(roomContent);
          roomExists = true;
          break;
        }
      }

      if (!roomExists) {
        FatRoom fatRoom = new FatRoom();
        fatRoom.setRoom(room);

        ArrayList<RoomContent> roomContents = new ArrayList<RoomContent>();

        roomContents.add(roomContent);
        fatRoom.setRoomContents(roomContents);
        fatRooms.add(fatRoom);
      }
    }
    return fatRooms;
  }

  public FatRoom getFatRoomAtRoomID(int roomID) throws SQLException {
    String query =
      "SELECT r.id AS room_id, r.name, r.description, r.price, r.cleaned, rc.id AS rc_id, rc.content, rc.type " +
      "FROM rooms r " +
      "JOIN roomcontentconnector c ON r.id = c.room_id " +
      "JOIN roomcontent rc ON c.content_id = rc.id " +
      "WHERE r.id = ?";

    PreparedStatement preStatement = con.prepareStatement(query);
    preStatement.setInt(1, roomID);
    ResultSet rs = preStatement.executeQuery();

    FatRoom fatRoom = new FatRoom();
    try {
      boolean roomExists = false;
      while (rs.next()) {
        if (!roomExists) {
          // Create Room object for first row
          Room room = new Room();
          room.setId(rs.getInt("room_id"));
          room.setName(rs.getString("name"));
          room.setDesc(rs.getString("description"));
          room.setPrice(rs.getInt("price"));
          fatRoom.setRoom(room);

          // Create list for RoomContent objects
          fatRoom.setRoomContents(new ArrayList<RoomContent>());

          roomExists = true;
        }

        // Create RoomContent object for each row
        RoomContent roomContent = new RoomContent();
        roomContent.setId(rs.getInt("rc_id"));
        roomContent.setContent(rs.getString("content"));
        roomContent.setType(rs.getString("type"));
        fatRoom.getRoomContents().add(roomContent);
      }
    } catch (Exception e) {
      System.out.println(e);
    }

    return fatRoom;
  }

  // get room services by room_booking_id
  public ArrayList<RoomService> getRoomServicesById(int room_booking_id)
    throws Exception {
    String query =
      "SELECT id, description, room_booking_id, service_time FROM room_service WHERE room_booking_id = ?";
    PreparedStatement preStatement = con.prepareStatement(query);
    preStatement.setInt(1, room_booking_id);
    ResultSet rs = preStatement.executeQuery();

    ArrayList<RoomService> roomServices = new ArrayList<RoomService>();

    try {
      while (rs.next()) {
        RoomService roomService = new RoomService();
        roomService.setId(rs.getInt("id"));
        roomService.setDescription(rs.getString("description"));
        roomService.setRoom_booking_id(rs.getInt("room_booking_id"));
        roomService.setService_time(rs.getString("service_time"));
        roomServices.add(roomService);
      }
    } catch (Exception e) {
      System.out.println(e);
    }

    return roomServices;
  }

  // remove roomservice by id

  public void removeRoomService(int room_service_id) {
    String query = "DELETE FROM room_service WHERE id = ?";
    try {
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setInt(1, room_service_id);
      preStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  // create room service
  public void createRoomService(RoomService roomService) {
    String query =
      "INSERT INTO room_service (description, room_booking_id) VALUES (?, ?)";
    try {
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setString(1, roomService.getDescription());
      preStatement.setInt(2, roomService.getRoom_booking_id());
      preStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println(e);
    }
  }


  // ------------------- ARRIVALS AND CHECKOUTS -------------------//

  public ArrayList<FatBooking> Fetch_Arrivals() throws Exception {
    Statement databaseConnection = con.createStatement();
    String query =
      "SELECT booking.id AS booking_id,guest.id AS guest_id,guest.first_name, guest.last_name, guest.phone,booking.checked_in, GROUP_CONCAT(DISTINCT booking.booking_note SEPARATOR ', ') as booking_notes FROM booking LEFT JOIN guest ON booking.guest_id = guest.id LEFT JOIN room_booking ON booking.id = room_booking.booking_id WHERE room_booking.start BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 12 HOUR) GROUP BY guest.id";
    ResultSet rs = databaseConnection.executeQuery(query);
    ArrayList<FatBooking> Arrivals = new ArrayList<FatBooking>();

    try {
      while (rs.next()) {
        Guest guest = new Guest();
        guest.setId(rs.getInt("guest_id"));
        guest.setFirstName(rs.getString("first_name"));
        guest.setLastName(rs.getString("last_name"));
        guest.setPhone(rs.getString("phone"));

        Booking booking = new Booking();
        booking.setId(rs.getInt("booking_id"));
        booking.setBooking_note(rs.getString("booking_notes"));
        String tmp = "False";
        if (rs.getInt("checked_in") == 1) {
          tmp = "True";
        }
        booking.setChecked_in(tmp);

        FatBooking fatBooking = new FatBooking();
        fatBooking.setBooking(booking);
        fatBooking.setGuest(guest);
        Arrivals.add(fatBooking);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return Arrivals;
  }

  // Fetch checkouts
  public ArrayList<FatBooking> Fetch_checkouts() throws Exception {
    Statement databaseConnection = con.createStatement();
    String query =
      "SELECT booking.id AS booking_id,guest.id AS guest_id,guest.first_name, guest.last_name, guest.phone,booking.checked_in, GROUP_CONCAT(DISTINCT booking.booking_note SEPARATOR ', ') as booking_notes FROM booking LEFT JOIN guest ON booking.guest_id = guest.id LEFT JOIN room_booking ON booking.id = room_booking.booking_id WHERE room_booking.end BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 12 HOUR) GROUP BY guest.id";
    ResultSet rs = databaseConnection.executeQuery(query);
    ArrayList<FatBooking> Checkouts = new ArrayList<FatBooking>();

    try {
      while (rs.next()) {
        Guest guest = new Guest();
        guest.setId(rs.getInt("guest_id"));
        guest.setFirstName(rs.getString("first_name"));
        guest.setLastName(rs.getString("last_name"));
        guest.setPhone(rs.getString("phone"));

        Booking booking = new Booking();
        booking.setId(rs.getInt("booking_id"));
        booking.setBooking_note(rs.getString("booking_notes"));
        String tmp = "False";
        if (rs.getInt("checked_in") == 1) {
          tmp = "True";
        }
        booking.setChecked_in(tmp);

        FatBooking fatBooking = new FatBooking();
        fatBooking.setBooking(booking);
        fatBooking.setGuest(guest);
        Checkouts.add(fatBooking);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return Checkouts;
  }

  // Checkin user
  public int check_in_user(int guest_id, int booking_id) throws Exception {
    try {
      String query =
        "UPDATE booking SET checked_in = 1 WHERE guest_id = ? AND checked_in = 0 and booking.id = ?";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setInt(1, guest_id);
      preStatement.setInt(2, booking_id);
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return 0;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return 2;
    }
    return 1;
  }

  // Check out user
  public int check_out_user(int guest_id, int booking_id) throws Exception {
    try {
      String query =
        "UPDATE booking SET checked_in = 0 WHERE guest_id = ? AND checked_in = 1 and booking.id = ?";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setInt(1, guest_id);
      preStatement.setInt(2, booking_id);
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return 0;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return 2;
    }
    return 1;
  }

  // ------------------- ACTUAL BOOKINGS -------------------//

  // Create booking --> return booking id
  public int createBooking(String booking) throws Exception {
    int id = 0;
    try {
      int nr = Integer.parseInt(booking);

      String insertQuery = "INSERT INTO booking (guest_id) VALUES (?)";
      PreparedStatement preStatement = con.prepareStatement(insertQuery);
      preStatement.setInt(1, nr);

      int count = preStatement.executeUpdate();

      String getQuery = "SELECT LAST_INSERT_ID() AS id";
      Statement databaseConnection = con.createStatement();
      ResultSet result = databaseConnection.executeQuery(getQuery);
      if (result.next()) {
        id = result.getInt("id");
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong!!!");
      System.out.println(e.getMessage());

      return 0;
    }
    return id;
  }

  // get all bookings
  public ArrayList<Booking> getBookings() throws Exception {
    Statement databaseConnection = con.createStatement();
    String query = "SELECT id, guest_id FROM booking";
    ResultSet rs = databaseConnection.executeQuery(query);
    ArrayList<Booking> bookings = new ArrayList<Booking>();

    try {
      while (rs.next()) {
        Booking booking = new Booking();
        booking.setId(rs.getInt(1));
        booking.setGuestId(rs.getInt(2));
        bookings.add(booking);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return bookings;
  }

  // delete booking
  public void deleteBooking(int id) throws Exception {
    try {
      String query = "DELETE FROM booking WHERE id = ?";
      PreparedStatement preStatment = con.prepareStatement(query);
      preStatment.setInt(1, id);
      preStatment.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Something went wrong!!!");
      System.out.println(e.getMessage());
    }
  }

  // ------------------------- FAT BOOKINGS (A join between booking, guest, room,
  // room_booking) ------------------------//

  public ArrayList<FatBooking> getFatBookings() throws SQLException {
    Statement databaseConnection = con.createStatement();

    // joined query between booking, guest and room_booking
    String query =
      "SELECT b.id AS booking_id, g.id AS guest_id, g.first_name, g.last_name, g.email, g.phone, g.address, g.zip, g.city, g.country, " +
      "r.id AS room_id, r.name, r.description, r.price, r.room_contents, r.cleaned " +
      "FROM booking b " +
      "JOIN guest g ON b.guest_id = g.id " +
      "JOIN room_booking rb ON b.id = rb.booking_id " +
      "JOIN rooms r ON rb.room_id = r.id";

    ResultSet rs = databaseConnection.executeQuery(query);
    ArrayList<FatBooking> fatBookings = new ArrayList<>();

    while (rs.next()) {
      // booking object
      Booking booking = new Booking();
      booking.setId(rs.getInt("booking_id"));
      booking.setGuestId(rs.getInt("guest_id"));

      // set other booking fields
      Guest guest = new Guest();
      guest.setId(rs.getInt("guest_id"));
      guest.setFirstName(rs.getString("first_name"));
      guest.setLastName(rs.getString("last_name"));
      guest.setEmail(rs.getString("email"));
      guest.setPhone(rs.getString("phone"));
      guest.setAddress(rs.getString("address"));
      guest.setZip(rs.getString("zip"));
      guest.setCity(rs.getString("city"));
      guest.setCountry(rs.getString("country"));

      // room object
      Room room = new Room();
      room.setId(rs.getInt("room_id"));
      room.setName(rs.getString("name"));
      room.setDesc(rs.getString("description"));
      room.setPrice(rs.getInt("price"));
      room.setContents(rs.getString("room_contents"));
      room.set_cleaned_status(rs.getInt("cleaned"));

      // check if there's already a fat booking object for this booking
      boolean bookingExists = false;
      for (FatBooking fatBooking : fatBookings) {
        if (fatBooking.getBooking().getId() == booking.getId()) {
          fatBooking.getRooms().add(room);
          bookingExists = true;
          break;
        }
      }

      // if there's no fat booking object for this booking, create one
      if (!bookingExists) {
        // create the fat booking object
        FatBooking fatBooking = new FatBooking();
        fatBooking.setBooking(booking);
        fatBooking.setGuest(guest);
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(room);
        fatBooking.setRooms(rooms);
        fatBookings.add(fatBooking);
      }
    }

    return fatBookings;
  }

  // ------------------- ROOM_BOOKINGS (THE CONNECTION BETWEEN ROOMS AND
  // BOOKINGS)!!!!!!! ------------------//

  // get all room_bookings
  public ArrayList<RoomBooking> getRoombookings() throws Exception {
    Statement databaseConnection = con.createStatement();
    String query =
      "SELECT id, start, end, room_id, booking_id FROM room_booking";
    ResultSet rs = databaseConnection.executeQuery(query);
    ArrayList<RoomBooking> bookings = new ArrayList<RoomBooking>();

    try {
      while (rs.next()) {
        RoomBooking booking = new RoomBooking();
        booking.setId(rs.getInt(1));
        booking.setStartDate(rs.getString(2));
        booking.setEndDate(rs.getString(3));
        booking.setRoomId(rs.getInt(4));
        booking.setBookingId(rs.getInt(5));

        bookings.add(booking);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return bookings;
  }

  // delete room_booking at index
  public void deleteRoomBooking(int id) throws Exception {
    String query = "DELETE FROM room_booking WHERE id = ?";
    PreparedStatement preStatment = con.prepareStatement(query);
    preStatment.setInt(1, id);
    preStatment.executeUpdate();
  }

  // edit room_booking
  public Boolean editRoomBooking(RoomBooking booking) throws Exception {
    try {
      String query = "UPDATE room_booking SET start =?, end =? WHERE id =? ";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setString(1, booking.getStartDate());
      preStatement.setString(2, booking.getEndDate());
      preStatement.setInt(3, booking.getId());
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong.");
      System.out.println(e.getMessage());
      return false;
    }

    return true;
  }

  // create room_booking
  public Boolean createRoomBooking(RoomBooking roomBooking) throws Exception {
    try {
      String query =
        "INSERT INTO room_booking (start, end, room_id, booking_id) VALUES (?, ?, ?, ?)";
      PreparedStatement preStatement = con.prepareStatement(query);
      preStatement.setString(1, roomBooking.getStartDate());
      preStatement.setString(2, roomBooking.getEndDate());
      preStatement.setInt(3, roomBooking.getRoomId());
      preStatement.setInt(4, roomBooking.getBookingId());

      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong!!!");
      System.out.println(e.getMessage());

      return false;
    }
    return true;
  }

  // get room booking at room id and booking id
  public RoomBooking getRoomBooking(int roomId, int bookingId)
    throws Exception {
    String query =
      "SELECT id, start, end, room_id, booking_id FROM room_booking WHERE room_id = ? AND booking_id = ?";
    PreparedStatement preStatement = con.prepareStatement(query);
    preStatement.setInt(1, roomId);
    preStatement.setInt(2, bookingId);
    ResultSet rs = preStatement.executeQuery();
    RoomBooking booking = new RoomBooking();

    try {
      while (rs.next()) {
        booking.setId(rs.getInt(1));
        booking.setStartDate(rs.getString(2));
        booking.setEndDate(rs.getString(3));
        booking.setRoomId(rs.getInt(4));
        booking.setBookingId(rs.getInt(5));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return booking;
  }

  public ArrayList<RoomContent> Fetch_all_room_content() {
    try {
      Statement databaseConnection = con.createStatement();
      String query = "SELECT id,content,type FROM `roomcontent`";
      ResultSet rs;
      rs = databaseConnection.executeQuery(query);
      ArrayList<RoomContent> all_content = new ArrayList<RoomContent>();
      try {
        while (rs.next()) {
          RoomContent content = new RoomContent();

          content.setId(rs.getInt(1));
          content.setContent(rs.getString(2));
          content.setType(rs.getString(3));
          all_content.add(content);
        }
      } catch (SQLException e) {
        System.out.println(e.getMessage());
        return new ArrayList<RoomContent>();
      }
      return all_content;
    } catch (SQLException e) {
      return new ArrayList<RoomContent>();
    }
  }

  public Boolean saveNote(String text) throws Exception{
    try{
      User user = App.getUser();
      //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      String query = "INSERT INTO notes(userid, noteText) VALUES (?, ?)";

      PreparedStatement preStatement = con.prepareStatement(query);

      preStatement.setInt(1, user.getId());
      preStatement.setString(2, text);
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
      return true;
    } catch (SQLException e) {
      System.out.println("Something went wrong.");
      System.out.println(e.getMessage());
      return false;
    }
  }



  public Boolean set_cleaning_status(int cleand, int room_id) {
    try {
      String query = "UPDATE rooms SET `cleaned` = ? WHERE id = ?;";
      PreparedStatement preStatement = con.prepareStatement(query);

      preStatement.setInt(1, cleand);
      preStatement.setInt(2, room_id);
      int count = preStatement.executeUpdate();
      if (!(count == 1)) {
        return false;
      }
      return true;
    } catch (SQLException e) {
      System.out.println("Something went wrong.");
      System.out.println(e.getMessage());
      return false;
    }
  }

  public ArrayList<Notes> getNotes() throws Exception{
    Statement databaseConnection = con.createStatement();
    String query = "SELECT * FROM notes";
    ResultSet rs = databaseConnection.executeQuery(query);
    ArrayList<Notes> NoteList = new ArrayList<Notes>();

    try{
      while (rs.next()){
        Notes note = new Notes();
        note.setUserID(rs.getInt(1));
        note.setNoteText(rs.getString(2));
        note.setCreationDate(rs.getString(3));

        NoteList.add(note);
      }
    } catch (SQLException e){
      System.out.println(e.getMessage());
    }
    
    return NoteList;
  }

  public void deleteNotes(String note) throws Exception {
    String query = "DELETE FROM notes WHERE noteText = ?";
    PreparedStatement preStatment = con.prepareStatement(query);
    preStatment.setString(1, note);
    preStatment.executeUpdate();
  }
}
