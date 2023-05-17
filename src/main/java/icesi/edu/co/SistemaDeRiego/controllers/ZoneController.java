package icesi.edu.co.SistemaDeRiego.controllers;

import icesi.edu.co.SistemaDeRiego.entities.User;
import icesi.edu.co.SistemaDeRiego.entities.Zone;
import icesi.edu.co.SistemaDeRiego.repositories.UserRepository;
import icesi.edu.co.SistemaDeRiego.repositories.ZoneRepository;
import icesi.edu.co.SistemaDeRiego.requests.AddZoneRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ZoneController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ZoneRepository zoneRepository;

    @PostMapping(value = "zones/add", consumes = "application/json")
    public ResponseEntity<?> addZone(@RequestBody AddZoneRequest addZoneRequest) {
        User user = addZoneRequest.getUser();
        Zone zone = addZoneRequest.getZone();

        Optional<User> oUser = userRepository.findById(user.getIdentification());

        if(oUser.isPresent()){
            User userInRepository = oUser.get();
            if(userInRepository.getAuthorization().getType().equals("MASTER")){
                zoneRepository.save(zone);
                return ResponseEntity.status(200).body("Zone successfully added");
            }
            return ResponseEntity.status(400).body("Not authorized");
        }
        return ResponseEntity.status(400).body("User doesn't exist.");
    }
}
