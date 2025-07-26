package com.ahp.content.dao;

import com.ahp.content.model.Matrix;
import java.util.List;

public interface MatrixDAO {
    boolean simpanMatrix(List<Matrix> matrixData);
    double[][] ambilMatrixNormalisasi(List<String> kriteriaList) throws Exception;
    List<Matrix> getAll();
}

