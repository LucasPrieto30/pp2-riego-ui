package pp2.riego.ui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.riego.Evaluador;
import com.riego.SmartAqua;

public class TimeUI extends JFrame {

    private SmartAqua smartAqua;

    public TimeUI(SmartAqua smartAqua) {
        this.smartAqua = smartAqua;

        setTitle("Simulador de Tiempo - SmartAqua");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton btnSimular = new JButton("Simular Paso del Tiempo");
        btnSimular.addActionListener(e -> simularEvaluaciones());

        add(btnSimular);

        setVisible(true);
    }

    private void simularEvaluaciones() {
        for (Evaluador evaluador : smartAqua.getEvaluadores()) {
            evaluador.evaluar();
        }
    }
}
