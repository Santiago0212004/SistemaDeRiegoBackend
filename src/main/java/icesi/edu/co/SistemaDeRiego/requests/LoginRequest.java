package icesi.edu.co.SistemaDeRiego.requests;

public class LoginRequest {
    private String identification;
    private String password;

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String email) {
        this.identification = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
