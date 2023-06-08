package icesi.edu.co.AIMOS.controllers;

import icesi.edu.co.AIMOS.entities.ActivationType;
import icesi.edu.co.AIMOS.entities.ActuatorType;
import icesi.edu.co.AIMOS.entities.Authorization;
import icesi.edu.co.AIMOS.entities.SensorType;
import icesi.edu.co.AIMOS.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
public class InitialController {
    @Autowired
    AuthorizationRepository authorizationRepository;

    @Autowired
    ActivationTypeRepository activationTypeRepository;

    @Autowired
    SensorTypeRepository sensorTypeRepository;

    @Autowired
    ActuatorTypeRepository actuatorTypeRepository;

    @PostMapping(value = "sh")
    public ResponseEntity<?> initialize() {
        List<Authorization> authorizations = (List<Authorization>) authorizationRepository.findAll();

        if (authorizations.isEmpty()){
            Authorization initialAuthorization = new Authorization();
            initialAuthorization.setType("MASTER");
            initialAuthorization.setValue("MASTER");
            authorizationRepository.save(initialAuthorization);

            ActivationType initialActivationType = new ActivationType();
            initialActivationType.setName("AUTOMATIC");
            activationTypeRepository.save(initialActivationType);

            ActuatorType initialActuatorType = new ActuatorType();
            initialActuatorType.setModel("WATER-PUMP");
            actuatorTypeRepository.save(initialActuatorType);

            SensorType sensorType1 = new SensorType();
            sensorType1.setModel("DHT11-Temperature");
            sensorType1.setUnit("Celsius");

            SensorType sensorType2 = new SensorType();
            sensorType2.setModel("DHT11-Humidity");
            sensorType2.setUnit("%");

            SensorType sensorType3 = new SensorType();
            sensorType3.setModel("Soil moisture");
            sensorType3.setUnit("%");

            sensorTypeRepository.save(sensorType1);
            sensorTypeRepository.save(sensorType2);
            sensorTypeRepository.save(sensorType3);

            return ResponseEntity.status(200).body("Program successfully initialized.");
        }
        return ResponseEntity.status(400).body("Program is already initialized.");
    }
}
