package edu.csus.asi.saferides;

import com.google.common.base.Predicates;
import edu.csus.asi.saferides.model.*;
import edu.csus.asi.saferides.repository.*;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.service.GeocodingService;
import edu.csus.asi.saferides.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
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
 * Configures swagger
 */
@SpringBootApplication
@EnableSwagger2
@EnableCaching
class SafeRidesApiApplication {
    /**
     * The main method
     *
     * @param args application arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SafeRidesApiApplication.class, args);
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
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .pathMapping("/");
    }
}
