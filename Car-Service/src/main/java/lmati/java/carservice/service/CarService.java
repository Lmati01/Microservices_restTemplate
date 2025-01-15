package lmati.java.carservice.service;

import lmati.java.carservice.entity.Car;
import lmati.java.carservice.entity.Client;
import lmati.java.carservice.model.CarResponse;
import lmati.java.carservice.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String CLIENT_SERVICE_URL = "http://localhost:8888/api/client/";

    // RestTemplate implementation
    public List<CarResponse> findAllWithRestTemplate() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(car -> {
                    Client client = fetchClientWithRestTemplate(car.getClientId());
                    return buildCarResponse(car, client);
                })
                .collect(Collectors.toList());
    }

    // WebClient implementation




    private Client fetchClientWithRestTemplate(Long clientId) {
        if (clientId == null) return null;
        try {
            ResponseEntity<Client> response = restTemplate.getForEntity(
                    CLIENT_SERVICE_URL + clientId,
                    Client.class
            );
            return response.getBody();
        } catch (Exception e) {
            System.out.println("Error fetching client with RestTemplate for ID {}: {}"+ clientId + e.getMessage());
            return null;
        }
    }





    private CarResponse buildCarResponse(Car car, Client client) {
        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .matricule(car.getMatricule())
                .client(client)
                .build();
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }

    // RestTemplate version
    public CarResponse findByIdWithRestTemplate(Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
        Client client = fetchClientWithRestTemplate(car.getClientId());
        return buildCarResponse(car, client);
    }

    // WebClient version


    // FeignClient version

}