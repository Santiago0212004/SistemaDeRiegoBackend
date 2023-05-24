package icesi.edu.co.AIMOS.controllers;

import icesi.edu.co.AIMOS.entities.Authorization;
import icesi.edu.co.AIMOS.entities.User;
import icesi.edu.co.AIMOS.repositories.AuthorizationRepository;
import icesi.edu.co.AIMOS.repositories.UserRepository;
import icesi.edu.co.AIMOS.request.AuthorizationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class AuthorizationController {

    @Autowired
    AuthorizationRepository authorizationRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "authorizations/all")
    public ResponseEntity<?> getAllAuthorizations(@RequestHeader String identification) {
        Optional<User> oMaster = userRepository.findById(identification);

        if (oMaster.isPresent()) {
            User masterInRepository = oMaster.get();
            if (masterInRepository.getAuthorization().getType().equals("MASTER")) {
                return ResponseEntity.status(200).body(authorizationRepository.findAll());
            }
            return ResponseEntity.status(400).body("Not authorized");
        }
        return ResponseEntity.status(400).body("Not authorized");
    }


    @PostMapping(value = "authorizations/add", consumes = "application/json")
    public ResponseEntity<?> addAuthorization(@RequestBody AuthorizationRequest authorizationRequest) {
        User master = authorizationRequest.getMaster();
        Optional<User> oMaster = userRepository.findById(master.getIdentification());

        Authorization authorization = authorizationRequest.getAuthorization();

        if (oMaster.isPresent()) {
            User masterInRepository = oMaster.get();
            if (masterInRepository.getAuthorization().getType().equals("MASTER")) {
                if (authorization.getType().equals("USER")) {
                    if (authorizationRepository.findById(authorization.getValue()).isPresent()) {
                        return ResponseEntity.status(400).body("Authorization with the same value already exists.");
                    }
                    Authorization addedAuthorization = authorizationRepository.save(authorization);
                    return ResponseEntity.status(200).body("Authorization successfully added.");
                }
                return ResponseEntity.status(400).body("Only authorizations of type 'USER' can be added.");
            }
            return ResponseEntity.status(401).body("Not authorized");
        }
        return ResponseEntity.status(401).body("Not authorized");
    }

    @DeleteMapping(value = "authorizations/delete", consumes = "application/json")
    public ResponseEntity<?> deleteAuthorization(@RequestBody AuthorizationRequest authorizationRequest) {
        User master = authorizationRequest.getMaster();
        Authorization authorization = authorizationRequest.getAuthorization();
        Optional<User> oMaster = userRepository.findById(master.getIdentification());
        Optional<Authorization> oAuthorization = authorizationRepository.findById(authorization.getValue());

        if (oMaster.isPresent()) {
            User masterUser = oMaster.get();
            if (masterUser.getAuthorization().getType().equals("MASTER")) {
                if (oAuthorization.isPresent()) {
                    Authorization authorizationInRepository = oAuthorization.get();
                    if (authorizationInRepository.getType().equals("USER")) {
                        List<User> users = authorizationInRepository.getUsers();

                        for (User user : users) {
                            user.getZones().forEach(zone -> zone.getUsers().remove(user));
                            userRepository.save(user);
                            userRepository.delete(user);
                        }

                        authorizationRepository.delete(authorizationInRepository);

                        return ResponseEntity.status(200).body("Authorization and associated users successfully deleted.");
                    }
                    return ResponseEntity.status(401).body("Only authorizations of type 'USER' can be deleted.");
                }
                return ResponseEntity.status(404).body("Authorization not found.");
            }
            return ResponseEntity.status(401).body("Not authorized");
        }
        return ResponseEntity.status(401).body("Not authorized");
    }


}
