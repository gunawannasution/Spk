package com.ahp.content.controller;

import com.ahp.content.dao.MatrixDAO;
import com.ahp.content.model.Matrix;

import java.util.List;

public class MatrixController {

    private final MatrixDAO matrixDAO;
    
    
    public MatrixController(MatrixDAO matrixDAO) {
        this.matrixDAO = matrixDAO;
    }

    public boolean simpanMatrixNormalisasi(List<Matrix> matrixList) {
        return matrixDAO.simpanMatrix(matrixList);
    }
}
