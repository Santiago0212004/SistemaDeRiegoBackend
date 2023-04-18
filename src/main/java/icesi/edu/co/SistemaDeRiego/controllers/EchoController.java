package icesi.edu.co.SistemaDeRiego.controllers;

import icesi.edu.co.SistemaDeRiego.entities.User;
import icesi.edu.co.SistemaDeRiego.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class EchoController {

    @Autowired
    ActivationRepository activationRepository;

    @Autowired
    ActivationTypeRepository activationTypeRepository;

    @Autowired
    ActuatorRepository actuatorRepository;

    @Autowired
    ActuatorTypeRepository actuatorTypeRepository;

    @Autowired
    MeasureRepository measureRepository;

    @Autowired
    PlantRepository plantRepository;

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    SensorTypeRepository sensorTypeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ZoneRepository zoneRepository;

    @GetMapping("echo")
    public String echo(){
        return "echo";
    }

    @PostMapping("another")
    public ResponseEntity<?> another(){
        return ResponseEntity.status(200).body("Another");
    }
}
