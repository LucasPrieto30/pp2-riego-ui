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
import java.util.Timer;
import java.util.TimerTask;


public class RiegoUI extends JFrame implements Observer {
    private Map<Sensor, JLabel> sensoresLabels = new HashMap<>();
    private JPanel panelSensores;
    private JLabel labelRiego;
    private Controller controlador;
    JButton btnCargarSensores;
 
    public RiegoUI(Controller controlador) {
        this.controlador = controlador;
        Sensor sensorHumedad = controlador.getSensorHumedad();
        setTitle("Sistema de Riego Automático");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelSensores = new JPanel(new GridLayout(0, 1));
        JLabel humedadLabel = new JLabel("SensorHumedad: " + sensorHumedad.getValor() + "%");
        panelSensores.add(humedadLabel);
        sensoresLabels.put(sensorHumedad, humedadLabel);
        sensorHumedad.agregarObservador(this);


        labelRiego = new JLabel("Estado del Riego: --", SwingConstants.CENTER);

        btnCargarSensores = new JButton("Cargar Sensores Dinámicos");
        btnCargarSensores.addActionListener(e -> cargarSensoresDinamicos());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCargarSensores);

        add(panelSensores, BorderLayout.CENTER);
        add(labelRiego, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cargarSensoresDinamicos() {
    	List<Sensor> sensores = controlador.cargarSensoresDinamicos();
        controlador.agregarSensoresDinamicos(sensores);

        if (sensores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron sensores dinámicos.", "Carga de Sensores", JOptionPane.WARNING_MESSAGE);
        }

        for (Sensor s : sensores) {
            JLabel label = new JLabel(s.getClass().getSimpleName() + ": " + s.getValor());
            panelSensores.add(label);
            sensoresLabels.put(s, label);

            s.agregarObservador(this);

            System.out.println("📌 Sensor agregado a la UI: " + s.getClass().getSimpleName());
        }

        panelSensores.revalidate();
        panelSensores.repaint();
        btnCargarSensores.setEnabled(false);
    }

    @Override
    public void actualizar(Sensor sensor) {
        SwingUtilities.invokeLater(() -> {
            JLabel label = sensoresLabels.get(sensor);
            if (label != null) {
                label.setText(sensor.getClass().getSimpleName() + ": " + sensor.getValor());
            }

            actualizarEstadoRiego(sensor.necesitaRiego());

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
