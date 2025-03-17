package pp2.riego.ui;

import javax.swing.*;

import com.riego.DispositivoRiego;
import com.riego.Observer;
import com.riego.PluginLoader;
import com.riego.PluginSensor;
import com.riego.Sensor;
import com.riego.SensorHumedad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.*;

public class RiegoUI extends JFrame implements Observer {
    private Map<Sensor, JLabel> sensoresLabels = new HashMap<>();
    private JPanel panelSensores;
    private JLabel labelRiego;
    private DispositivoRiego riego;
    private SensorHumedad sensorHumedad;
    private List<PluginSensor> sensoresDinamicos = new ArrayList<>();
    
    public RiegoUI(SensorHumedad sensor) {
    	this.sensorHumedad = sensor;
        this.riego = new DispositivoRiego(sensorHumedad);

        sensor.agregarObservador(this); // 📌 La UI observa el sensor principal

        setTitle("Sistema de Riego Automático");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel donde se mostrarán los sensores
        panelSensores = new JPanel(new GridLayout(0, 1));

        // Estado del riego
        labelRiego = new JLabel("Estado del Riego: --", SwingConstants.CENTER);

        // Botón para cargar sensores dinámicos
        JButton btnCargarSensores = new JButton("Cargar Sensores Dinámicos");
        btnCargarSensores.addActionListener(e -> cargarSensoresDinamicos());

        // Agregar todo al frame
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCargarSensores);

        add(panelSensores, BorderLayout.CENTER);
        add(labelRiego, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cargarSensoresDinamicos() {
        List<PluginSensor> sensores = PluginLoader.cargarPlugins();

        if (sensores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron sensores dinámicos.", "Carga de Sensores", JOptionPane.WARNING_MESSAGE);
        }

        for (PluginSensor s : sensores) {
            JLabel label = new JLabel(s.getClass().getSimpleName() + ": " + s.obtenerValor());
            panelSensores.add(label);
            System.out.println("📌 Sensor agregado a la UI: " + s.getClass().getSimpleName());
        }

        panelSensores.revalidate();
        panelSensores.repaint();
    }

    @Override
    public void actualizar(Sensor sensor) {
        SwingUtilities.invokeLater(() -> {
            JLabel label = sensoresLabels.get(sensor);
            if (label != null) {
                label.setText(sensor.getClass().getSimpleName() + ": " + sensor.getValor() + "%");
            }

            // 📌 Revisar si el riego debe activarse por cualquier sensor
            if (sensor instanceof SensorHumedad) {
                actualizarEstadoRiego(((SensorHumedad) sensor).necesitaRiego());
            }
        });
    }

    private void actualizarEstadoRiego(boolean riegoActivo) {
        if (riegoActivo) {
            labelRiego.setText("Estado del Riego: ACTIVADO 💧");
        } else {
            labelRiego.setText("Estado del Riego: DESACTIVADO ❌");
        }
    }
}
