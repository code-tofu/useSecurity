package tofu.code.useSecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UseSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(UseSecurityApplication.class, args);
	}

	//TODO: V2 - Fix/Test response.sendError
	//TODO: V3 - Implement Roles
	//TODO: V4 - IMPLEMENT AuthenticationFilter
	//TODO: V5 - TRY USING auth0 library
	//TODO: V6 - TRY roles in separate table

}
