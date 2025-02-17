package com.example.springsecurityexamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringSecurityExamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityExamplesApplication.class, args);
	}

	@RestController
	@RequestMapping("/default")
    static class Controller implements Resource {

		@GetMapping
		@Override
		public String hello() {
			return "I'm different";
		}
	}

	@RestController

    static class DefaultController implements Resource {
	}

	@RestController
	@RequestMapping("/user")
    static class UserController implements Resource {

		/**
		 * http://localhost:8080/user/ should work with user:password
		 */
		@GetMapping("/")
		@PreAuthorize("hasRole('USER')")
		@Override
		public String hello() {
			return "Hello user";
		}
	}


	@RequestMapping("/admin")
	interface Resource {

		@GetMapping("/")
		@PreAuthorize("hasRole('ADMIN')")
		default String hello() {
			return "Shouldn't get me";
		}
	}

}
