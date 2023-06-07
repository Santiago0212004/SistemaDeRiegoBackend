package icesi.edu.co.AIMOS.controllers;

import icesi.edu.co.AIMOS.entities.*;
import icesi.edu.co.AIMOS.repositories.*;
import icesi.edu.co.AIMOS.requests.SensorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class SensorController {
    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ZoneRepository zoneRepository;

    @Autowired
    PlantRepository plantRepository;

    @Autowired
    SensorTypeRepository sensorTypeRepository;


    @PostMapping(value = "sensors/add", consumes = "application/json")
    public ResponseEntity<?> addSensorToPlant(@RequestBody SensorRequest sensorRequest) {
        Sensor sensor = sensorRequest.getSensor();

        Optional<User> oUser = userRepository.findById(sensorRequest.getUser().getIdentification());
        Optional<Plant> oPlant = plantRepository.findById(sensorRequest.getPlant().getId());
        Optional<SensorType> oSensorType = sensorTypeRepository.findById(sensorRequest.getSensorType().getId());

        if (oUser.isPresent() && oPlant.isPresent() && oSensorType.isPresent()) {
            User userInRepository = oUser.get();
            Plant plantInRepository = oPlant.get();
            SensorType sensorTypeInRepository = oSensorType.get();


            if (userInRepository.getAuthorization().getType().equals("USER")) {
                if (userInRepository.getZones().contains(plantInRepository.getZone())) {
                    sensor.setPlant(plantInRepository);
                    sensor.setSensorType(sensorTypeInRepository);
                    sensorRepository.save(sensor);
                    return ResponseEntity.status(200).body("Sensor successfully added to the plant.");
                }
                return ResponseEntity.status(401).body("User is not associated with the zone where this plant is present.");
            }

            return ResponseEntity.status(401).body("User is not of type 'USER'.");
        }

        return ResponseEntity.status(404).body("User or Plant not found.");
    }

    @DeleteMapping(value = "sensors/delete", consumes = "application/json")
    public ResponseEntity<?> deleteSensor(@RequestBody SensorRequest sensorRequest) {
        Optional<User> oUser = userRepository.findById(sensorRequest.getUser().getIdentification());
        Optional<Sensor> oSensor = sensorRepository.findById(sensorRequest.getSensor().getId());

        if (oUser.isPresent()  && oSensor.isPresent()) {
            User userInRepository = oUser.get();
            Sensor sensorInRepository = oSensor.get();

            if (userInRepository.getAuthorization().getType().equals("USER")) {
                if (userInRepository.getZones().contains(sensorInRepository.getPlant().getZone())) {
                    sensorRepository.delete(sensorInRepository);
                    return ResponseEntity.status(200).body("Sensor successfully deleted.");
                }
                return ResponseEntity.status(401).body("User is not associated with the zone where this plant is present.");
            }

            return ResponseEntity.status(401).body("User is not of type 'USER'.");
        }

        return ResponseEntity.status(404).body("User or Actuator not found.");
    }

    @GetMapping(value = "sensors/measures")
    public ResponseEntity<?> getActivationsByActuator(@RequestHeader Long sensorId, @RequestHeader String identification) {
        Optional<User> oUser = userRepository.findById(identification);
        Optional<Sensor> oSensor = sensorRepository.findById(sensorId);

        if (oUser.isPresent()  && oSensor.isPresent()) {
            User userInRepository = oUser.get();
            Sensor sensorInRepository = oSensor.get();

            if (userInRepository.getAuthorization().getType().equals("USER")) {
                if (userInRepository.getZones().contains(sensorInRepository.getPlant().getZone())) {
                    return ResponseEntity.status(200).body(sensorInRepository.getMeasures());
                }
                return ResponseEntity.status(401).body("User is not associated with the zone where this plant is present.");
            }

            return ResponseEntity.status(401).body("User is not of type 'USER'.");
        }

        return ResponseEntity.status(404).body("User or Actuator not found.");
    }
}
