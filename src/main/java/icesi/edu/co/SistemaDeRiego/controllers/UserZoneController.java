package icesi.edu.co.SistemaDeRiego.controllers;

import icesi.edu.co.SistemaDeRiego.entities.User;
import icesi.edu.co.SistemaDeRiego.entities.Zone;
import icesi.edu.co.SistemaDeRiego.repositories.UserRepository;
import icesi.edu.co.SistemaDeRiego.repositories.ZoneRepository;
import icesi.edu.co.SistemaDeRiego.requests.LinkUserZoneRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class UserZoneController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ZoneRepository zoneRepository;


    @PostMapping(value = "zones/link", consumes = "application/json")
    public ResponseEntity<?> linkUserToZone(@RequestBody LinkUserZoneRequest linkRequest) {
        User master = linkRequest.getMaster();
        User user = linkRequest.getUser();
        Zone zone = linkRequest.getZone();

        Optional<User> oMaster = userRepository.findById(master.getIdentification());

        if (oMaster.isPresent()) {
            User masterInRepository = oMaster.get();
            if (masterInRepository.getAuthorization().getType().equals("MASTER")) {
                Optional<User> oUser = userRepository.findById(user.getIdentification());
                Optional<Zone> oZone = zoneRepository.findById(zone.getId());

                if (oUser.isPresent() && oZone.isPresent()) {
                    User userInRepository = oUser.get();
                    Zone zoneInRepository = oZone.get();

                    if (!userInRepository.getZones().contains(zoneInRepository)) {

                        if(userInRepository.getAuthorization().getType().equals("USER")){
                            userInRepository.getZones().add(zoneInRepository);
                            zoneInRepository.getUsers().add(userInRepository);
                            userRepository.save(userInRepository);
                            zoneRepository.save(zoneInRepository);
                            return ResponseEntity.status(200).body("User successfully linked to the zone.");
                        }
                        return ResponseEntity.status(400).body("Master user cannot have zones.");
                    }
                    return ResponseEntity.status(400).body("User is already linked to the zone.");
                }
                return ResponseEntity.status(400).body("User or Zone not found.");
            }
            return ResponseEntity.status(400).body("Not authorized.");
        }
        return ResponseEntity.status(400).body("Master user not found.");
    }


    @DeleteMapping(value = "zones/unlink", consumes = "application/json")
    public ResponseEntity<?> unlinkUserFromZone(@RequestBody LinkUserZoneRequest linkRequest) {
        User master = linkRequest.getMaster();
        User user = linkRequest.getUser();
        Zone zone = linkRequest.getZone();

        Optional<User> oMaster = userRepository.findById(master.getIdentification());

        if (oMaster.isPresent()) {
            User masterInRepository = oMaster.get();
            if (masterInRepository.getAuthorization().getType().equals("MASTER")) {
                Optional<User> oUser = userRepository.findById(user.getIdentification());
                Optional<Zone> oZone = zoneRepository.findById(zone.getId());

                if (oUser.isPresent() && oZone.isPresent()) {
                    User userInRepository = oUser.get();
                    Zone zoneInRepository = oZone.get();

                    if (userInRepository.getZones().contains(zoneInRepository)) {
                        userInRepository.getZones().remove(zoneInRepository);
                        zoneInRepository.getUsers().remove(userInRepository);
                        userRepository.save(userInRepository);
                        zoneRepository.save(zoneInRepository);
                        return ResponseEntity.status(200).body("User successfully unlinked from the zone.");
                    }
                    return ResponseEntity.status(400).body("User is not linked to the zone.");
                }
                return ResponseEntity.status(400).body("User or Zone not found.");
            }
            return ResponseEntity.status(400).body("Not authorized.");
        }
        return ResponseEntity.status(400).body("Master user not found.");
    }
}
