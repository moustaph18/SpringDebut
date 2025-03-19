package com.esp.dbcar1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public Car(@JsonProperty("id") String id, @JsonProperty("brand") String brand,@JsonProperty("model") String model,
               @JsonProperty("color") String color,@JsonProperty("registerName") String registerName, @JsonProperty("year") String year,
               @JsonProperty("price") String price) {
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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