package org.comu;


import java.sql.*;

public class UserDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/musteridespanel";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Eğer kullanıcı varsa true döner
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
