# safe-rides-api

### Running Locally

##### Eclipse
1. Go to `File -> Import`
2. Choose `Existing Maven Projects`
3. Browse to the location of the project and click `Next` to import the project
4. Right click on the `SafeRidesApiApplication` class and choose `Run As` -> Java Application

##### IntelliJ
1. Go to `File -> New -> Project from Existing Sources...`
2. Browse to the location of the project (select the pom.xml) and click OK. Defaults are okay (confirming stuff in the maven config) - click through the dialogues  
3. Click Play icon

You should see some output that looks like:
```
 .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.1.RELEASE)
 ............
 ............
 ............
 main] o.s.c.support.DefaultLifecycleProcessor  : Starting beans in phase 0
 main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
 main] e.c.a.saferides.SafeRidesApiApplication  : Started SafeRidesApiApplication in 22.261 seconds (JVM running for 26.356)
```

If successfully started you should see a message saying `Started SafeRidesApiApplication`

An embedded Tomcat server is automatically started on port 8080.

### Making API Calls
1. Download and install the [Postman app](https://www.getpostman.com)
2. Open the Postman app and set the URL to `http://localhost:8080/safe-rides-api/drivers`
3. Hit send and you should see an HTTP response that looks like
```
	[
	  {
	    "id": 2,
	    "firstName": "Ryan",
	    "lastName": "Long",
	    "dlNumber": "E1111111",
	    "insuranceChecked": true,
	    "active": false
	  },
	  {
	    "id": 3,
	    "firstName": "Bryce",
	    "lastName": "Hairabedian",
	    "dlNumber": "E2222222",
	    "insuranceChecked": true,
	    "active": true
	  },
	  {
	    "id": 4,
	    "firstName": "Edward",
	    "lastName": "Ozeruga",
	    "dlNumber": "E3333333",
	    "insuranceChecked": true,
	    "active": false
	  },
	  {
	    "id": 5,
	    "firstName": "Justin",
	    "lastName": "Mendiguarin",
	    "dlNumber": "E4444444",
	    "insuranceChecked": true,
	    "active": true
	  },
	  {
	    "id": 6,
	    "firstName": "Nik",
	    "lastName": "Sorvari",
	    "dlNumber": "E5555555",
	    "insuranceChecked": true,
	    "active": false
	  }
	]
```

See the [DriverController](https://github.com/shan916/safe-rides/blob/dev/safe-rides-api/src/main/java/edu/csus/asi/saferides/service/DriverController.java) class for the rest of the HTTP methods.
