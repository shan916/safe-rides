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
			driverRepository.save(new Driver("Zeeshan", "Khaliq", "E0000000", true, true));
			driverRepository.save(new Driver("Ryan", "Long", "E1111111", true, false));
			driverRepository.save(new Driver("Bryce", "Hairabedian", "E2222222", true, true));
			driverRepository.save(new Driver("Edward", "Ozeruga", "E3333333", true, false));
			driverRepository.save(new Driver("Justin", "Mendiguarin", "E4444444", true, true));
			driverRepository.save(new Driver("Nik", "Sorvari", "E5555555", true, false));
		};
	}
	
}
