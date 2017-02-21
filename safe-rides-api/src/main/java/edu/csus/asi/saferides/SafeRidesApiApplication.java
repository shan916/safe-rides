package edu.csus.asi.saferides;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.repository.DriverRepository;

@SpringBootApplication
public class SafeRidesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeRidesApiApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(DriverRepository driverRepository) {
		return (args) -> {
			// save a few drivers
			driverRepository.save(new Driver("000000000", "Zeeshan Khaliq", "E0000000", "CA", "male", true, true));
			driverRepository.save(new Driver("000000001", "Ryan Long", "E1111111", "CA", "male", true, false));
			driverRepository.save(new Driver("000000002", "Bryce Hairabedian", "E2222222", "CA", "male", true, true));
			driverRepository.save(new Driver("000000003", "Edward Ozeruga", "E3333333", "CA", "male", true, false));
			driverRepository.save(new Driver("000000004", "Justin Mendiguarin", "E4444444", "CA", "male", true, true));
			driverRepository.save(new Driver("000000005", "Nik Sorvari", "E5555555", "CA", "male", true, false));
		};
	}
	
}
