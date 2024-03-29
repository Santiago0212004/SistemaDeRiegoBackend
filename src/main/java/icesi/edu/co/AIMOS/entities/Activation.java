package icesi.edu.co.AIMOS.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "activation")
public class Activation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Long id;

    @Basic(optional = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "actuator_id", nullable = false)
    private Actuator actuator;

    @ManyToOne
    @JoinColumn(name = "activation_type_id", nullable = false)
    private ActivationType activationType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Actuator getActuator() {
        return actuator;
    }

    public void setActuator(Actuator actuator) {
        this.actuator = actuator;
    }

    public ActivationType getActivationType() {
        return activationType;
    }

    public void setActivationType(ActivationType activationType) {
        this.activationType = activationType;
    }
}
