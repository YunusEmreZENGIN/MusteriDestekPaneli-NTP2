package org.comu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupportTicketDAO implements SupportTicketDAOInterface {

    private static final String URL = "jdbc:mysql://localhost:3306/musteridespanel";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public void createTicket(SupportTicket ticket) {
        String sql = "INSERT INTO destek_talepleri (customer_id, category, status, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ticket.getCustomerId());
            stmt.setString(2, ticket.getCategory());
            stmt.setString(3, ticket.getStatus());
            stmt.setString(4, ticket.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<SupportTicket> listTickets() {
        List<SupportTicket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM destek_talepleri";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                SupportTicket ticket = new SupportTicket(
                        rs.getString("id"),
                        rs.getString("customer_id"),
                        rs.getString("category"),
                        rs.getString("status"),
                        rs.getString("description")
                );
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public void updateTicketStatus(String ticketId, String status) {
        String sql = "UPDATE destek_talepleri SET status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, ticketId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTicket(String ticketId) {
        String sql = "DELETE FROM destek_talepleri WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SupportTicket getTicketById(String id) {
        String sql = "SELECT * FROM destek_talepleri WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SupportTicket(
                        rs.getString("id"),
                        rs.getString("customer_id"),
                        rs.getString("category"),
                        rs.getString("status"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    // Son 30 gündeki çözülen talepler
    public List<SupportTicket> getResolvedTicketsLast30Days() {
        String query = "SELECT * FROM destek_talepleri WHERE status = 'Çözülmüş' " +
                "AND created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
        return executeQuery(query);
    }

    // Teknik destek kategorisindeki talepler
    public List<SupportTicket> getTechnicalSupportTickets() {
        String query = "SELECT * FROM destek_talepleri WHERE category = 'Teknik Destek'";
        return executeQuery(query);
    }

    // Genel sorgu çalıştırıcı
    private List<SupportTicket> executeQuery(String query) {
        List<SupportTicket> tickets = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                SupportTicket ticket = new SupportTicket();
                ticket.setId(rs.getString("id")); // id string ise
                ticket.setCustomerId(rs.getString("customer_id")); // customer_id string ise
                ticket.setCategory(rs.getString("category"));
                ticket.setStatus(rs.getString("status"));
                ticket.setDescription(rs.getString("description"));
                ticket.setCreatedAt(rs.getTimestamp("created_at"));
                ticket.setUpdatedAt(rs.getTimestamp("updated_at"));

                tickets.add(ticket);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

}
