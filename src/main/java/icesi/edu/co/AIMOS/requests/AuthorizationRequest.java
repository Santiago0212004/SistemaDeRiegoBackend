package icesi.edu.co.AIMOS.requests;


import icesi.edu.co.AIMOS.entities.Authorization;
import icesi.edu.co.AIMOS.entities.User;

public class AuthorizationRequest {
    private User user;
    private Authorization authorization;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }
}
