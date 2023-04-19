package icesi.edu.co.SistemaDeRiego.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Long id;

    @Basic(optional = false)
    private String password;

    @Basic(optional = false)
    private String email;

    @Basic(optional = false)
    private String username;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<UserZone> userZones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserZone> getUserZones() {
        return userZones;
    }

    public void setUserZones(List<UserZone> userZones) {
        this.userZones = userZones;
    }
}
