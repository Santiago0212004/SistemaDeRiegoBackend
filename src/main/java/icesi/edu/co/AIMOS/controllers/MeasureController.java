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

    @Autowired
    UserRepository userRepository;


    @PostMapping(value = "measures/add",consumes = "application/json")
    public ResponseEntity<?> addMeasure(@RequestBody Measure measure, @RequestHeader String identification){
        measure.setDate(new Date());

        Optional<Sensor> oSensor = sensorRepository.findById(measure.getSensor().getId());
        Optional<User> oUser = userRepository.findById(identification);
        if(oSensor.isPresent() && oUser.isPresent()){
            Sensor sensorInRepository = oSensor.get();
            User userInRepository = oUser.get();
            if(sensorInRepository.getPlant().getZone().getUsers().contains(userInRepository)){
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
            return ResponseEntity.status(401).body("User is not associated with the zone of this sensor.");
        }

        return ResponseEntity.status(404).body("Sensor or user does not exist");
    }


}
