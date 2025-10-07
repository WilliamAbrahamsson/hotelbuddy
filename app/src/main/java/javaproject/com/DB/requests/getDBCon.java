package javaproject.com.DB.requests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class getDBCon {
    protected String ip_con;
    protected String username;
    protected String password;

    public void get_db_con() {
        try {
            File myObj = new File("db.con.txt");
            Scanner myReader = new Scanner(myObj);
            String data = myReader.nextLine();
            String[] parts = data.split(",");
            ip_con = parts[0];
            username = parts[1];
            password = parts[2];
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Something is wrong in ur db.con file");
            e.printStackTrace();
        }

    }
}
