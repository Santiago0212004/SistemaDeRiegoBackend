package icesi.edu.co.AIMOS.controllers;

import icesi.edu.co.AIMOS.entities.Measure;
import icesi.edu.co.AIMOS.repositories.MeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@CrossOrigin(maxAge = 3600)
public class MeasureController {
    @Autowired
    MeasureRepository measureRepository;

    @PostMapping(value = "measures/add",consumes = "application/json")
    public ResponseEntity<?> addMeasure(@RequestBody Measure measure){
        measure.setDate(new Date());
        measureRepository.save(measure);
        return ResponseEntity.status(200).body("Received");
    }
}
