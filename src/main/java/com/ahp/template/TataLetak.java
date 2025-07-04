
package com.ahp.template;

import com.ahp.helper.UIComponent;
import com.ahp.helper.libButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TataLetak extends JPanel{

    public TataLetak() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        //inset=padding kiri kanan atas bawah
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel nameLabel = UIComponent.buatLabel("Nama:");
        JTextField nameField = UIComponent.buatTxt(20);

        JLabel genderLabel = UIComponent.buatLabel("Jenis Kelamin:");
        //JComboBox<String> genderCombo = UIComponent.buatCmb(new String[]{"Laki-laki", "Perempuan"});

        JCheckBox agreeBox = UIComponent.buatCheckBox("Saya setuju dengan syarat dan ketentuan");
        JButton btnSimpan=libButton.buatBtn(libButton.ButtonType.SIMPAN);
        JButton btnUpdate=libButton.buatBtn(libButton.ButtonType.UPDATE);
        //gridx kolom 0,1,2,3 dst
        //gridy baris 1, 2, dst
        //gridwidth lebar kolom
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);

        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(genderLabel, gbc);

        gbc.gridx = 1;
        //add(genderCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(agreeBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(btnSimpan,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(btnUpdate,gbc);
    }    
}
