package org.nice.ogc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
public class DomaineApplication {

    public static void main(String[] args) {
        SpringApplication.run(DomaineApplication.class, args);
        System.out.println();
    }
}
