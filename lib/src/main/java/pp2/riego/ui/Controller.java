package pp2.riego.ui;

import java.util.List;
import com.riego.Observer;
import com.riego.Sensor;
import com.riego.SmartWater;

public class Controller implements Observer {
   private SmartWater smartWater;
   private RiegoUI vista;
	
   public Controller(SmartWater smartWater, RiegoUI vista) {
        this.smartWater = smartWater;
        this.vista = vista;
        
        smartWater.getSensores().forEach(s -> s.agregarObservador(this));
    }

   public boolean riegoEstaActivo() {
	   return this.smartWater.riegoActivado();
   }
   
   public List<Sensor> getSensores() {
	   return this.smartWater.getSensores();
   }
	@Override
	public void actualizar(Sensor sensor, int medicion) {
		// TODO Auto-generated method stub
		
	}

}
