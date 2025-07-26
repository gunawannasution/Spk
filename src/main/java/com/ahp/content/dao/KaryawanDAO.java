package com.ahp.content.dao;

import com.ahp.content.model.Karyawan;
import java.util.List;
import java.util.Map;

public interface KaryawanDAO {
    boolean insert(Karyawan kar);
    boolean update(Karyawan kar);
    boolean delete(int id);
    List<Karyawan> getAll();
    boolean isNikExists(String nik);
    boolean isNikExists(String nik, int excludeId);
    Karyawan getById(int id); 
    Map<Integer, String> getAllAlternatifAsMap();
}
