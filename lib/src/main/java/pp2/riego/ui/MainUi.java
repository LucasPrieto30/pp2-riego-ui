package pp2.riego.ui;


import javax.swing.*;

import com.riego.SmartWater;
public class MainUi {
    public static void main(String[] args) {
        SmartWater smartWater = new SmartWater();
        
        SwingUtilities.invokeLater(() -> new RiegoUI(smartWater));
    }
}
