package com.esp.dbcar1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class DbCar1Application {

    public static void main(String[] args) {
        SpringApplication.run(DbCar1Application.class, args);
    }

}
class Car{
    private final String id;
    private String brand;
    private String model;
    private String color;
    private String registerName;
    private String year;
    private String price;

    public Car(String id, String brand, String model, String color, String registerName, String year, String price) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.registerName = registerName;
        this.year = year;
        this.price = price;
    }

    public Car(String brand, String model, String color, String registerName, String year, String price) {
        this(UUID.randomUUID().toString(),brand,model,color,registerName,year,price);
    }

    public String getId() {
        return id;
    }

}

@RestController
@RequestMapping("/AllCar")
class RestCarController {

    private List<Car> AllCar = new ArrayList<>();

    public RestCarController() {
        AllCar.addAll(List.of(
                new Car("Peugeot","P1","Bleu","AA-234-BB","2016","20000000"),
                new Car("Peugeot2","P2","Rouge","AA-235-BB","2017","20000000"),
                new Car("Peugeot3","P3","Vert","AA-236-BB","2018","20000000"),
                new Car("Peugeot4","P4","Gris","AA-237-BB","2019","20000000"),
                new Car("Peugeot5","P5","Marron","AA-238-BB","2021","20000000")
        ));
    }

    @GetMapping
    Iterable<Car> getCar() {
        return AllCar;
    }

    @GetMapping("/{id}")
    Optional<Car> getCarById(@PathVariable String id) {
        for (Car c:AllCar) {
            if (c.getId().equals(id)){
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    @PostMapping
    Car postCar(@RequestBody Car car) {
        AllCar.add(car);
        return car;
    }

    @PutMapping("/{id}")
    ResponseEntity<Car> putCar(@PathVariable String id, @RequestBody Car car) {
        int carIndex = -1;
        for (Car c : AllCar) {
            if (c.getId().equals(id)) {
                carIndex = AllCar.indexOf(c);
                AllCar.set(carIndex, c);
                break;
            }
        }
        return (carIndex == -1) ?
                new ResponseEntity<>(postCar(car), HttpStatus.CREATED) :
                new ResponseEntity<>(car, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    void deleteCar(@PathVariable String id) {
        AllCar.removeIf(c -> c.getId().equals(id));
    }
}