package icesi.edu.co.AIMOS.controllers;

import icesi.edu.co.AIMOS.entities.SensorType;
import icesi.edu.co.AIMOS.entities.User;
import icesi.edu.co.AIMOS.repositories.SensorTypeRepository;
import icesi.edu.co.AIMOS.repositories.UserRepository;
import icesi.edu.co.AIMOS.requests.SensorTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class SensorTypeController {
    @Autowired
    SensorTypeRepository sensorTypeRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "types/sensors/add", consumes = "application/json")
    public ResponseEntity<?> addSensorType(@RequestBody SensorTypeRequest sensorTypeRequest){
        SensorType sensorType = sensorTypeRequest.getSensorType();
        Optional<User> oMaster = userRepository.findById(sensorTypeRequest.getUser().getIdentification());

        if(oMaster.isPresent()){
            User masterInRepository = oMaster.get();
            if(masterInRepository.getAuthorization().getType().equals("MASTER")){
                sensorTypeRepository.save(sensorType);
                return ResponseEntity.status(202).body("Actuator type added successfully.");
            }
            return ResponseEntity.status(401).body("Not authorized");
        }
        return ResponseEntity.status(404).body("Master user not found.");
    }


    @DeleteMapping(value = "types/sensors/delete", consumes = "application/json")
    public ResponseEntity<?> deleteSensorType(@RequestBody SensorTypeRequest sensorTypeRequest){
        Optional<SensorType> oSensorType = sensorTypeRepository.findById(sensorTypeRequest.getSensorType().getId());
        Optional<User> oMaster = userRepository.findById(sensorTypeRequest.getUser().getIdentification());

        if(oMaster.isPresent() && oSensorType.isPresent()){
            User masterInRepository = oMaster.get();
            SensorType sensorTypeInRepository = oSensorType.get();
            if(masterInRepository.getAuthorization().getType().equals("MASTER")){
                sensorTypeRepository.delete(sensorTypeInRepository);
                return ResponseEntity.status(200).body("Actuator type deleted successfully.");
            }
            return ResponseEntity.status(401).body("Not authorized");
        }
        return ResponseEntity.status(404).body("Master user not found.");
    }

    @GetMapping(value = "types/sensors/all")
    public ResponseEntity<?> getAllSensorTypes(@RequestHeader String identification) {
        Optional<User> oUser = userRepository.findById(identification);

        if (oUser.isPresent()) {
            User userInRepository = oUser.get();
            if (userInRepository.getAuthorization().getType().equals("MASTER") || userInRepository.getAuthorization().getType().equals("USER")) {
                return ResponseEntity.status(201).body(sensorTypeRepository.findAll());
            }
            return ResponseEntity.status(401).body("Not authorized.");
        }
        return ResponseEntity.status(400).body("User not found.");
    }
}
