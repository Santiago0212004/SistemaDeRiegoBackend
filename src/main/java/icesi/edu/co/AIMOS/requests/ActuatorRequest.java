package icesi.edu.co.AIMOS.requests;

import icesi.edu.co.AIMOS.entities.Actuator;
import icesi.edu.co.AIMOS.entities.ActuatorType;
import icesi.edu.co.AIMOS.entities.Plant;
import icesi.edu.co.AIMOS.entities.User;

public class ActuatorRequest {

    private Actuator actuator;

    private User user;

    private Plant plant;

    private ActuatorType actuatorType;

    public Actuator getActuator() {
        return actuator;
    }

    public void setActuator(Actuator actuator) {
        this.actuator = actuator;
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

    public ActuatorType getActuatorType() {
        return actuatorType;
    }

    public void setActuatorType(ActuatorType actuatorType) {
        this.actuatorType = actuatorType;
    }
}
