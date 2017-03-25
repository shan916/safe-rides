package edu.csus.asi.saferides;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.RideRequestStatus;
import edu.csus.asi.saferides.model.Vehicle;
import edu.csus.asi.saferides.repository.DriverRepository;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import edu.csus.asi.saferides.security.model.Authority;
import edu.csus.asi.saferides.security.model.AuthorityName;
import edu.csus.asi.saferides.security.model.User;
import edu.csus.asi.saferides.security.repository.AuthorityRepository;
import edu.csus.asi.saferides.security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class SafeRidesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafeRidesApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(DriverRepository driverRepository, RideRequestRepository rideRequestRepository,
                                  UserRepository userRepository, AuthorityRepository authorityRepository) {
        return (args) -> {
            // save a few drivers
            Driver driver1 = new Driver("000000000", "Zeeshan", "Khaliq", "9160123456", "CA", "E0000000", "male", true, "Farmers", true);
            Driver driver2 = new Driver("000000001", "Ryan", "Long", "9161234567", "CA", "E1111111", "male", true, "Farmers", false);
            Driver driver3 = new Driver("000000002", "Bryce", "Hairabedian", "9162345678", "CA", "E2222222", "male", true, "Farmers", true);
            Driver driver4 = new Driver("000000003", "Edward", "Ozeruga", "9163456789", "CA", "E3333333", "male", true, "Farmers", false);
            Driver driver5 = new Driver("000000004", "Justin", "Mendiguarin", "9164567890", "CA", "E4444444", "male", true, "Farmers", true);
            Driver driver6 = new Driver("000000005", "Nik", "Sorvari", "9165678901", "CA", "E5555555", "male", true, "Farmers", false);

//			Vehicle vehicle = new Vehicle(driver1, "Tesla", "Model S", "2018", "ELECTRIC", "Silver", 5);

//			for (int i = 6; i < 1000; i++) {
//				Driver driver = new Driver (String.format("%09d", i), "Zeeshan" + i, "Khaliq" + i, "9161234567", "CA",
//						String.format("%09d", i), "male", true, "Farmers", false);
//				vehicle.setDriver(driver);
//				driverRepository.save(driver);
//			}

            Vehicle vehicle1 = new Vehicle(driver1, "Honda", "Civic", "2018", "AAAAAAA", "Magenta", 5);
            Vehicle vehicle2 = new Vehicle(driver2, "Toyota", "Corolla", "2017", "BBBBBBB", "Blue", 7);
            Vehicle vehicle3 = new Vehicle(driver3, "Koenigsegg", "Trevita", "2016", "CCCCCCC", "Green", 2);
            Vehicle vehicle4 = new Vehicle(driver4, "Smart", "Fortwo", "2015", "DDDDDDD", "Grey", 3);
            Vehicle vehicle5 = new Vehicle(driver5, "Volkswagen", "Jetta", "2014", "EEEEEEE", "White", 4);
            Vehicle vehicle6 = new Vehicle(driver6, "Lexus", "Rx", "2013", "FFFFFFF", "Black", 2);

            driver1.setVehicle(vehicle1);
            driver2.setVehicle(vehicle2);
            driver3.setVehicle(vehicle3);
            driver4.setVehicle(vehicle4);
            driver5.setVehicle(vehicle5);
            driver6.setVehicle(vehicle6);

            RideRequest rideRequest1 = new RideRequest(123456, "Zeeshan", "Khaliq", "9161231234", 4, "123 Main St.", "Sacramento",
                    "95818", "345 Main St.", "Sacramento", "95818");
            RideRequest rideRequest2 = new RideRequest(23456, "Nik", "Sorvari", "9161231234", 2, "123 Main St.", "Sacramento",
                    "95818", "345 Main St.", "Sacramento", "95818");
            RideRequest rideRequest3 = new RideRequest(3456, "Justin", "Mendiguarin", "9161231234", 8, "123 Main St.", "Sacramento",
                    "95818", "345 Main St.", "Sacramento", "95818");
            RideRequest rideRequest4 = new RideRequest(456, "Ryan", "Long", "9161231234", 2, "123 Main St.", "Sacramento",
                    "95818", "345 Main St.", "Sacramento", "95818");
            RideRequest rideRequest5 = new RideRequest(56, "Edward", "Ozeruga", "9161231234", 4, "123 Main St.", "Sacramento",
                    "95818", "345 Main St.", "Sacramento", "95818");
            RideRequest rideRequest6 = new RideRequest(6, "Bryce", "Hairabedian", "9161231234", 1, "123 Main St.", "Sacramento",
                    "95818", "345 Main St.", "Sacramento", "95818");
            RideRequest rideRequest7 = new RideRequest(7, "Bill", "Mitchell", "9161231234", 1, "123 Main St.", "Sacramento",
                    "95818", "345 Main St.", "Sacramento", "95818");

            rideRequest2.setStatus(RideRequestStatus.COMPLETE);

            driverRepository.save(driver1);
            driverRepository.save(driver2);
            driverRepository.save(driver3);
            driverRepository.save(driver4);
            driverRepository.save(driver5);
            driverRepository.save(driver6);

            rideRequestRepository.save(rideRequest1);
            rideRequestRepository.save(rideRequest2);
            rideRequestRepository.save(rideRequest3);
            rideRequestRepository.save(rideRequest4);
            rideRequestRepository.save(rideRequest5);
            rideRequestRepository.save(rideRequest6);
            rideRequestRepository.save(rideRequest7);

            Authority authAdmin = new Authority();
            authAdmin.setName(AuthorityName.ROLE_ADMIN);
            Authority authCoordinator = new Authority();
            authCoordinator.setName(AuthorityName.ROLE_COORDINATOR);
            Authority authDriver = new Authority();
            authDriver.setName(AuthorityName.ROLE_DRIVER);
            Authority authRider = new Authority();
            authRider.setName(AuthorityName.ROLE_RIDER);
            authorityRepository.save(authAdmin);
            authorityRepository.save(authCoordinator);
            authorityRepository.save(authDriver);
            authorityRepository.save(authRider);

            User newUser = new User("admin", "Admin", "Smith", "hunter2", "example@email.com");
            List<Authority> authorityList = new ArrayList<Authority>();
            authorityList.add(authAdmin);
            authorityList.add(authCoordinator);
            authorityList.add(authDriver);
            authorityList.add(authRider);
            newUser.setAuthorities(authorityList);
            userRepository.save(newUser);

            System.out.println("***************************************************************");
            System.out.println(driver1.getId());
            System.out.println(driver1.getdriverFirstName());
            System.out.println("***************************************************************");


        };
    }

    @Bean
    public Docket swaggerSettings() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }

}
