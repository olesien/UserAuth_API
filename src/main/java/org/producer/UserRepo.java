package org.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserRepo {

    Connection con;

    public UserRepo(Connection con) {
        this.con = con;
    }
    public User createUser(String name, String email, int age,  String gender, String password, boolean cookieConsent, boolean dataConsent) throws SQLException {
        String query = "INSERT INTO users (name, email, age, gender, password, cookie_consent, data_consent) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setString(2, email);
        stmt.setInt(3, age);
        stmt.setString(4, gender);
        stmt.setString(5, password);
        stmt.setBoolean(6, cookieConsent);
        stmt.setBoolean(7, dataConsent);
        int id = stmt.executeUpdate();
        return new User(id, name, email, age, gender, password, cookieConsent, dataConsent);
    }

    public ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String query
                = "select * from users"; // query to be run
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println("New user found");
            users.add(new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getInt("age"), rs.getString("gender"), rs.getString("password"), rs.getBoolean("cookie_consent"), rs.getBoolean("data_consent")));
        }
        return users;
    }
    public boolean deleteUser(int id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1,id);
        stmt.executeUpdate();
        return stmt.getUpdateCount() >= 1;
    }

    public boolean updateConsent(int id, Boolean cookieConsent, Boolean dataConsent) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("UPDATE users SET cookie_consent = ?, data_consent = ? WHERE id = ?");
        stmt.setBoolean(1,cookieConsent);
        stmt.setBoolean(2,dataConsent);
        stmt.setInt(3,id);
        stmt.executeUpdate();
        return stmt.getUpdateCount() >= 1;
    }

}
