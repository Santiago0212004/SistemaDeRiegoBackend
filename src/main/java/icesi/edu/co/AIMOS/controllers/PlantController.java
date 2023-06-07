package icesi.edu.co.AIMOS.controllers;


import icesi.edu.co.AIMOS.entities.*;
import icesi.edu.co.AIMOS.repositories.*;
import icesi.edu.co.AIMOS.requests.PlantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class PlantController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ZoneRepository zoneRepository;

    @Autowired
    PlantRepository plantRepository;

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    ActuatorRepository actuatorRepository;

    @PostMapping(value = "plants/add", consumes = "application/json")
    public ResponseEntity<?> addPlantToZone(@RequestBody PlantRequest plantRequest) {
        User user = plantRequest.getUser();
        Zone zone = plantRequest.getZone();
        Plant plant = plantRequest.getPlant();
        plant.setHumidityLimit((double) 0);

        Optional<User> oUser = userRepository.findById(user.getIdentification());
        Optional<Zone> oZone = zoneRepository.findById(zone.getId());

        if (oUser.isPresent() && oZone.isPresent()) {
            User userInRepository = oUser.get();
            Zone zoneInRepository = oZone.get();

            if (userInRepository.getAuthorization().getType().equals("USER")) {

                if (userInRepository.getZones().contains(zoneInRepository)) {
                    plant.setZone(zoneInRepository);
                    plantRepository.save(plant);
                    return ResponseEntity.status(200).body("Plant successfully added to the zone.");
                }

                return ResponseEntity.status(401).body("User is not associated with the zone.");
            }

            return ResponseEntity.status(401).body("User is not of type 'USER'.");
        }

        return ResponseEntity.status(404).body("User or Zone not found.");
    }


    @DeleteMapping(value = "plants/delete", consumes = "application/json")
    public ResponseEntity<?> delete(@RequestBody PlantRequest plantRequest) {
        Optional<User> oUser = userRepository.findById(plantRequest.getUser().getIdentification());
        Optional<Plant> oPlant = plantRepository.findById(plantRequest.getPlant().getId());

        if (oUser.isPresent() &&  oPlant.isPresent()) {
            User userInRepository = oUser.get();
            Plant plantInRepository = oPlant.get();

            if (userInRepository.getAuthorization().getType().equals("USER")) {
                if (userInRepository.getZones().contains(plantInRepository.getZone())) {
                    plantRepository.delete(plantInRepository);
                    return ResponseEntity.status(200).body("Plant successfully deleted.");
                }
                return ResponseEntity.status(400).body("User not associated with the zone.");
            }
            return ResponseEntity.status(401).body("Only users can delete plants.");
        }
        return ResponseEntity.status(404).body("User, zone, or plant not found.");
    }


    @PutMapping(value = "plants/limit", consumes = "application/json")
    public ResponseEntity<?> changePlantHumidityLimit(@RequestBody PlantRequest plantRequest, @RequestHeader Double humidityLimit){
        Optional<User> oUser = userRepository.findById(plantRequest.getUser().getIdentification());
        Optional<Plant> oPlant = plantRepository.findById(plantRequest.getPlant().getId());

        if (oUser.isPresent() &&  oPlant.isPresent()) {
            User userInRepository = oUser.get();
            Plant plantInRepository = oPlant.get();
            if (userInRepository.getAuthorization().getType().equals("USER")) {
                if (userInRepository.getZones().contains(plantInRepository.getZone())) {
                    plantInRepository.setHumidityLimit(humidityLimit);
                    plantRepository.save(plantInRepository);
                    return ResponseEntity.status(200).body("Plant humidity limit successfully changed.");
                }
                return ResponseEntity.status(400).body("User not associated with the zone.");
            }
            return ResponseEntity.status(401).body("Only users can delete plants.");
        }
        return ResponseEntity.status(404).body("User, zone, or plant not found.");
    }

    @GetMapping(value = "plants/actuators")
    public ResponseEntity<?> getActuatorsByPlant(@RequestHeader Long plantId, @RequestHeader String identification) {
        Optional<Plant> oPlant = plantRepository.findById(plantId);
        Optional<User> oUser = userRepository.findById(identification);

        if(oPlant.isPresent() && oUser.isPresent()){
            User userInRepository = oUser.get();
            Plant plantInRepository = oPlant.get();

            if(userInRepository.getAuthorization().getType().equals("USER")){
                if(userInRepository.getZones().contains(plantInRepository.getZone())){
                    return ResponseEntity.status(200).body(plantInRepository.getActuators());
                }
                return ResponseEntity.status(401).body("User is not associated with the plant's zone.");
            }
            return ResponseEntity.status(401).body("Not a valid user.");
        }
        return ResponseEntity.status(404).body("Zone or User not found.");
    }

    @GetMapping(value = "plants/sensors")
    public ResponseEntity<?> getSensorsByPlant(@RequestHeader Long plantId, @RequestHeader String identification) {
        Optional<Plant> oPlant = plantRepository.findById(plantId);
        Optional<User> oUser = userRepository.findById(identification);

        if(oPlant.isPresent() && oUser.isPresent()){
            User userInRepository = oUser.get();
            Plant plantInRepository = oPlant.get();

            if(userInRepository.getAuthorization().getType().equals("USER")){
                if(userInRepository.getZones().contains(plantInRepository.getZone())){
                    return ResponseEntity.status(200).body(plantInRepository.getSensors());
                }
                return ResponseEntity.status(401).body("User is not associated with the plant's zone.");
            }
            return ResponseEntity.status(401).body("Not a valid user.");
        }
        return ResponseEntity.status(404).body("Zone or User not found.");
    }

}
