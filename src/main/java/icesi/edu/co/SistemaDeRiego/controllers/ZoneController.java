package icesi.edu.co.SistemaDeRiego.controllers;

import icesi.edu.co.SistemaDeRiego.entities.User;
import icesi.edu.co.SistemaDeRiego.entities.Zone;
import icesi.edu.co.SistemaDeRiego.repositories.UserRepository;
import icesi.edu.co.SistemaDeRiego.repositories.ZoneRepository;
import icesi.edu.co.SistemaDeRiego.requests.ZoneRequest;
import icesi.edu.co.SistemaDeRiego.requests.LinkUserZoneRequest;
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

    @PostMapping(value = "zones/add", consumes = "application/json")
    public ResponseEntity<?> addZone(@RequestBody ZoneRequest zoneRequest) {
        User master = zoneRequest.getMaster();
        Zone zone = zoneRequest.getZone();

        Optional<User> oMaster = userRepository.findById(master.getIdentification());

        if (oMaster.isPresent()) {
            User masterInRepository = oMaster.get();
            if (masterInRepository.getAuthorization().getType().equals("MASTER")) {
                if (zoneRepository.existsByName(zone.getName())) {
                    return ResponseEntity.status(400).body("Zone with the same name already exists.");
                }
                zoneRepository.save(zone);
                return ResponseEntity.status(200).body("Zone successfully added");
            }
            return ResponseEntity.status(400).body("Not authorized");
        }
        return ResponseEntity.status(400).body("User doesn't exist.");
    }


    @PostMapping(value = "zones/link", consumes = "application/json")
    public ResponseEntity<?> linkUserZone(@RequestBody LinkUserZoneRequest linkUserZoneRequest) {
        User master = linkUserZoneRequest.getMaster();
        User user = linkUserZoneRequest.getUser();
        Zone zone = linkUserZoneRequest.getZone();

        Optional<User> oMaster = userRepository.findById(master.getIdentification());
        Optional<User> oUser = userRepository.findById(user.getIdentification());
        Optional<Zone> oZone = zoneRepository.findById(zone.getId());

        if (oMaster.isPresent() && oUser.isPresent() && oZone.isPresent()) {
            User masterInRepository = oMaster.get();
            if (masterInRepository.getAuthorization().getType().equals("MASTER")) {
                User userInRepository = oUser.get();
                Zone zoneInRepository = oZone.get();

                List<Zone> userZones = userInRepository.getZones();
                userZones.add(zoneInRepository);
                userInRepository.setZones(userZones);

                List<User> zoneUsers = zoneInRepository.getUsers();
                zoneUsers.add(userInRepository);
                zoneInRepository.setUsers(zoneUsers);

                userRepository.save(userInRepository);
                zoneRepository.save(zoneInRepository);

                return ResponseEntity.status(200).body("Zone linked to user successfully.");
            }
            return ResponseEntity.status(400).body("Not authorized.");
        }
        return ResponseEntity.status(400).body("Invalid user or zone.");
    }

    @DeleteMapping(value = "zones/delete")
    public ResponseEntity<?> deleteZone(@RequestBody ZoneRequest zoneRequest) {

        Optional<User> oMaster = userRepository.findById(zoneRequest.getMaster().getIdentification());
        Optional<Zone> oZone = zoneRepository.findById(zoneRequest.getZone().getId());

        if (oZone.isPresent()) {
            Zone zone = oZone.get();

            if(oMaster.isPresent()){
                User master = oMaster.get();
                if (master.getAuthorization().getType().equals("MASTER")) {
                    zoneRepository.delete(zone);
                    return ResponseEntity.status(200).body("Zone successfully deleted and unlinked from users.");
                }
                return ResponseEntity.status(401).body("Not authorized to delete the zone.");
            }
            return ResponseEntity.status(401).body("Not authorized to delete the zone.");
        }

        return ResponseEntity.status(400).body("Zone not found.");
    }


}
