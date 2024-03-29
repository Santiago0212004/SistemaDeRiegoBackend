package icesi.edu.co.AIMOS.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "actuator")
public class Actuator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @ManyToOne
    @JoinColumn(name = "actuator_type_id", nullable = false)
    private ActuatorType actuatorType;

    @OneToMany(mappedBy = "actuator", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Activation> activations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public ActuatorType getActuatorType() {
        return actuatorType;
    }

    public void setActuatorType(ActuatorType actuatorType) {
        this.actuatorType = actuatorType;
    }

    public List<Activation> getActivations() {
        return activations;
    }

    public void setActivations(List<Activation> activations) {
        this.activations = activations;
    }
}
