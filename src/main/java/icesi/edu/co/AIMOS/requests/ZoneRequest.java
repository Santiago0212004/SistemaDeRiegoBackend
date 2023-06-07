package icesi.edu.co.AIMOS.requests;


import icesi.edu.co.AIMOS.entities.User;
import icesi.edu.co.AIMOS.entities.Zone;

public class ZoneRequest {
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
