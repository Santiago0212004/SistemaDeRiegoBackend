package icesi.edu.co.SistemaDeRiego.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "plants")
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Long id;

    @Basic(optional = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @OneToMany(mappedBy = "plants")
    @JsonIgnore
    private List<Actuator> actuators;

    @OneToMany(mappedBy = "plants")
    @JsonIgnore
    private List<Sensor> sensors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public List<Actuator> getActuators() {
        return actuators;
    }

    public void setActuators(List<Actuator> actuators) {
        this.actuators = actuators;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }
}
