package icesi.edu.co.AIMOS.controllers;


import icesi.edu.co.AIMOS.entities.Authorization;
import icesi.edu.co.AIMOS.entities.User;
import icesi.edu.co.AIMOS.entities.Zone;
import icesi.edu.co.AIMOS.repositories.AuthorizationRepository;
import icesi.edu.co.AIMOS.repositories.UserRepository;
import icesi.edu.co.AIMOS.repositories.ZoneRepository;
import icesi.edu.co.AIMOS.requests.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorizationRepository authorizationRepository;

    @Autowired
    ZoneRepository zoneRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping(value = "users/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader String identification) {
        Optional<User> oMaster = userRepository.findById(identification);

        if (oMaster.isPresent()) {
            User masterInRepository = oMaster.get();
            if (masterInRepository.getAuthorization().getType().equals("MASTER")) {
                return ResponseEntity.status(200).body(userRepository.findAllNonMasterUsers());
            }
            return ResponseEntity.status(401).body("Not authorized to access all users.");
        }
        return ResponseEntity.status(400).body("Master user not found.");
    }

    @PostMapping(value = "users/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        String authorization = user.getAuthorization().getValue();

        Optional<Authorization> oAuthInRepository = authorizationRepository.findById(authorization);

        if(oAuthInRepository.isPresent()){
            Authorization authInRepository = oAuthInRepository.get();
            if(authInRepository.getType().equals("USER") || authInRepository.getType().equals("MASTER")){

                boolean existsByUsername = userRepository.existsByUsername(user.getUsername());
                boolean existsByIdentification = userRepository.existsByIdentification(user.getIdentification());
                boolean usedAuthorization = userRepository.existsByAuthorizationValue(authInRepository.getValue());

                if(usedAuthorization){
                    return ResponseEntity.status(400).body("Access key already used.");
                }

                if (existsByUsername || existsByIdentification) {
                    return ResponseEntity.status(400).body("Username or identification already exists.");
                }

                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);

                userRepository.save(user);
                return ResponseEntity.status(200).body("User successfully registered.");
            }
            return ResponseEntity.status(400).body("Not authorized.");
        }
        return ResponseEntity.status(400).body("Not authorized.");
    }

    @PostMapping(value = "users/login", consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        String identification = loginUser.getIdentification();
        String password = loginUser.getPassword();

        Optional<User> oUser = userRepository.findById(identification);

        if (oUser.isPresent()) {
            User foundUser = oUser.get();
            if (passwordEncoder.matches(password, foundUser.getPassword())) {
                if(foundUser.getAuthorization() != null){
                    return ResponseEntity.status(200).body(foundUser);
                }
                return ResponseEntity.status(401).body("Your access key expired");
            }
            return ResponseEntity.status(401).body("Invalid identification or password.");
        }

        return ResponseEntity.status(401).body("Invalid identification or password.");
    }

    @DeleteMapping(value = "users/delete", consumes = "application/json")
    public ResponseEntity<?> delete(@RequestBody UserRequest userRequest) {
        Optional<User> oMaster = userRepository.findById(userRequest.getMaster().getIdentification());
        Optional<User> oUser = userRepository.findById(userRequest.getUser().getIdentification());

        if (oMaster.isPresent()) {
            User master = oMaster.get();
            if(master.getAuthorization().getType().equals("MASTER")){
                if(oUser.isPresent()){
                    User user = oUser.get();
                    if(user.getAuthorization().getType().equals("USER")){
                        user.getZones().forEach(zone -> zone.getUsers().remove(user));
                        zoneRepository.saveAll(user.getZones());
                        userRepository.deleteById(user.getIdentification());
                        return ResponseEntity.status(200).body("User successfully deleted.");
                    }
                    return ResponseEntity.status(401).body("Master user cannot be deleted.");
                }
                return ResponseEntity.status(401).body("Deleting user doesn't exist");
            }
            return ResponseEntity.status(401).body("Not authorized");
        }
        return ResponseEntity.status(401).body("Not authorized");
    }

    @GetMapping("users/zones")
    public ResponseEntity<?> getZonesByUser(@RequestHeader String identification) {
        Optional<User> oUser = userRepository.findById(identification);

        if (oUser.isPresent()) {
            User userInRepository = oUser.get();
            if(userInRepository.getAuthorization().getType().equals("USER") || userInRepository.getAuthorization().getType().equals("MASTER")){
                List<Zone> zones = userInRepository.getZones();
                return ResponseEntity.status(200).body(zones);
            }
            return ResponseEntity.status(401).body("Not authorized.");
        }
        return ResponseEntity.status(404).body("User not found.");
    }


    @GetMapping("users/notLinked")
    public ResponseEntity<?> getZonesWithoutAUser(@RequestHeader String identification, @RequestHeader Long zoneId) {
        Optional<User> oUser = userRepository.findById(identification);
        Optional<Zone> oZone = zoneRepository.findById(zoneId);

        if (oUser.isPresent() && oZone.isPresent()) {
            User userInRepository = oUser.get();
            if(userInRepository.getAuthorization().getType().equals("MASTER")){
                return ResponseEntity.status(200).body(userRepository.findUsersNotLinkedToZone(zoneId));
            }
            return ResponseEntity.status(401).body("Not authorized.");
        }
        return ResponseEntity.status(404).body("User or zone not found.");
    }
}
