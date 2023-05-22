package icesi.edu.co.SistemaDeRiego.requests;

import icesi.edu.co.SistemaDeRiego.entities.Authorization;
import icesi.edu.co.SistemaDeRiego.entities.User;

public class AuthorizationRequest {
    private User master;
    private Authorization authorization;

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }
}
