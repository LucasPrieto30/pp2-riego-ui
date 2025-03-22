package pp2.riego.ui;

import com.riego.DispositivoRiego;
import com.riego.PluginLoader;
import com.riego.Sensor;
import com.riego.SensorHumedad;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private DispositivoRiego dispositivoRiego;
    private SensorHumedad sensorHumedad;
    private List<Sensor> sensoresDinamicos;

    public Controller(SensorHumedad sensorHumedad) {
        this.sensorHumedad = sensorHumedad;
        this.dispositivoRiego = new DispositivoRiego(sensorHumedad);
        this.sensoresDinamicos = new ArrayList<>();
    }

    public void agregarSensoresDinamicos(List<Sensor> sensores) {
        this.sensoresDinamicos.addAll(sensores);
        for (Sensor sensor : sensores) {
            sensor.agregarObservador(dispositivoRiego);
        }
    }

    public DispositivoRiego getDispositivoRiego() {
        return dispositivoRiego;
    }

    public List<Sensor> getSensoresDinamicos() {
        return sensoresDinamicos;
    }
    
    public List<Sensor> cargarSensoresDinamicos() {
        List<Sensor> sensores = PluginLoader.cargarPlugins();
        agregarSensoresDinamicos(sensores);
        return sensores;
    }
    
    public Sensor getSensorHumedad() {
        return sensorHumedad;
    }
}
