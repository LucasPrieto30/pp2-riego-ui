package pp2.riego.ui;

import javax.swing.*;

import com.riego.SmartAqua;
import com.riego.SmartAquaFactory;

public class Main {
    public static void main(String[] args) {
    	SmartAqua smartAqua = SmartAquaFactory.crear("plugins/config/config.json");
        
        SwingUtilities.invokeLater(() -> new RiegoUI(smartAqua));

        SwingUtilities.invokeLater(() -> new TimeUI(smartAqua).setState(JFrame.ICONIFIED));
    }
}
