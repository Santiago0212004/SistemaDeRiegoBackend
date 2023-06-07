package icesi.edu.co.AIMOS.requests;


import icesi.edu.co.AIMOS.entities.ActuatorType;
import icesi.edu.co.AIMOS.entities.User;

public class ActuatorTypeRequest {
    private User user;
    private ActuatorType actuatorType;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ActuatorType getActuatorType() {
        return actuatorType;
    }

    public void setActuatorType(ActuatorType actuatorType) {
        this.actuatorType = actuatorType;
    }
}