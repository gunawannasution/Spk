package com.ahp.content.dao;
import com.ahp.content.model.HasilAlternatif;
import java.util.List;

public interface HasilAlternatifDAO {
    void insert(HasilAlternatif hasil);
    void deleteAll();
    List<HasilAlternatif> getAll();
    List<HasilAlternatif> getTopHasil(int limit);
}
