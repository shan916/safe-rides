package edu.csus.asi.saferides;

import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.Status;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.Vehicle;
import edu.csus.asi.saferides.repository.DriverRepository;
import edu.csus.asi.saferides.repository.RideRequestRepository;

@SpringBootApplication
public class SafeRidesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeRidesApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(DriverRepository driverRepository, RideRequestRepository rideRequestRepository) {
		return (args) -> {
			// save a few drivers
			Driver driver1 = new Driver("000000000", "Zeeshan", "Khaliq", "E0000000", "CA", "male", true, true);
			Driver driver2 = new Driver("000000001", "Ryan", "Long", "E1111111", "CA", "male", true, false);
			Driver driver3 = new Driver("000000002", "Bryce", "Hairabedian", "E2222222", "CA", "male", true, true);
			Driver driver4 = new Driver("000000003", "Edward", "Ozeruga", "E3333333", "CA", "male", true, false);
			Driver driver5 = new Driver("000000004", "Justin", "Mendiguarin", "E4444444", "CA", "male", true, true);
			Driver driver6 = new Driver("000000005", "Nik", "Sorvari", "E5555555", "CA", "male", true, false);

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

			RideRequest rideRequest1 = new RideRequest(123456, "Zeeshan", "", "9161231234", 4, "123 Main St.", "Sacramento",
					"95818", "345 Main St.", "Sacramento", "95818");
			RideRequest rideRequest2 = new RideRequest(23456, "Nik", "", "9161231234", 2, "123 Main St.", "Sacramento",
					"95818", "345 Main St.", "Sacramento", "95818");
			RideRequest rideRequest3 = new RideRequest(3456, "Justin", "", "9161231234", 8, "123 Main St.", "Sacramento",
					"95818", "345 Main St.", "Sacramento", "95818");
			RideRequest rideRequest4 = new RideRequest(456, "Ryan", "", "9161231234", 2, "123 Main St.", "Sacramento",
					"95818", "345 Main St.", "Sacramento", "95818");
			RideRequest rideRequest5 = new RideRequest(56, "Edward", "", "9161231234", 4, "123 Main St.", "Sacramento",
					"95818", "345 Main St.", "Sacramento", "95818");
			RideRequest rideRequest6 = new RideRequest(6, "Bryce", "", "9161231234", 1, "123 Main St.", "Sacramento",
					"95818", "345 Main St.", "Sacramento", "95818");
			RideRequest rideRequest7 = new RideRequest(7, "Bill", "", "9161231234", 1, "123 Main St.", "Sacramento",
					"95818", "345 Main St.", "Sacramento", "95818");

			rideRequest2.setDriver(driver4);
			rideRequest2.setStatus(Status.COMPLETE);
			rideRequest3.setDriver(driver4);
			rideRequest4.setDriver(driver5);
			rideRequest5.setDriver(driver6);


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
		};
	}

}
