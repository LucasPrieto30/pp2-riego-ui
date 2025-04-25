package pp2.riego.ui;

import javax.swing.*;
import com.riego.Observer;
import com.riego.Sensor;
import com.riego.SmartWater;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;

public class RiegoUI extends JFrame implements Observer {

    private Controller controlador;
    private JPanel panelSensores;
    private JLabel labelRiego;
    private Map<Sensor, JLabel> sensoresLabels = new HashMap<>();
    private JLabel labelSinSensores;

    public RiegoUI(SmartWater smartWater) {
        controlador = new Controller(smartWater, this);
        inicializar();
        setVisible(true);
    }

    private void inicializar() {
        setTitle("🌿 SmartWater - Sistema de Riego Automático");
        setBounds(20, 300, 450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        Font fuenteGrande = new Font("SansSerif", Font.BOLD, 18);
        Font fuenteEstado = new Font("SansSerif", Font.BOLD, 20);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        labelRiego = new JLabel("Estado del Riego: ", SwingConstants.CENTER);
        labelRiego.setFont(fuenteEstado);
        panelSuperior.add(labelRiego, BorderLayout.CENTER);

        panelSensores = new JPanel();
        panelSensores.setLayout(new BoxLayout(panelSensores, BoxLayout.Y_AXIS));
        panelSensores.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        for (Sensor sensor : controlador.getSensores()) {
            JLabel label = new JLabel(sensor.getClass().getSimpleName() + ": " + sensor.getValorMedido() + " %");
            label.setFont(fuenteGrande);
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            sensoresLabels.put(sensor, label);
            panelSensores.add(label);
            sensor.agregarObservador(this);
        }

        add(panelSuperior, BorderLayout.NORTH);
        add(panelSensores, BorderLayout.CENTER);

    }

    @Override
    public void actualizar(Sensor sensor, int medicion) {
        SwingUtilities.invokeLater(() -> {
            JLabel label = sensoresLabels.get(sensor);
            if (label != null) {
                label.setText(sensor.getClass().getSimpleName() + ": " + medicion + " %");
            }
            actualizarEstadoRiego(controlador.riegoEstaActivo());
        });
    }

    private void actualizarEstadoRiego(boolean riegoActivo) {
        labelRiego.setText("Estado del Riego: " + (riegoActivo ? "ACTIVADO 💧" : "DESACTIVADO ❌"));
        labelRiego.setForeground(riegoActivo ? new Color(60, 179, 113) : new Color(0, 0, 0));

    }
}
