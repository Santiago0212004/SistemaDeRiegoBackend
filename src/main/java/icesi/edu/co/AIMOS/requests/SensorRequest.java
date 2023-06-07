package icesi.edu.co.AIMOS.requests;

import icesi.edu.co.AIMOS.entities.Plant;
import icesi.edu.co.AIMOS.entities.Sensor;
import icesi.edu.co.AIMOS.entities.SensorType;
import icesi.edu.co.AIMOS.entities.User;

public class SensorRequest {

    private Sensor sensor;

    private User user;

    private Plant plant;

    private SensorType sensorType;

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }
}
