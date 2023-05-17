package icesi.edu.co.SistemaDeRiego.controllers;

import icesi.edu.co.SistemaDeRiego.requests.LoginRequest;
import icesi.edu.co.SistemaDeRiego.entities.Authorization;
import icesi.edu.co.SistemaDeRiego.entities.User;
import icesi.edu.co.SistemaDeRiego.repositories.AuthorizationRepository;
import icesi.edu.co.SistemaDeRiego.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorizationRepository authorizationRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping(value = "users/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        String authorization = user.getAuthorization().getValue();

        Optional<Authorization> oAuthInRepository = authorizationRepository.findById(authorization);

        if(oAuthInRepository.isPresent()){
            Authorization authInRepository = oAuthInRepository.get();
            if(authInRepository.getType().equals("USER") || authInRepository.getType().equals("MASTER")){

                boolean existsByUsername = userRepository.existsByUsername(user.getUsername());
                boolean existsByIdentification = userRepository.existsByIdentification(user.getIdentification());

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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String identification = loginRequest.getIdentification();
        String password = loginRequest.getPassword();

        Optional<User> oUser = userRepository.findById(identification);

        if (oUser.isPresent()) {
            User user = oUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.status(200).body(user);
            }
            return ResponseEntity.status(401).body("Invalid identification or password.");
        }

        return ResponseEntity.status(401).body("Invalid identification or password.");
    }
}
