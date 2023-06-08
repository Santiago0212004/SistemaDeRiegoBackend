package icesi.edu.co.AIMOS.controllers;

import icesi.edu.co.AIMOS.entities.*;
import icesi.edu.co.AIMOS.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class MeasureController {
    @Autowired
    MeasureRepository measureRepository;

    @Autowired
    ActivationRepository activationRepository;

    @Autowired
    ActivationTypeRepository activationTypeRepository;

    @Autowired
    ActuatorRepository actuatorRepository;

    @Autowired
    SensorRepository sensorRepository;


    @PostMapping(value = "measures/add",consumes = "application/json")
    public ResponseEntity<?> addMeasure(@RequestBody Measure measure){
        measure.setDate(new Date());

        Optional<Sensor> oSensor = sensorRepository.findById(measure.getSensor().getId());

        if(oSensor.isPresent()){
            Sensor sensorInRepository = oSensor.get();
            measure.setSensor(sensorInRepository);
            measureRepository.save(measure);
            if(measure.getSensor().getSensorType().getModel().equals("Soil moisture")){
                if(measure.getValue() <= measure.getSensor().getPlant().getHumidityLimit()){

                    ActivationType activationType = activationTypeRepository.findById(1L).get();
                    Actuator actuator = measure.getSensor().getPlant().getActuators().get(0);

                    Activation activation = new Activation();
                    activation.setActivationType(activationType);
                    activation.setDate(new Date());
                    activation.setActuator(actuator);

                    activationRepository.save(activation);

                    return ResponseEntity.status(201).body("Activate");
                }
                return ResponseEntity.status(200).body("Received");
            }
            return ResponseEntity.status(200).body("Received");
        }

        return ResponseEntity.status(400).body("Sensor does not exist");
    }


}
