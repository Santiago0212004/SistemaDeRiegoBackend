package icesi.edu.co.SistemaDeRiego.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "activations_types")
public class ActivationType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Long id;

    @Basic(optional = false)
    private String name;

    @OneToMany(mappedBy = "activations_types")
    @JsonIgnore
    private List<Activation> activations;

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

    public List<Activation> getActivations() {
        return activations;
    }

    public void setActivations(List<Activation> activations) {
        this.activations = activations;
    }
}
