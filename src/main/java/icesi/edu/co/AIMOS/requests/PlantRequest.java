package icesi.edu.co.AIMOS.requests;

import icesi.edu.co.AIMOS.entities.Plant;
import icesi.edu.co.AIMOS.entities.User;
import icesi.edu.co.AIMOS.entities.Zone;

public class PlantRequest {
    User user;
    Zone zone;
    Plant plant;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
