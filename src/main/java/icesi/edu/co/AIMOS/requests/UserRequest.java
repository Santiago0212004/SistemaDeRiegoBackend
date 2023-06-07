package icesi.edu.co.AIMOS.requests;


import icesi.edu.co.AIMOS.entities.User;

public class UserRequest {
    private User master;
    private User user;

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
