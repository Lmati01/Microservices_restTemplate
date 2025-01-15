package lmati.java.carservice.controller;

import lmati.java.carservice.entity.Car;
import lmati.java.carservice.model.CarResponse;
import lmati.java.carservice.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/car")
public class CarController {
    @Autowired
    private CarService carService;

    private static final Logger log = LoggerFactory.getLogger(CarController.class);



    // Original endpoints with RestTemplate (default)
    @GetMapping
    public ResponseEntity<List<CarResponse>> getAllCars() {
        return ResponseEntity.ok(carService.findAllWithRestTemplate());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(carService.findByIdWithRestTemplate(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/rest-template/all")
    public ResponseEntity<List<CarResponse>> getAllCarsRestTemplate() {
        return ResponseEntity.ok(carService.findAllWithRestTemplate());
    }





    @GetMapping("/rest-template/{id}")
    public ResponseEntity<CarResponse> findByIdRestTemplate(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(carService.findByIdWithRestTemplate(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }





    // Standard CRUD operations (unchanged)
    @PostMapping
    public ResponseEntity<Car> save(@RequestBody Car car) {
        try {
            return ResponseEntity.ok(carService.save(car));
        } catch (Exception e) {
            log.error("Error saving car: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> update(@PathVariable Long id, @RequestBody Car car) {
        try {
            car.setId(id);
            return ResponseEntity.ok(carService.save(car));
        } catch (Exception e) {
            log.error("Error updating car: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            carService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting car: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // Client-specific endpoints with different implementations
    @GetMapping("/client/rest-template/{clientId}")
    public CarResponse findByClientIdRestTemplate(@PathVariable Long clientId) {
        return carService.findByIdWithRestTemplate(clientId);
    }

}
