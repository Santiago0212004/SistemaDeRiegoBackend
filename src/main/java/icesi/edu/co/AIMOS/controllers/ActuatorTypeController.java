package icesi.edu.co.AIMOS.controllers;


import icesi.edu.co.AIMOS.entities.ActuatorType;
import icesi.edu.co.AIMOS.entities.User;
import icesi.edu.co.AIMOS.repositories.ActuatorTypeRepository;
import icesi.edu.co.AIMOS.repositories.UserRepository;
import icesi.edu.co.AIMOS.requests.ActuatorTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class ActuatorTypeController {

    @Autowired
    ActuatorTypeRepository actuatorTypeRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "types/actuators/add", consumes = "application/json")
    public ResponseEntity<?> addActuatorType(@RequestBody ActuatorTypeRequest actuatorTypeRequest){
        ActuatorType actuatorType = actuatorTypeRequest.getActuatorType();
        Optional<User> oMaster = userRepository.findById(actuatorTypeRequest.getUser().getIdentification());

        if(oMaster.isPresent()){
            User masterInRepository = oMaster.get();
            if(masterInRepository.getAuthorization().getType().equals("MASTER")){
                actuatorTypeRepository.save(actuatorType);
                return ResponseEntity.status(201).body("Actuator type added successfully.");
            }
            return ResponseEntity.status(401).body("Not authorized");
        }
        return ResponseEntity.status(404).body("Master user not found.");
    }


    @DeleteMapping(value = "types/actuators/delete", consumes = "application/json")
    public ResponseEntity<?> deleteActuatorType(@RequestBody ActuatorTypeRequest actuatorTypeRequest){
        Optional<ActuatorType> oActuatorType = actuatorTypeRepository.findById(actuatorTypeRequest.getActuatorType().getId());
        Optional<User> oMaster = userRepository.findById(actuatorTypeRequest.getUser().getIdentification());

        if(oMaster.isPresent() && oActuatorType.isPresent()){
            User masterInRepository = oMaster.get();
            ActuatorType actuatorTypeInRepository = oActuatorType.get();
            if(masterInRepository.getAuthorization().getType().equals("MASTER")){
                actuatorTypeRepository.delete(actuatorTypeInRepository);
                return ResponseEntity.status(200).body("Actuator type deleted successfully.");
            }
            return ResponseEntity.status(401).body("Not authorized");
        }
        return ResponseEntity.status(404).body("Master user not found.");
    }

    @GetMapping(value = "types/actuators/all")
    public ResponseEntity<?> getAllActuatorTypes(@RequestHeader String identification) {
        Optional<User> oUser = userRepository.findById(identification);

        if (oUser.isPresent()) {
            User userInRepository = oUser.get();
            if (userInRepository.getAuthorization().getType().equals("MASTER") || userInRepository.getAuthorization().getType().equals("USER")) {
                return ResponseEntity.status(200).body(actuatorTypeRepository.findAll());
            }
            return ResponseEntity.status(401).body("Not authorized.");
        }
        return ResponseEntity.status(400).body("User not found.");
    }
}
