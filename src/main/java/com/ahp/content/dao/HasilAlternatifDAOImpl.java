package com.ahp.content.dao;

import com.ahp.content.model.HasilAlternatif;
import com.ahp.util.DBConnection;
import java.sql.*;
import java.util.*;

public class HasilAlternatifDAOImpl implements HasilAlternatifDAO {

    @Override
    public void insert(HasilAlternatif hasil) {
        String sql = "INSERT INTO hasil_alternatif (id_alternatif, skor, peringkat, rekomendasi) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hasil.getIdAlternatif());
            ps.setDouble(2, hasil.getSkor());
            ps.setInt(3, hasil.getPeringkat());
            ps.setString(4, hasil.getRekomendasi());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }


    @Override
    public void deleteAll() {
        String sql = "DELETE FROM hasil_alternatif";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }

 
    @Override
    public List<HasilAlternatif> getAll() {
        List<HasilAlternatif> list = new ArrayList<>();
        String sql = "SELECT * FROM hasil_alternatif";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                HasilAlternatif hasil = new HasilAlternatif(
                    rs.getInt("id"),
                    rs.getInt("id_alternatif"),
                    rs.getDouble("skor"),
                    rs.getInt("peringkat"),
                    rs.getString("rekomendasi")
                );
                list.add(hasil);
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return list;
    }
}
