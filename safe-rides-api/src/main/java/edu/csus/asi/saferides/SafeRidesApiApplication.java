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
import edu.csus.asi.saferides.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    Util util;

    public static void main(String[] args) {
        SpringApplication.run(SafeRidesApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(DriverRepository driverRepository, RideRequestRepository rideRequestRepository,
                                  UserRepository userRepository, AuthorityRepository authorityRepository) {
        return (args) -> {
            // save a few drivers
            Driver driver0 = new Driver("000000000", "Melanie", "Birdsell", "9165797607", "CA", "E0000000", "Female", true, "Farmers", true);
            Driver driver1 = new Driver("000000001", "Jayne", "Jayne", "9166675866", "CA", "E1111111", "Female", true, "Farmers", false);
            Driver driver2 = new Driver("000000002", "Mary", "Rose", "9167471328", "CA", "E2222222", "Female", true, "Farmers", true);
            Driver driver3 = new Driver("000000003", "Carl", "Wertz", "4053468560", "CA", "E3333333", "Male", true, "Farmers", false);
            Driver driver4 = new Driver("000000004", "Keith", "Watts", "9166775773", "CA", "E4444444", "Male", true, "Farmers", true);
            Driver driver5 = new Driver("000000005", "Bobby", "Obyrne", "9169062157","CA", "E5555555", "Male", true, "Farmers", false);
            Driver driver6 = new Driver("000000006", "Olivia", "Defreitas", "9162237579","CA", "E6666666", "Female", true, "Farmers", false);
            Driver driver7 = new Driver("000000007", "Kenny", "Rivera", "9164571650","CA", "E7777777", "Male", true, "Farmers", false);
            Driver driver8 = new Driver("000000008", "Sean", "Jenkins", "9164054110","CA", "E8888888", "Male", true, "Farmers", false);
            Driver driver9 = new Driver("000000009", "Robert", "Montoya", "9164802066","CA", "E9999999", "Male", true, "Farmers", false);

            Vehicle vehicle0 = new Vehicle(driver0, "Honda", "CR-V", "2006", "AAAAAAA", "Magenta", 5);
            Vehicle vehicle1 = new Vehicle(driver1, "Lexus ", "LS", "2005", "BBBBBBB", "Purple", 7);
            Vehicle vehicle2 = new Vehicle(driver2, "Ariel", "Atom", "2004", "CCCCCCC", "Green", 2);
            Vehicle vehicle3 = new Vehicle(driver3, "Green", "Mazda", "1997", "DDDDDDD", "Grey", 3);
            Vehicle vehicle4 = new Vehicle(driver4, "Volkswagen", "Jetta", "2014", "EEEEEEE", "White", 4);
            Vehicle vehicle5 = new Vehicle(driver5, "Volvo", "XC90", "2006", "FFFFFFF", "Black", 2);
            Vehicle vehicle6 = new Vehicle(driver6, "Nissan", "Maxima", "2009", "GGGGGGG", "Grey", 3);
            Vehicle vehicle7 = new Vehicle(driver7, "BMW", "X5", "2000", "HHHHHHH", "White", 4);
            Vehicle vehicle8 = new Vehicle(driver8, "Nissan", "Almera", "2003", "IIIIIII", "Black", 2);
            Vehicle vehicle9 = new Vehicle(driver9, "Ginetta", "G33", "1998", "JJJJJJJ", "White", 4);


            driver0.setVehicle(vehicle0);
            driver1.setVehicle(vehicle1);
            driver2.setVehicle(vehicle2);
            driver3.setVehicle(vehicle3);
            driver4.setVehicle(vehicle4);
            driver5.setVehicle(vehicle5);
            driver6.setVehicle(vehicle6);
            driver7.setVehicle(vehicle7);
            driver8.setVehicle(vehicle8);
            driver9.setVehicle(vehicle9);


            RideRequest rideRequest0 = new RideRequest(
                    146978572,
                    "Kevin",
                    "Winters",
                    "9162601900",
                    1,
                    "8535 La Riviera Dr",
                    "Sacramento",
                    "95826",
                    "3109 Occidental Dr",
                    "Sacramento",
                    "95826");
            RideRequest rideRequest1 = new RideRequest(
                    380256637,
                    "Peter",
                    "Skaggs",
                    "9165129630",
                    2,
                    "4041 C St",
                    "Sacramento",
                    "95819",
                    "6325 14th Ave",
                    "Sacramento",
                    "95820");
            RideRequest rideRequest2 = new RideRequest(
                    017150500,
                    "Kevin",
                    "Winters",
                    "9162601900",
                    3,
                    "4350 J St",
                    "Sacramento",
                    "95819",
                    "1900 22nd St",
                    "Sacramento",
                    "95816");
            RideRequest rideRequest3 = new RideRequest(
                    353488787,
                    "Jack",
                    "Bonner",
                    "9167033428",
                    4,
                    "5540 Spilman Ave",
                    "Sacramento",
                    "95819",
                    "8671 Everglade Dr",
                    "Sacramento",
                    "95826");
            RideRequest rideRequest4 = new RideRequest(
                    454397082,
                    "Daniel",
                    "Reaves",
                    "9164321498",
                    1,
                    "9409 Mira Del Rio Dr",
                    "Sacramento",
                    "95827",
                    "9065 Canberra Dr",
                    "Sacramento",
                    "95826");
            RideRequest rideRequest5 = new RideRequest(
                    124796316,
                    "James",
                    "Miner",
                    "9169553182",
                    1,
                    "8213 Lake Forest Dr",
                    "Sacramento",
                    "95826",
                    "3115 Sierra Oaks Dr",
                    "Sacramento",
                    "95864");
            RideRequest rideRequest6 = new RideRequest(
                    012644411,
                    "Jennifer",
                    "Alexander",
                    "9165338593",
                    2,
                    "4631 Nickles Way",
                    "Sacramento",
                    "95864",
                    "1961 Howe Ave",
                    "Sacramento",
                    "95825");
            RideRequest rideRequest7 = new RideRequest(
                    507271867,
                    "Charlene",
                    "Thomas",
                    "9162820006",
                    2,
                    "3805 Becerra Way",
                    "Sacramento",
                    "95821",
                    "2025 W El Camino Ave",
                    "Sacramento",
                    "95833");
            RideRequest rideRequest8 = new RideRequest(
                    653902142,
                    "Rolanda",
                    "James",
                    "9167739687",
                    3,
                    "3340 Soda Way",
                    "Sacramento",
                    "95834",
                    "1235 Pebblewood Dr",
                    "Sacramento",
                    "95834");
            RideRequest rideRequest9 = new RideRequest(
                    393465734,
                    "Debra",
                    "Lang",
                    "9164712820",
                    1,
                    "8071 La Riviera Dr",
                    "Sacramento",
                    "95826",
                    "345 Main St.",
                    "Sacramento",
                    "95818");
            RideRequest rideRequest10 = new RideRequest(
                    227299993,
                    "Sue",
                    "Grantham",
                    "9164375577",
                    1,
                    "2780 Millcreek Dr",
                    "Sacramento",
                    "95833",
                    "2441 Seamist Dr",
                    "Sacramento",
                    "95833");
            RideRequest rideRequest11 = new RideRequest(
                    060116327,
                    "Noemi",
                    "Cox",
                    "9162422785",
                    1,
                    "505 10th St",
                    "Sacramento",
                    "95814",
                    "7901 La Riviera Dr",
                    "Sacramento",
                    "95826");
            RideRequest rideRequest12 = new RideRequest(
                    645735866,
                    "Joy",
                    "Pence",
                    "9163505114",
                    4,
                    "2349 Fruitridge Rd",
                    "Sacramento",
                    "95822",
                    "4900 10th Ave",
                    "Sacramento",
                    "95820");
            RideRequest rideRequest13 = new RideRequest(
                    314246498,
                    "Rochelle",
                    "Rowell",
                    "9164639941",
                    2,
                    "4617 12th Ave",
                    "Sacramento",
                    "95820",
                    "2498 24th St",
                    "Sacramento",
                    "95818");
            RideRequest rideRequest14 = new RideRequest(
                    536200095,
                    "Charles",
                    "Tuttle",
                    "9166348237",
                    1,
                    "1403 V St",
                    "Sacramento",
                    "95818",
                    "1126 T St",
                    "Sacramento",
                    "95811");
            RideRequest rideRequest15 = new RideRequest(
                    737128850,
                    "Mario",
                    "Tutt",
                    "9162213034",
                    3,
                    "455 Richards Blvd",
                    "Sacramento",
                    "95811",
                    "534 Hartnell Pl",
                    "Sacramento",
                    "95825");

            rideRequest0.setDriver(driver0);
            rideRequest0.setStatus(RideRequestStatus.COMPLETE);
            rideRequest1.setDriver(driver0);
            rideRequest1.setStatus(RideRequestStatus.COMPLETE);
            rideRequest2.setDriver(driver0);
            rideRequest2.setStatus(RideRequestStatus.COMPLETE);
            rideRequest3.setDriver(driver0);
            rideRequest3.setStatus(RideRequestStatus.ASSIGNED);
            rideRequest4.setDriver(driver1);
            rideRequest4.setStatus(RideRequestStatus.COMPLETE);
            rideRequest5.setDriver(driver1);
            rideRequest5.setStatus(RideRequestStatus.COMPLETE);
            rideRequest6.setDriver(driver1);
            rideRequest6.setStatus(RideRequestStatus.INPROGRESS);
            rideRequest7.setDriver(driver2);
            rideRequest7.setStatus(RideRequestStatus.COMPLETE);
            rideRequest8.setDriver(driver2);
            rideRequest6.setStatus(RideRequestStatus.INPROGRESS);
            rideRequest9.setDriver(driver3);
            rideRequest6.setStatus(RideRequestStatus.ASSIGNED);
            rideRequest10.setDriver(driver4);
            rideRequest6.setStatus(RideRequestStatus.INPROGRESS);
            rideRequest11.setDriver(driver5);
            rideRequest6.setStatus(RideRequestStatus.ASSIGNED);
            rideRequest12.setDriver(driver6);
            rideRequest6.setStatus(RideRequestStatus.INPROGRESS);
            rideRequest13.setDriver(driver7);
            rideRequest6.setStatus(RideRequestStatus.ASSIGNED);
            rideRequest14.setDriver(driver8);
            rideRequest6.setStatus(RideRequestStatus.INPROGRESS);
            rideRequest15.setDriver(driver9);
            rideRequest6.setStatus(RideRequestStatus.ASSIGNED);

            util.setCoordinates(rideRequest0);
            util.setCoordinates(rideRequest1);
            util.setCoordinates(rideRequest2);
            util.setCoordinates(rideRequest3);
            util.setCoordinates(rideRequest4);
            util.setCoordinates(rideRequest5);
            util.setCoordinates(rideRequest6);
            util.setCoordinates(rideRequest7);
            util.setCoordinates(rideRequest8);
            util.setCoordinates(rideRequest9);
            util.setCoordinates(rideRequest10);
            util.setCoordinates(rideRequest11);
            util.setCoordinates(rideRequest12);
            util.setCoordinates(rideRequest13);
            util.setCoordinates(rideRequest14);
            util.setCoordinates(rideRequest15);

            driverRepository.save(driver0);
            driverRepository.save(driver1);
            driverRepository.save(driver2);
            driverRepository.save(driver3);
            driverRepository.save(driver4);
            driverRepository.save(driver5);
            driverRepository.save(driver6);
            driverRepository.save(driver7);
            driverRepository.save(driver8);
            driverRepository.save(driver9);

            rideRequestRepository.save(rideRequest0);
            rideRequestRepository.save(rideRequest1);
            rideRequestRepository.save(rideRequest2);
            rideRequestRepository.save(rideRequest3);
            rideRequestRepository.save(rideRequest4);
            rideRequestRepository.save(rideRequest5);
            rideRequestRepository.save(rideRequest6);
            rideRequestRepository.save(rideRequest7);
            rideRequestRepository.save(rideRequest8);
            rideRequestRepository.save(rideRequest9);
            rideRequestRepository.save(rideRequest10);
            rideRequestRepository.save(rideRequest11);
            rideRequestRepository.save(rideRequest12);
            rideRequestRepository.save(rideRequest13);
            rideRequestRepository.save(rideRequest14);
            rideRequestRepository.save(rideRequest15);

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

            User driver = new User("driver", "Driver", "Long", "hunter2", "example@email.com");
            User coordinator = new User("coordinator", "Coordinator", "Jones", "hunter2", "example@email.com");
            User admin = new User("admin", "Admin", "Smith", "hunter2", "example@email.com");

            List<Authority> authorityList = new ArrayList<Authority>();

            authorityList.add(authRider);
            authorityList.add(authDriver);
            driver.setAuthorities(authorityList);
            userRepository.save(driver);
            authorityList.add(authCoordinator);
            coordinator.setAuthorities(authorityList);
            userRepository.save(coordinator);
            authorityList.add(authAdmin);
            admin.setAuthorities(authorityList);
            userRepository.save(admin);
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
