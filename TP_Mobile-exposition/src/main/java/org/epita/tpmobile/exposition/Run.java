package org.epita.tpmobile.exposition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.epita.tpmobile"})
public class Run {

	public static void main(String[] args) {
		SpringApplication.run(Run.class, args);

	}

}
