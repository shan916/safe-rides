package edu.csus.asi.saferides;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.Status;
import edu.csus.asi.saferides.repository.DriverRepository;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.repository.RideRequestRepository;

import java.util.Date;


@SpringBootApplication
public class SafeRidesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeRidesApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(DriverRepository driverRepository, RideRequestRepository rideRequestRepository) {
		return (args) -> {
			// save a few drivers
			driverRepository.save(new Driver("000000000", "Zeeshan Khaliq", "E0000000", "CA", "male", true, true));
			driverRepository.save(new Driver("000000001", "Ryan Long", "E1111111", "CA", "male", true, false));
			driverRepository.save(new Driver("000000002", "Bryce L Hairabedian", "E2222222", "CA", "male", true, true));
			driverRepository.save(new Driver("000000003", "Edward Ozeruga", "E3333333", "CA", "male", true, false));
			driverRepository.save(new Driver("000000004", "Justin Mendiguarin", "E4444444", "CA", "male", true, true));
			driverRepository.save(new Driver("000000005", "Nik Sorvari", "E5555555", "CA", "male", true, false));

			//rideRequestRepository.save(new RideRequest(id, date, firstname,
			//lastname, phone, peopleCount, pickup, dropoff, status));
			rideRequestRepository.save(new RideRequest(1, new Date(), "Janice",
			"Smith", "619-555-5555", 2, "123 Savory Ln, Sacramento, 95840", "4565 Jenkins Ave, Sacramento, 95840", Status.Unassigned));
			rideRequestRepository.save(new RideRequest(2, new Date(), "Clay",
			"Thompson", "909-555-5555", 4, "456 Rowboat Ravine, Sacramento, 95840", "8898 Baller Ln, Sacramento, 95841", Status.InProgress));
		};
	}

}
