package com.ahp.template;

import com.ahp.helper.UIComponent;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FooterPanel extends JPanel{

    public FooterPanel() {
        setBackground(new Color(200,200,200));
        JLabel titleFooter=UIComponent.buatLabel("@2025- Skripsi ");
        titleFooter.setForeground(Color.DARK_GRAY);
        add(titleFooter);
    }
    
}
