package pp2.riego.ui;

import javax.swing.*;

import com.riego.Observer;
import com.riego.Sensor;
import com.riego.SmartWater;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.*;


public class RiegoUI extends JFrame implements Observer {
	
	private SmartWater smartWater;
	private Controller controlador;
	
	private JPanel panelSensores;
	private JLabel labelRiego;
    private Map<Sensor, JLabel> sensoresLabels = new HashMap<>();
    JButton btnCargarSensores;
    JLabel labelSinSensores;
    
    public RiegoUI(SmartWater smartWater) {
    	this.smartWater = smartWater;
    	
    	smartWater.getSensores().forEach(s -> s.agregarObservador(this));
    	
    	controlador = new Controller(smartWater, this);
    	
    	inicializar();

        setVisible(true);
    }
    
    private void inicializar() {
        setTitle("Sistema de Riego Automático");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        labelRiego = new JLabel("Estado del Riego: --", SwingConstants.CENTER);
        labelSinSensores = new JLabel("No hay sensores cargados aún.");
        panelSensores = new JPanel(new GridLayout(0, 1));
        if (smartWater.getSensores().isEmpty()) {
            panelSensores.add(labelSinSensores);
        }
        for (Sensor sensor : smartWater.getSensores()) {
        	panelSensores.remove(labelSinSensores);
            JLabel label = new JLabel(sensor.getClass().getSimpleName() + ": " + sensor.getValor());
            panelSensores.add(label);
            sensoresLabels.put(sensor, label);
        }
        
        btnCargarSensores = new JButton("Cargar Sensores Dinámicos");
        btnCargarSensores.addActionListener(e -> cargarSensoresDinamicos());
        

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCargarSensores);
        
        JPanel panelSuperior = new JPanel(new GridLayout(0, 1));
        panelSuperior.add(labelRiego);


        add(panelSensores, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarSensoresDinamicos() {
    	List<Sensor> sensores = controlador.cargarSensoresDinamicos();

        if (sensores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron sensores dinámicos.", "Carga de Sensores", JOptionPane.WARNING_MESSAGE);
        }
        panelSensores.remove(labelSinSensores);
        for (Sensor s : sensores) {
            JLabel label = new JLabel(s.getClass().getSimpleName() + ": " + s.getValor());
            panelSensores.add(label);
            sensoresLabels.put(s, label);
            s.agregarObservador(this);
            System.out.println(" Sensor agregado a la UI: " + s.getClass().getSimpleName());
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
