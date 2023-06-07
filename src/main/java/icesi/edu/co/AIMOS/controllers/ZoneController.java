package icesi.edu.co.AIMOS.controllers;


import icesi.edu.co.AIMOS.entities.User;
import icesi.edu.co.AIMOS.entities.Zone;
import icesi.edu.co.AIMOS.repositories.UserRepository;
import icesi.edu.co.AIMOS.repositories.ZoneRepository;
import icesi.edu.co.AIMOS.requests.ZoneRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class ZoneController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ZoneRepository zoneRepository;

    @GetMapping(value = "zones/all")
    public ResponseEntity<?> getAllZones(@RequestHeader String identification) {
        Optional<User> oMaster = userRepository.findById(identification);

        if (oMaster.isPresent()) {
            User masterInRepository = oMaster.get();
            if (masterInRepository.getAuthorization().getType().equals("MASTER")) {
                return ResponseEntity.status(200).body(zoneRepository.findAll());
            }
            return ResponseEntity.status(401).body("Not authorized to access all zones.");
        }
        return ResponseEntity.status(400).body("Master user not found.");
    }

    @PostMapping(value = "zones/add", consumes = "application/json")
    public ResponseEntity<?> addZone(@RequestBody ZoneRequest zoneRequest) {
        if(zoneRequest.getUser().getIdentification()!=null && zoneRequest.getZone()!=null){
            User master = zoneRequest.getUser();
            Zone zone = zoneRequest.getZone();

            Optional<User> oMaster = userRepository.findById(master.getIdentification());

            if (oMaster.isPresent()) {
                User masterInRepository = oMaster.get();
                if (masterInRepository.getAuthorization().getType().equals("MASTER")) {
                    if (!zoneRepository.existsByName(zone.getName())) {
                        zoneRepository.save(zone);
                        return ResponseEntity.status(200).body("Zone successfully added");
                    }
                    return ResponseEntity.status(400).body("Zone with the same name already exists.");
                }
                return ResponseEntity.status(400).body("Not authorized");
            }
            return ResponseEntity.status(400).body("User doesn't exist.");
        }
        return ResponseEntity.status(400).body("Incomplete data.");
    }

    @DeleteMapping(value = "zones/delete")
    public ResponseEntity<?> deleteZone(@RequestBody ZoneRequest zoneRequest) {

        Optional<User> oMaster = userRepository.findById(zoneRequest.getUser().getIdentification());
        Optional<Zone> oZone = zoneRepository.findById(zoneRequest.getZone().getId());

        if (oZone.isPresent()) {
            Zone zone = oZone.get();

            if(oMaster.isPresent()){
                User master = oMaster.get();
                if (master.getAuthorization().getType().equals("MASTER")) {
                    zone.getUsers().forEach(user -> user.getZones().remove(zone));
                    userRepository.saveAll(zone.getUsers());
                    zoneRepository.delete(zone);
                    return ResponseEntity.status(200).body("Zone successfully deleted and unlinked from users.");
                }
                return ResponseEntity.status(401).body("Not authorized to delete the zone.");
            }
            return ResponseEntity.status(401).body("Not authorized to delete the zone.");
        }

        return ResponseEntity.status(400).body("Zone not found.");
    }

    @GetMapping(value = "zones/users")
    public ResponseEntity<?> getUsersByZone(@RequestHeader Long id) {
        Optional<Zone> oZone = zoneRepository.findById(id);


        if (oZone.isPresent()) {
            Zone zoneInRepository = oZone.get();
            List<User> users = zoneInRepository.getUsers();
            return ResponseEntity.status(200).body(users);
        }

        return ResponseEntity.status(404).body("Zone not found.");
    }

    @GetMapping(value = "zones/plants")
    public ResponseEntity<?> getPlantsByZone(@RequestHeader Long zoneId, @RequestHeader String identification) {
        Optional<Zone> oZone = zoneRepository.findById(zoneId);
        Optional<User> oUser = userRepository.findById(identification);

        if(oZone.isPresent() && oUser.isPresent()){
            User userInRepository = oUser.get();
            Zone zoneInRepository = oZone.get();

            if(userInRepository.getAuthorization().getType().equals("USER")){
                if(userInRepository.getZones().contains(zoneInRepository)){
                    return ResponseEntity.status(200).body(zoneInRepository.getPlants());
                }
                return ResponseEntity.status(401).body("User is not associated with the zone");
            }
            return ResponseEntity.status(401).body("Not a valid user.");
        }
        return ResponseEntity.status(404).body("Zone or User not found.");
    }

    @GetMapping("zones/notLinked")
    public ResponseEntity<?> getUsersWithoutAZone(@RequestHeader String identification, @RequestHeader Long zoneId) {
        Optional<User> oUser = userRepository.findById(identification);
        Optional<Zone> oZone = zoneRepository.findById(zoneId);

        if (oUser.isPresent() && oZone.isPresent()) {
            User userInRepository = oUser.get();
            Zone zoneInRepository = oZone.get();
            if(userInRepository.getAuthorization().getType().equals("MASTER")){
                return ResponseEntity.status(201).body(zoneRepository.findZonesNotLinkedToUser(identification));
            }
            return ResponseEntity.status(401).body("Master user cannot have zones.");
        }
        return ResponseEntity.status(404).body("User or zone not found.");
    }

}
