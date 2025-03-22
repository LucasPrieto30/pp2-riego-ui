package pp2.riego.ui;


import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

import com.riego.SensorHumedad;
public class MainUi {
    public static void main(String[] args) {
        SensorHumedad sensor = new SensorHumedad(40); 
        Controller controlador = new Controller(sensor);
        
        SwingUtilities.invokeLater(() -> new RiegoUI(controlador));
    }
}
