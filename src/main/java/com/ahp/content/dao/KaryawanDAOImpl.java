package com.ahp.content.dao;
import com.ahp.content.model.Karyawan;
import com.ahp.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
public class KaryawanDAOImpl implements KaryawanDAO {
    public KaryawanDAOImpl() {}

    @Override
    public boolean insert(Karyawan kar) {
        String sql = "INSERT INTO alternatif (nik, nama, jabatan, alamat) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, kar.getNik());
            ps.setString(2, kar.getNama());
            ps.setString(3, kar.getJabatan());
            ps.setString(4, kar.getAlamat());
            
            // executeUpdate mengembalikan jumlah baris yg terpengaruh
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            Logger.getLogger(KaryawanDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean update(Karyawan kar) {
        String sql = "UPDATE alternatif SET nik = ?, nama = ?, jabatan = ?, alamat = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, kar.getNik());
            ps.setString(2, kar.getNama());
            ps.setString(3, kar.getJabatan());
            ps.setString(4, kar.getAlamat());
            ps.setInt(5, kar.getId());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            Logger.getLogger(KaryawanDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM alternatif WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            Logger.getLogger(KaryawanDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<Karyawan> getAll() {
        List<Karyawan> list = new ArrayList<>();
        String sql = "SELECT * FROM alternatif ORDER BY id";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Karyawan alt = new Karyawan();
                alt.setId(rs.getInt("id"));
                alt.setNik(rs.getString("nik"));
                alt.setNama(rs.getString("nama"));
                alt.setJabatan(rs.getString("jabatan"));
                alt.setAlamat(rs.getString("alamat"));
                list.add(alt);
            }
        } catch (SQLException ex) {
            Logger.getLogger(KaryawanDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // Cek apakah NIK sudah ada, kecuali data dengan excludeId (untuk update)
    @Override
    public boolean isNikExists(String nik, int excludeId) {
        String sql = "SELECT id FROM alternatif WHERE nik = ? AND id <> ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nik);
            ps.setInt(2, excludeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(KaryawanDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // cek nik sebelum insert data, jika ada maka tidak bisa ditambahkan
    @Override
    public boolean isNikExists(String nik) {
        String sql = "SELECT id FROM alternatif WHERE nik = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nik);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(KaryawanDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // Cari data alternatif berdasarkan id
    @Override
    public Karyawan getById(int id) {
        String sql = "SELECT * FROM alternatif WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Karyawan(
                        rs.getInt("id"),
                        rs.getString("nik"),
                        rs.getString("nama"),
                        rs.getString("jabatan"),
                        rs.getString("alamat")
                    );
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(KaryawanDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;  
    }

    @Override
    public Map<Integer, String> getAllAlternatifAsMap() {
        Map<Integer, String> map = new HashMap<>();
        String sql = "SELECT id, nama FROM alternatif";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getInt("id"), rs.getString("nama"));
            }
        } catch (SQLException e) {
            Logger.getLogger(KaryawanDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return map;

    }
}
