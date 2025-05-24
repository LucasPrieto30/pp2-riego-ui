package pp2.riego.ui;

import java.util.List;

import com.riego.Evaluador;
import com.riego.Observer;
import com.riego.SmartAqua;

public class Controller implements Observer {
   private SmartAqua smartAqua;
   private RiegoUI vista;
	
   public Controller(SmartAqua smartWater, RiegoUI vista) {
        this.smartAqua = smartWater;
        this.vista = vista;
        
        smartWater.getEvaluadores().forEach(s -> s.agregarObservador(this));
    }

   public boolean riegoEstaActivo() {
	   return this.smartAqua.riegoActivado();
   }
   
   public List<Evaluador> getEvaluadores() {
	   return this.smartAqua.getEvaluadores();
   }
   
   public List<String> getLogs() {
	   return this.smartAqua.getLogger().getLogs();
   }
	@Override
	public void actualizar(Evaluador evaluador, boolean debeRegar) {
		// TODO Auto-generated method stub
		
	}
}
