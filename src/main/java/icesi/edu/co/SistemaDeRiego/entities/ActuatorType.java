package icesi.edu.co.SistemaDeRiego.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "actuator_types")
public class ActuatorType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Long id;

    @Basic(optional = false)
    private String model;

    @OneToMany(mappedBy = "actuator_types")
    @JsonIgnore
    private List<Actuator> actuators;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Actuator> getActuators() {
        return actuators;
    }

    public void setActuators(List<Actuator> actuators) {
        this.actuators = actuators;
    }
}
