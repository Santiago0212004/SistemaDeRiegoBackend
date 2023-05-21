package icesi.edu.co.SistemaDeRiego.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "user")
public class User {
    @Id
    @Basic(optional = false)
    private String identification;

    @Basic(optional = false)
    private String password;


    @Basic(optional = false)
    private String username;


    @ManyToOne
    @JoinColumn(name = "authorization", nullable = false)
    private Authorization authorization;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_zone",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "zone_id")
    )
    private List<Zone> zones;

    public User(){
        this.zones = new ArrayList<>();
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String id) {
        this.identification = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }
}
