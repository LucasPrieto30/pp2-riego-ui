package pp2.riego.ui;

import javax.swing.*;

import com.riego.Evaluador;
import com.riego.Observer;
import com.riego.SmartAqua;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RiegoUI extends JFrame implements Observer {

    private JLabel labelEstadoRiego;
    private JPanel panelEvaluadores;
    private Map<Evaluador, JLabel> etiquetasEvaluadores;
    private JTextArea areaHistorial;
    private Controller controlador;

    public RiegoUI(SmartAqua smartAqua) {
    	this.controlador = new Controller(smartAqua, this);
        this.etiquetasEvaluadores = new HashMap<>();
        inicializarUI();
        setVisible(true);
    }

    private void inicializarUI() {
    	for (Evaluador evaluador : controlador.getEvaluadores()) {
    	    evaluador.agregarObservador(this);
    	}
        setTitle("SmartAqua - Sistema de Riego");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        labelEstadoRiego = new JLabel("Estado del Riego: --", SwingConstants.CENTER);
        labelEstadoRiego.setFont(new Font("Arial", Font.BOLD, 16));
        panelSuperior.add(labelEstadoRiego, BorderLayout.CENTER);

        panelEvaluadores = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollEvaluadores = new JScrollPane(panelEvaluadores);

        for (Evaluador evaluador : controlador.getEvaluadores()) {
            JLabel etiqueta = new JLabel(nombreEvaluador(evaluador));
            etiqueta.setFont(new Font("Arial", Font.PLAIN, 14));
            etiquetasEvaluadores.put(evaluador, etiqueta);
            panelEvaluadores.add(etiqueta);
        }

        areaHistorial = new JTextArea(6, 20);
        areaHistorial.setEditable(false);
        areaHistorial.setLineWrap(true);
        areaHistorial.setWrapStyleWord(true);
        JScrollPane scrollHistorial = new JScrollPane(areaHistorial);
        scrollHistorial.setBorder(BorderFactory.createTitledBorder("Historial de Activaciones"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollEvaluadores, BorderLayout.CENTER);
        add(scrollHistorial, BorderLayout.SOUTH);
    }

    private void evaluarCondiciones() {
        for (Evaluador evaluador : controlador.getEvaluadores()) {
            evaluador.evaluar();
            actualizarEtiqueta(evaluador);
        }

        actualizarEstadoRiego();
        actualizarHistorial();
    }

    private void actualizarEtiqueta(Evaluador evaluador) {
        JLabel etiqueta = etiquetasEvaluadores.get(evaluador);
        if (etiqueta != null) {
            String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String texto = nombreEvaluador(evaluador)
                    + " | Medición: " + evaluador.getUltimaMedicion()
                    + " | Umbral: " + evaluador.getUmbral()
                    + " | [" + hora + "]";
            etiqueta.setText(texto);
        }
    }

    private void actualizarEstadoRiego() {
        boolean activo = controlador.riegoEstaActivo();
        if (activo) {
            labelEstadoRiego.setText("Estado del Riego: ACTIVADO");
        } else {
            labelEstadoRiego.setText("Estado del Riego: DESACTIVADO");
        }
    }

    private void actualizarHistorial() {
        StringBuilder historial = new StringBuilder();
        for (String linea : controlador.getLogs()) {
            historial.append(linea).append("\n");
        }
        areaHistorial.setText(historial.toString());
    }

    private String nombreEvaluador(Evaluador e) {
        return e.getClass().getSimpleName();
    }

	@Override
	public void actualizar(Evaluador evaluador, boolean debeRegar) {
		actualizarEstadoRiego();
		actualizarEtiqueta(evaluador);
		actualizarHistorial();
	}
}
