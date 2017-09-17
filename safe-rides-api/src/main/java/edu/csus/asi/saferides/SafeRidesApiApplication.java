package edu.csus.asi.saferides;

import edu.csus.asi.saferides.model.*;
import edu.csus.asi.saferides.repository.*;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.service.GeocodingService;
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

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * The class that bootstraps the application
 * Sets the default database state and configures swagger
 */
@SpringBootApplication
@EnableSwagger2
public class SafeRidesApiApplication {

    @Autowired
    private GeocodingService geocodingService;

    /**
     * The main method
     *
     * @param args application arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SafeRidesApiApplication.class, args);
    }

    /**
     * Demo data for the initial start of the application
     *
     * @param driverRepository         driver repository
     * @param rideRequestRepository    ride request repository
     * @param userRepository           user repository
     * @param authorityRepository      authority repository
     * @param driverLocationRepository driver location repository
     * @param configurationRepository  configuration repository
     * @return CommandLineRunner
     */
    @Bean
    public CommandLineRunner demo(DriverRepository driverRepository, RideRequestRepository rideRequestRepository,
                                  UserRepository userRepository, AuthorityRepository authorityRepository, DriverLocationRepository driverLocationRepository,
                                  ConfigurationRepository configurationRepository, JwtTokenUtil jwtTokenUtil) {
        return (args) -> {

            // save a few drivers
            Driver driver0 = new Driver("000000000", "Melanie", "Birdsell", "9165797607", true, true, "Farmers", true);
            Driver driver1 = new Driver("000000001", "Jayne", "Knight", "9166675866", true, true, "Farmers", true);
            Driver driver2 = new Driver("000000002", "Mary", "Rose", "9167471328", true, true, "Farmers", true);
            Driver driver3 = new Driver("000000003", "Carl", "Wertz", "4053468560", true, true, "Farmers", true);
            Driver driver4 = new Driver("000000004", "Keith", "Watts", "9166775773", true, true, "Farmers", true);
            Driver driver5 = new Driver("000000005", "Bobby", "Obyrne", "9169062157", true, true, "Farmers", true);
            Driver driver6 = new Driver("000000006", "Olivia", "Defreitas", "9162237579", true, true, "Farmers", true);
            Driver driver7 = new Driver("000000007", "Kenny", "Rivera", "9164571650", true, true, "Farmers", true);
            Driver driver8 = new Driver("000000008", "Sean", "Jenkins", "9164054110", true, true, "Farmers", true);
            Driver driver9 = new Driver("000000009", "Robert", "Montoya", "9164802066", true, true, "Farmers", true);

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

            DriverLocation loc0 = new DriverLocation(38.5617341, -121.4309151);
            DriverLocation loc1 = new DriverLocation(38.5604605, -121.430052);
            DriverLocation loc2 = new DriverLocation(38.5583356, -121.4284198);
            DriverLocation loc3 = new DriverLocation(38.5536317, -121.4248821);

            loc0.setDriver(driver0);
            loc1.setDriver(driver0);
            loc2.setDriver(driver0);
            loc3.setDriver(driver0);

            RideRequest rideRequest0 = new RideRequest(
                    "146978572",
                    "9162601900",
                    1,
                    "8535 La Riviera Dr",
                    "Sacramento",
                    "3109 Occidental Dr",
                    "Sacramento");
            RideRequest rideRequest1 = new RideRequest(
                    "380256637",
                    "9165129630",
                    2,
                    "4041 C St",
                    "Sacramento",
                    "6325 14th Ave",
                    "Sacramento");
            RideRequest rideRequest2 = new RideRequest(
                    "146978572",
                    "9162601900",
                    3,
                    "4350 J St",
                    "Sacramento",
                    "1900 22nd St",
                    "Sacramento");
            RideRequest rideRequest3 = new RideRequest(
                    "353488787",
                    "9167033428",
                    3,
                    "5540 Spilman Ave",
                    "Sacramento",
                    "8671 Everglade Dr",
                    "Sacramento");
            RideRequest rideRequest4 = new RideRequest(
                    "454397082",
                    "9164321498",
                    1,
                    "9409 Mira Del Rio Dr",
                    "Sacramento",
                    "9065 Canberra Dr",
                    "Sacramento");
            RideRequest rideRequest5 = new RideRequest(
                    "124796316",
                    "9169553182",
                    1,
                    "8213 Lake Forest Dr",
                    "Sacramento",
                    "3115 Sierra Oaks Dr",
                    "Sacramento");
            RideRequest rideRequest6 = new RideRequest(
                    "012644411",
                    "9165338593",
                    2,
                    "4631 Nickles Way",
                    "Sacramento",
                    "1961 Howe Ave",
                    "Sacramento");
            RideRequest rideRequest7 = new RideRequest(
                    "507271867",
                    "9162820006",
                    2,
                    "3805 Becerra Way",
                    "Sacramento",
                    "2025 W El Camino Ave",
                    "Sacramento");
            RideRequest rideRequest8 = new RideRequest(
                    "653902142",
                    "9167739687",
                    3,
                    "3340 Soda Way",
                    "Sacramento",
                    "1235 Pebblewood Dr",
                    "Sacramento");
            RideRequest rideRequest9 = new RideRequest(
                    "393465734",
                    "9164712820",
                    1,
                    "8071 La Riviera Dr",
                    "Sacramento",
                    "345 Main Ave.",
                    "Sacramento");
            RideRequest rideRequest10 = new RideRequest(
                    "227299993",
                    "9164375577",
                    1,
                    "2780 Millcreek Dr",
                    "Sacramento",
                    "2441 Seamist Dr",
                    "Sacramento");
            RideRequest rideRequest11 = new RideRequest(
                    "060116327",
                    "9162422785",
                    1,
                    "505 10th St",
                    "Sacramento",
                    "7901 La Riviera Dr",
                    "Sacramento");
            RideRequest rideRequest12 = new RideRequest(
                    "645735866",
                    "9163505114",
                    3,
                    "2349 Fruitridge Rd",
                    "Sacramento",
                    "4900 10th Ave",
                    "Sacramento");
            RideRequest rideRequest13 = new RideRequest(
                    "314246498",
                    "9164639941",
                    2,
                    "4617 12th Ave",
                    "Sacramento",
                    "2498 24th St",
                    "Sacramento");
            RideRequest rideRequest14 = new RideRequest(
                    "536200095",
                    "9166348237",
                    1,
                    "1403 V St",
                    "Sacramento",
                    "1126 T St",
                    "Sacramento");
            RideRequest rideRequest15 = new RideRequest(
                    "737128850",
                    "9162213034",
                    3,
                    "455 Richards Blvd",
                    "Sacramento",
                    "534 Hartnell Pl",
                    "Sacramento");
            RideRequest rideRequest16 = new RideRequest(
                    "748528850",
                    "9162768046",
                    3,
                    "6141 Hall Ln",
                    "Sacramento",
                    "4786 Garfield Ave",
                    "Sacramento");
            RideRequest rideRequest17 = new RideRequest(
                    "737159820",
                    "9165360504",
                    3,
                    "5630 Roseville Rd",
                    "Sacramento",
                    "2121 Moonstone Way",
                    "Sacramento");
            RideRequest rideRequest18 = new RideRequest(
                    "127128850",
                    "9168732659",
                    3,
                    "1570 Edgemore Ave",
                    "Sacramento",
                    "7235 Pritchard Rd",
                    "Sacramento");
            RideRequest rideRequest19 = new RideRequest(
                    "257129650",
                    "9167646255",
                    3,
                    "7300 Frasinetti Rd",
                    "Sacramento",
                    "8637 Oakbank Way",
                    "Sacramento");

            User user0 = new User("kwinters", "Kevin", "Winters");
            User user1 = new User("pskaggs", "Peter", "Skaggs");
            User user3 = new User("jbonner", "Jack", "Bonner");
            User user4 = new User("dreaves", "Daniel", "Reaves");
            User user5 = new User("jminer", "James", "Miner");
            User user6 = new User("jalexander", "Jennifer", "Alexander");
            User user7 = new User("cthomas", "charlene", "thomas");
            User user8 = new User("rjames", "Rolanda", "James");
            User user9 = new User("dlang", "Debra", "Lang");
            User user10 = new User("sgrantham", "Sue", "Grantham");
            User user11 = new User("ncox", "Noemi", "Cox");
            User user12 = new User("jpence", "Joy", "Pence");
            User user13 = new User("rrowell", "Rochelle", "Rowell");
            User user14 = new User("ctuttle", "Charles", "Tuttle");
            User user15 = new User("mtutt", "Mario", "Tutt");
            User user16 = new User("wscruggs", "Wanda", "Scruggs");
            User user17 = new User("mbutler", "Mercy", "Butler");
            User user18 = new User("dlopez", "Dennis", "Lopez");
            User user19 = new User("dmeyer", "David", "Meyer");

            rideRequest0.setUser(user0);
            rideRequest0.setDriver(driver0);
            rideRequest0.setStatus(RideRequestStatus.COMPLETE);
            rideRequest1.setUser(user1);
            rideRequest1.setDriver(driver0);
            rideRequest1.setStatus(RideRequestStatus.COMPLETE);
            rideRequest2.setUser(user0);
            rideRequest2.setDriver(driver0);
            rideRequest2.setStatus(RideRequestStatus.COMPLETE);
            rideRequest3.setUser(user3);
            rideRequest3.setDriver(driver0);
            rideRequest3.setStatus(RideRequestStatus.ASSIGNED);
            rideRequest3.setAssignedDate(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)));
            rideRequest3.setEstimatedTime("15");
            rideRequest4.setUser(user4);
            rideRequest4.setDriver(driver1);
            rideRequest4.setStatus(RideRequestStatus.COMPLETE);
            rideRequest5.setUser(user5);
            rideRequest5.setDriver(driver1);
            rideRequest5.setStatus(RideRequestStatus.COMPLETE);
            rideRequest6.setUser(user6);
            rideRequest6.setDriver(driver1);
            rideRequest6.setStatus(RideRequestStatus.PICKINGUP);
            rideRequest6.setAssignedDate(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)));
            rideRequest6.setEstimatedTime("30");
            rideRequest7.setUser(user7);
            rideRequest7.setDriver(driver2);
            rideRequest7.setStatus(RideRequestStatus.COMPLETE);
            rideRequest8.setUser(user8);
            rideRequest8.setDriver(driver2);
            rideRequest8.setStatus(RideRequestStatus.DROPPINGOFF);
            rideRequest8.setAssignedDate(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)));
            rideRequest8.setEstimatedTime("45");
            rideRequest9.setUser(user9);
            rideRequest9.setDriver(driver3);
            rideRequest9.setStatus(RideRequestStatus.ASSIGNED);
            rideRequest9.setAssignedDate(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)));
            rideRequest9.setEstimatedTime("1 hour");
            rideRequest10.setUser(user10);
            rideRequest10.setDriver(driver4);
            rideRequest10.setStatus(RideRequestStatus.PICKINGUP);
            rideRequest10.setAssignedDate(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)));
            rideRequest10.setEstimatedTime("> 1 hour");
            rideRequest11.setUser(user11);
            rideRequest11.setDriver(driver5);
            rideRequest11.setStatus(RideRequestStatus.ASSIGNED);
            rideRequest11.setAssignedDate(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)));
            rideRequest11.setEstimatedTime("15");
            rideRequest12.setUser(user12);
            rideRequest12.setDriver(driver6);
            rideRequest12.setStatus(RideRequestStatus.DROPPINGOFF);
            rideRequest12.setAssignedDate(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)));
            rideRequest12.setEstimatedTime("30");

            rideRequest13.setUser(user13);
            rideRequest14.setUser(user14);
            rideRequest15.setUser(user15);
            rideRequest16.setUser(user16);
            rideRequest17.setUser(user17);
            rideRequest18.setUser(user18);
            rideRequest19.setUser(user19);

            geocodingService.setCoordinates(rideRequest0);
            geocodingService.setCoordinates(rideRequest1);
            geocodingService.setCoordinates(rideRequest2);
            geocodingService.setCoordinates(rideRequest3);
            geocodingService.setCoordinates(rideRequest4);
            geocodingService.setCoordinates(rideRequest5);
            geocodingService.setCoordinates(rideRequest6);
            geocodingService.setCoordinates(rideRequest7);
            geocodingService.setCoordinates(rideRequest8);
            geocodingService.setCoordinates(rideRequest9);
            geocodingService.setCoordinates(rideRequest10);
            geocodingService.setCoordinates(rideRequest11);
            geocodingService.setCoordinates(rideRequest12);
            geocodingService.setCoordinates(rideRequest13);
            geocodingService.setCoordinates(rideRequest14);
            geocodingService.setCoordinates(rideRequest15);
            geocodingService.setCoordinates(rideRequest16);
            geocodingService.setCoordinates(rideRequest17);
            geocodingService.setCoordinates(rideRequest18);
            geocodingService.setCoordinates(rideRequest19);
            
            User driver = new User("driver", "Driver", "Long");

            User coordinator = new User("edwardozeruga", "Coordinator", "Jones");

            User admin = new User("admin", "Admin", "Smith");

            ArrayList<Authority> riderAuthorityList = new ArrayList<Authority>();
            ArrayList<Authority> driverAuthorityList = new ArrayList<Authority>();
            ArrayList<Authority> coordinatorAuthorityList = new ArrayList<Authority>();
            ArrayList<Authority> adminAuthorityList = new ArrayList<Authority>();

            Authority authAdmin = new Authority(AuthorityName.ROLE_ADMIN);
            Authority authCoordinator = new Authority(AuthorityName.ROLE_COORDINATOR);
            Authority authDriver = new Authority(AuthorityName.ROLE_DRIVER);
            Authority authRider = new Authority(AuthorityName.ROLE_RIDER);
            authorityRepository.save(authAdmin);
            authorityRepository.save(authCoordinator);
            authorityRepository.save(authDriver);
            authorityRepository.save(authRider);

            riderAuthorityList.add(authRider);
            driverAuthorityList.add(authRider);
            driverAuthorityList.add(authDriver);
            coordinatorAuthorityList.add(authRider);
            coordinatorAuthorityList.add(authDriver);
            coordinatorAuthorityList.add(authCoordinator);
            adminAuthorityList.add(authRider);
            adminAuthorityList.add(authDriver);
            adminAuthorityList.add(authCoordinator);
            adminAuthorityList.add(authAdmin);

            admin.setAuthorities(adminAuthorityList);
            coordinator.setAuthorities(coordinatorAuthorityList);
            driver.setAuthorities(driverAuthorityList);

            user0.setAuthorities(riderAuthorityList);
            user1.setAuthorities(riderAuthorityList);
            user3.setAuthorities(riderAuthorityList);
            user4.setAuthorities(riderAuthorityList);
            user5.setAuthorities(riderAuthorityList);
            user6.setAuthorities(riderAuthorityList);
            user7.setAuthorities(riderAuthorityList);
            user8.setAuthorities(riderAuthorityList);
            user9.setAuthorities(riderAuthorityList);
            user10.setAuthorities(riderAuthorityList);
            user11.setAuthorities(riderAuthorityList);
            user12.setAuthorities(riderAuthorityList);
            user13.setAuthorities(riderAuthorityList);
            user14.setAuthorities(riderAuthorityList);
            user15.setAuthorities(riderAuthorityList);
            user16.setAuthorities(riderAuthorityList);
            user17.setAuthorities(riderAuthorityList);
            user18.setAuthorities(riderAuthorityList);
            user19.setAuthorities(riderAuthorityList);

            driver0.getUser().setAuthorities(driverAuthorityList);
            driver1.getUser().setAuthorities(driverAuthorityList);
            driver2.getUser().setAuthorities(driverAuthorityList);
            driver3.getUser().setAuthorities(driverAuthorityList);
            driver4.getUser().setAuthorities(driverAuthorityList);
            driver5.getUser().setAuthorities(driverAuthorityList);
            driver6.getUser().setAuthorities(driverAuthorityList);
            driver7.getUser().setAuthorities(driverAuthorityList);
            driver8.getUser().setAuthorities(driverAuthorityList);
            driver9.getUser().setAuthorities(driverAuthorityList);

            userRepository.save(admin);
            userRepository.save(coordinator);
            userRepository.save(driver);

            userRepository.save(user0);
            userRepository.save(user1);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);
            userRepository.save(user6);
            userRepository.save(user7);
            userRepository.save(user8);
            userRepository.save(user9);
            userRepository.save(user10);
            userRepository.save(user11);
            userRepository.save(user12);
            userRepository.save(user13);
            userRepository.save(user14);
            userRepository.save(user15);
            userRepository.save(user16);
            userRepository.save(user17);
            userRepository.save(user18);
            userRepository.save(user19);

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

            driverLocationRepository.save(loc0);
            driverLocationRepository.save(loc1);
            driverLocationRepository.save(loc2);
            driverLocationRepository.save(loc3);

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
            rideRequestRepository.save(rideRequest16);
            rideRequestRepository.save(rideRequest17);
            rideRequestRepository.save(rideRequest18);
            rideRequestRepository.save(rideRequest19);

            LocalTime startTime = LocalTime.of(20, 00, 0);
            LocalTime endTime = LocalTime.of(02, 00, 0);
            Configuration newConfig = new Configuration(startTime, endTime);
            ArrayList<DayOfWeek> dayOfWeeks = new ArrayList<DayOfWeek>();
            dayOfWeeks.add(DayOfWeek.WEDNESDAY);
            dayOfWeeks.add(DayOfWeek.THURSDAY);
            dayOfWeeks.add(DayOfWeek.FRIDAY);
            dayOfWeeks.add(DayOfWeek.SATURDAY);
            newConfig.setDaysOfWeek(dayOfWeeks);
            configurationRepository.save(newConfig);
        };
    }

    /**
     * Configures swagger
     *
     * @return Docket
     */
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
