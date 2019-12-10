package com.usedCars.ServiceUsedCars.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usedCars.ServiceUsedCars.dao.UsedCarDao;
import com.usedCars.ServiceUsedCars.pojo.UsedCarDetails;

@Service
public class UsedCarServiceImpl implements UsedCarService{
	
	@Autowired
	UsedCarDao usedCarDao;

	@Override
	public UsedCarDetails storeUsedCar(UsedCarDetails usedCarDetails) {
		usedCarDetails.setCar_id(null);
		usedCarDetails.setUsedcarimages(null);
		long count = usedCarDao.count();
		usedCarDetails.setCar_id(String.valueOf(count++));
		return usedCarDao.save(usedCarDetails);
	}

	@Override
	public Optional<UsedCarDetails> findUsedCarById(String id) {
		return usedCarDao.findById(id);
	}

}
