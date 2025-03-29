package pp2.riego.ui;


import javax.swing.*;

import com.riego.SensorHumedad;
import com.riego.SmartWater;
public class MainUi {
    public static void main(String[] args) {
        SensorHumedad sensor = new SensorHumedad(40);
        SmartWater modelo = new SmartWater(sensor);
        
        SwingUtilities.invokeLater(() -> new RiegoUI(modelo));
    }
}
