package icesi.edu.co.AIMOS.controllers;

import icesi.edu.co.AIMOS.entities.*;
import icesi.edu.co.AIMOS.repositories.*;
import icesi.edu.co.AIMOS.requests.ActuatorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class ActuatorController {

    @Autowired
    ActuatorRepository actuatorRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ZoneRepository zoneRepository;

    @Autowired
    PlantRepository plantRepository;

    @Autowired
    ActuatorTypeRepository actuatorTypeRepository;


    @PostMapping(value = "actuators/add", consumes = "application/json")
    public ResponseEntity<?> addActuatorToPlant(@RequestBody ActuatorRequest actuatorRequest) {
        Actuator actuator = actuatorRequest.getActuator();

        Optional<User> oUser = userRepository.findById(actuatorRequest.getUser().getIdentification());
        Optional<Plant> oPlant = plantRepository.findById(actuatorRequest.getPlant().getId());
        Optional<ActuatorType> oActuatorType = actuatorTypeRepository.findById(actuatorRequest.getActuatorType().getId());

        if (oUser.isPresent() && oPlant.isPresent() && oActuatorType.isPresent()) {
            User userInRepository = oUser.get();
            Plant plantInRepository = oPlant.get();
            ActuatorType actuatorTypeInRepository = oActuatorType.get();

            Zone zoneInRepository = plantInRepository.getZone();


            if (userInRepository.getAuthorization().getType().equals("USER")) {
                if (userInRepository.getZones().contains(zoneInRepository)) {
                    actuator.setPlant(plantInRepository);
                    actuator.setActuatorType(actuatorTypeInRepository);
                    actuatorRepository.save(actuator);
                    return ResponseEntity.status(200).body("Actuator successfully added to the plant.");
                }
                return ResponseEntity.status(401).body("User is not associated with the zone where this plant is present.");
            }

            return ResponseEntity.status(401).body("User is not of type 'USER'.");
        }

        return ResponseEntity.status(404).body("User or Plant not found.");
    }

    @DeleteMapping(value = "actuators/delete", consumes = "application/json")
    public ResponseEntity<?> deleteActuator(@RequestBody ActuatorRequest actuatorRequest) {
        Optional<User> oUser = userRepository.findById(actuatorRequest.getUser().getIdentification());
        Optional<Actuator> oActuator = actuatorRepository.findById(actuatorRequest.getActuator().getId());

        if (oUser.isPresent()  && oActuator.isPresent()) {
            User userInRepository = oUser.get();
            Actuator actuatorInRepository = oActuator.get();
            Plant plantInRepository = actuatorInRepository.getPlant();
            Zone zoneInRepository = plantInRepository.getZone();

            if (userInRepository.getAuthorization().getType().equals("USER")) {
                if (userInRepository.getZones().contains(zoneInRepository)) {
                    actuatorRepository.delete(actuatorInRepository);
                    return ResponseEntity.status(200).body("Actuator successfully deleted.");
                }
                return ResponseEntity.status(401).body("User is not associated with the zone where this plant is present.");
            }

            return ResponseEntity.status(401).body("User is not of type 'USER'.");
        }

        return ResponseEntity.status(404).body("User or Actuator not found.");
    }

    @GetMapping(value = "actuators/activations")
    public ResponseEntity<?> getActivationsByActuator(@RequestHeader Long actuatorId, @RequestHeader String identification) {
        Optional<User> oUser = userRepository.findById(identification);
        Optional<Actuator> oActuator = actuatorRepository.findById(actuatorId);

        if (oUser.isPresent()  && oActuator.isPresent()) {
            User userInRepository = oUser.get();
            Actuator actuatorInRepository = oActuator.get();

            if (userInRepository.getAuthorization().getType().equals("USER")) {
                if (userInRepository.getZones().contains(actuatorInRepository.getPlant().getZone())) {
                    return ResponseEntity.status(200).body(actuatorInRepository.getActivations());
                }
                return ResponseEntity.status(401).body("User is not associated with the zone where this plant is present.");
            }

            return ResponseEntity.status(401).body("User is not of type 'USER'.");
        }
        return ResponseEntity.status(404).body("User or Actuator not found.");
    }
}
