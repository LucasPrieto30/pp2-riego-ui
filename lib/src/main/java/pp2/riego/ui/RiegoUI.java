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
    
    private Controller controlador;
    private JLabel labelRiego;
    private JPanel panelEvaluadores;
    private JTextArea historialActivaciones;
    private Map<Evaluador, JLabel> evaluadorLabels = new HashMap<>();
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public RiegoUI(SmartAqua smartAqua) {
        this.controlador = new Controller(smartAqua, this);
        inicializar();
        setVisible(true);
    }

    private void inicializar() {
        setTitle("SmartAqua - Sistema de Riego");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        labelRiego = new JLabel("Estado del riego: --", SwingConstants.CENTER);
        labelRiego.setFont(new Font("Arial", Font.BOLD, 18));

        panelEvaluadores = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollEvaluadores = new JScrollPane(panelEvaluadores);

        historialActivaciones = new JTextArea(10, 40);
        historialActivaciones.setEditable(false);
        JScrollPane scrollHistorial = new JScrollPane(historialActivaciones);
        scrollHistorial.setBorder(BorderFactory.createTitledBorder("Historial de Activaciones"));

        for (Evaluador eval : controlador.getEvaluadores()) {
            JLabel label = new JLabel(descripcionEvaluador(eval));
            evaluadorLabels.put(eval, label);
            panelEvaluadores.add(label);
            eval.agregarObservador(this);
        }

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(labelRiego, BorderLayout.NORTH);
        panelTop.add(scrollEvaluadores, BorderLayout.CENTER);

        add(panelTop, BorderLayout.CENTER);
        add(scrollHistorial, BorderLayout.SOUTH);
    }

    private String descripcionEvaluador(Evaluador eval) {
        return String.format("%s - Medición: %d - Umbral: %d - ¿Activa riego?: %s",
                eval.getClass().getSimpleName(),
                eval.getUltimaMedicion(),
                eval.getUmbral(),
                eval.getDebeRegar() ? "✅" : "❌");
    }

    @Override
    public void actualizar(Evaluador evaluador, boolean activar) {
        SwingUtilities.invokeLater(() -> {
            evaluadorLabels.get(evaluador).setText(descripcionEvaluador(evaluador));
            labelRiego.setText("Estado del riego: " + (controlador.riegoEstaActivo() ? "ACTIVADO " : "DESACTIVADO "));

            if (activar) {
                String log = String.format("[%s] %s - Medición: %d - Umbral: %d ✅",
                        LocalTime.now().format(timeFormatter),
                        evaluador.getClass().getSimpleName(),
                        evaluador.getUltimaMedicion(),
                        evaluador.getUmbral());
                historialActivaciones.append(log + "\n");
            }
        });
    }
}