package pp2.riego.ui;


import javax.swing.*;

import com.riego.SmartWater;
public class MainUi {
    public static void main(String[] args) {
        SmartWater modelo = new SmartWater();
        
        SwingUtilities.invokeLater(() -> new RiegoUI(modelo));
    }
}
