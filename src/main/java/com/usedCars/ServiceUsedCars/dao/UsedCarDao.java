package com.usedCars.ServiceUsedCars.dao;

import java.util.List;

import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.usedCars.ServiceUsedCars.pojo.UsedCarDetails;

@Repository
@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "usedCarDetails")
public interface UsedCarDao  extends CouchbasePagingAndSortingRepository<UsedCarDetails, String>{

	@Query("Select usedcarimages,META().id AS _ID, META().cas AS _CAS from #{#n1ql.bucket} where #{#n1ql.filter} AND ANY imagename IN usedcarimages SATISFIES imagename Like '%' || $1 || '%' END")
	List<UsedCarDetails> findByUsedcarimagesContains(String fileName);

}
