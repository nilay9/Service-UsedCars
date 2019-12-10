package com.usedCars.ServiceUsedCars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

import com.usedCars.ServiceUsedCars.pojo.FileStorageProperties;

@SpringBootApplication
@EnableEurekaClient
//@ComponentScan(basePackages="com.usedCars.ServiceUsedCars.Controller")
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class ServiceUsedCarsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceUsedCarsApplication.class, args);

	}
	
	

}
