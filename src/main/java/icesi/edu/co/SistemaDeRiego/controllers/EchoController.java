package icesi.edu.co.SistemaDeRiego.controllers;

import icesi.edu.co.SistemaDeRiego.entities.*;
import icesi.edu.co.SistemaDeRiego.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
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
        User user = new User();
        user.setUsername("Example Username");
        user.setEmail("example@example.com");
        user.setPassword("example password");

        Zone zone = new Zone();
        zone.setName("Example Zone");
        zone.setDescription("Example Description");

        user.getZones().add(zone);

        zoneRepository.save(zone);
        userRepository.save(user);

        Plant plant = new Plant();
        plant.setName("Example Plant");
        plant.setDescription("Example Description");
        plant.setZone(zone);
        plantRepository.save(plant);

        SensorType sensorType = new SensorType();
        sensorType.setModel("Example Model");
        sensorType.setUnit("Example Unit");
        sensorTypeRepository.save(sensorType);

        Sensor sensor = new Sensor();
        sensor.setDescription("Example Description");
        sensor.setPlant(plant);
        sensor.setSensorType(sensorType);
        sensorRepository.save(sensor);

        Measure measure = new Measure();
        measure.setDate(new Date());
        measure.setValue(10.0);
        measure.setSensor(sensor);
        measureRepository.save(measure);

        ActuatorType actuatorType = new ActuatorType();
        actuatorType.setModel("Model example");
        actuatorTypeRepository.save(actuatorType);

        Actuator actuator = new Actuator();
        actuator.setDescription("Example Description");
        actuator.setPlant(plant);
        actuator.setActuatorType(actuatorType);
        actuatorRepository.save(actuator);

        ActivationType activationType = new ActivationType();
        activationType.setName("Example Activation Type");
        activationTypeRepository.save(activationType);

        Activation activation = new Activation();
        activation.setActivationType(activationType);
        activation.setDate(new Date());
        activation.setActuator(actuator);
        activationRepository.save(activation);

        return "echo";
    }





    @PostMapping("another")
    public ResponseEntity<?> another(){
        return ResponseEntity.status(200).body("Another");
    }
}
