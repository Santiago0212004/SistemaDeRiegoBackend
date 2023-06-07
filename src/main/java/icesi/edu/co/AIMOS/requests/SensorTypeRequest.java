package icesi.edu.co.AIMOS.requests;

import icesi.edu.co.AIMOS.entities.SensorType;
import icesi.edu.co.AIMOS.entities.User;

public class SensorTypeRequest {
    private User user;
    private SensorType sensorType;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }
}
