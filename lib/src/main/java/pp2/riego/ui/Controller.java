package pp2.riego.ui;

import com.riego.Observer;
import com.riego.PluginLoader;
import com.riego.Sensor;
import com.riego.SmartWater;

import java.util.List;

public class Controller implements Observer {
   private SmartWater smartWater;
   private RiegoUI vista;
	
   public Controller(SmartWater smartWater, RiegoUI vista) {
        this.smartWater = smartWater;
        this.vista = vista;
        
        smartWater.getSensores().forEach(s -> s.agregarObservador(this));
    }


   public List<Sensor> cargarSensoresDinamicos() {
       List<Sensor> sensores = PluginLoader.cargarPlugins();
       for (Sensor sensor : sensores) {
    	   smartWater.agregarSensor(sensor);
           sensor.agregarObservador((Observer) this);
       }
       return sensores;
   }


	@Override
	public void actualizar(Sensor sensor) {
		// TODO Auto-generated method stub
		
	}

}
