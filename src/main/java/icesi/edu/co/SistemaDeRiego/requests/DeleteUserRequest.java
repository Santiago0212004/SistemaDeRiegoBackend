package icesi.edu.co.SistemaDeRiego.requests;

import icesi.edu.co.SistemaDeRiego.entities.User;

public class DeleteUserRequest {
    private User master;
    private User deletingUser;

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public User getDeletingUser() {
        return deletingUser;
    }

    public void setDeletingUser(User deletingUser) {
        this.deletingUser = deletingUser;
    }
}
