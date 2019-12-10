package com.usedCars.ServiceUsedCars.service;

import java.util.List;
import java.util.Optional;

import com.usedCars.ServiceUsedCars.pojo.UsedCarDetails;

public interface UsedCarService {

	UsedCarDetails storeUsedCar(UsedCarDetails usedCarDetails);
	
	Optional<UsedCarDetails> findUsedCarById(String id);
	
	

}
