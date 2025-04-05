package pp2.riego.ui;


import javax.swing.*;

import com.riego.SmartWater;
import com.riego.SmartWaterFactory;
public class MainUi {
    public static void main(String[] args) {
    	SmartWater smartWater = SmartWaterFactory.crear("plugins/", "plugins/config/umbrales.json");
        
        SwingUtilities.invokeLater(() -> new RiegoUI(smartWater));
    }
}
