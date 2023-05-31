package icesi.edu.co.AIMOS.controllers;


import icesi.edu.co.AIMOS.entities.*;
import icesi.edu.co.AIMOS.repositories.*;
import icesi.edu.co.AIMOS.request.PlantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        Optional<Zone> oZone = zoneRepository.findById(plantRequest.getZone().getId());
        Optional<Plant> oPlant = plantRepository.findById(plantRequest.getPlant().getId());

        if (oUser.isPresent() && oZone.isPresent() && oPlant.isPresent()) {
            User userInRepository = oUser.get();
            Zone zoneInRepository = oZone.get();
            Plant plantInRepository = oPlant.get();

            if (userInRepository.getAuthorization().getType().equals("USER")) {
                List<Zone> userZones = userInRepository.getZones();

                if (userZones.contains(zoneInRepository)) {
                    List<Plant> zonePlants = zoneInRepository.getPlants();

                    if (zonePlants.contains(plantInRepository)) {
                        zonePlants.remove(plantInRepository);
                        zoneInRepository.setPlants(zonePlants);
                        plantInRepository.setZone(null);
                        zoneRepository.save(zoneInRepository);

                        List<Sensor> plantSensors = plantInRepository.getSensors();
                        plantInRepository.setSensors(null);
                        sensorRepository.deleteAll(plantSensors);


                        List<Actuator> plantActuators = plantInRepository.getActuators();
                        plantInRepository.setActuators(null);
                        actuatorRepository.deleteAll(plantActuators);

                        plantRepository.delete(plantInRepository);

                        return ResponseEntity.status(200).body("Plant successfully deleted.");
                    }
                    return ResponseEntity.status(400).body("Plant not associated with the zone.");
                }
                return ResponseEntity.status(400).body("User not associated with the zone.");
            }
            return ResponseEntity.status(401).body("Only users can delete plants.");
        }
        return ResponseEntity.status(404).body("User, zone, or plant not found.");
    }

}
