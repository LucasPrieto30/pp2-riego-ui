package pp2.riego.ui;


import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

import com.riego.SensorHumedad;
public class MainUi {
    public static void main(String[] args) {
        SensorHumedad sensor = new SensorHumedad(40);  // Simulación de sensor

        // Crear la UI en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> new RiegoUI(sensor));

    }
}
