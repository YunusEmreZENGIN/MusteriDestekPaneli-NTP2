package org.comu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusteriDAO_MySQL implements MusteriDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/musteridespanel";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public MusteriDAO_MySQL() {
        // Veritabanı bağlantısını buraya ekleyebilirsiniz
    }

    @Override
    public void ekle(Musteri m) {
        String sql = "INSERT INTO musteriler (ad, soyad, email, telefon) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getAd());
            stmt.setString(2, m.getSoyad());
            stmt.setString(3, m.getEmail());
            stmt.setString(4, m.getTelefon());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Musteri> listele() {
        List<Musteri> liste = new ArrayList<>();
        String sql = "SELECT * FROM musteriler";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Musteri m = new Musteri(
                        rs.getString("id"),
                        rs.getString("ad"),
                        rs.getString("soyad"),
                        rs.getString("email"),
                        rs.getString("telefon")
                );
                liste.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public void guncelle(Musteri m) {
        String sql = "UPDATE musteriler SET ad = ?, soyad = ?, email = ?, telefon = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getAd());
            stmt.setString(2, m.getSoyad());
            stmt.setString(3, m.getEmail());
            stmt.setString(4, m.getTelefon());
            stmt.setString(5, m.getId()); // Burada id'yi String olarak alıyoruz
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sil(String id) {
        String sql = "DELETE FROM musteriler WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(id));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
