package icesi.edu.co.SistemaDeRiego.requests;

import icesi.edu.co.SistemaDeRiego.entities.User;
import icesi.edu.co.SistemaDeRiego.entities.Zone;

public class AddZoneRequest {
    private User user;
    private Zone zone;

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
}
