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
    JLabel labelSinSensores;
    
    public RiegoUI(SmartWater smartWater) {
    	controlador = new Controller(smartWater, this);
    	
    	inicializar();

        setVisible(true);
    }
    
    private void inicializar() {
        setTitle("SmartWater - Sistema de Riego");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelSensores = new JPanel(new GridLayout(0, 1));
        
        labelRiego = new JLabel("Estado del Riego: --", SwingConstants.CENTER);
        

        for (Sensor sensor : controlador.getSensores()) {
            JLabel label = new JLabel(sensor.getClass().getSimpleName() + ": " + sensor.getValorMedido());
            sensoresLabels.put(sensor, label);
            panelSensores.add(label);
            sensor.agregarObservador(this);
        }

        panelSensores.revalidate();
        panelSensores.repaint();
        
        JPanel panelSuperior = new JPanel(new GridLayout(0, 1));
        panelSuperior.add(labelRiego);


        add(panelSensores, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);
    }

    @Override
    public void actualizar(Sensor sensor) {
        SwingUtilities.invokeLater(() -> {
            JLabel label = sensoresLabels.get(sensor);
            if (label != null) {
                label.setText(sensor.getClass().getSimpleName() + ": " + sensor.getValorMedido());
            }

            actualizarEstadoRiego(controlador.riegoEstaActivo());
        });
    }

    private void actualizarEstadoRiego(boolean riegoActivo) {
        if (riegoActivo) {
            labelRiego.setText("Estado del Riego: ACTIVADO ");
        } else {
            labelRiego.setText("Estado del Riego: DESACTIVADO ❌");
        }
    }
}
