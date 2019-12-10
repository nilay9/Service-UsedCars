package com.usedCars.ServiceUsedCars.pojo;

import java.util.ArrayList;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;


@Document
public class UsedCarDetails {
	@Id
	private String car_id;
	@Field
	@NotNull
	private String car_name;
	@Field
	@NotNull
	private String car_model;
	@Field
	@NotNull
	private String car_built;
	@Field
	@NotNull
	private String car_manufac_year;
	@Field
	private List<String> usedcarimages = new ArrayList<>();
	
	
	
	public UsedCarDetails() {}
	
	public UsedCarDetails(String car_id, String car_name, String car_model, String car_built, String car_manufac_year,
			List<String> usedcarimages) {
		this.car_id = car_id;
		this.car_name = car_name;
		this.car_model = car_model;
		this.car_built = car_built;
		this.car_manufac_year = car_manufac_year;
		this.usedcarimages = usedcarimages;
	}
	
	public String getCar_id() {
		return car_id;
	}
	public void setCar_id(String car_id) {
		this.car_id = car_id;
	}
	public String getCar_name() {
		return car_name;
	}
	public void setCar_name(String car_name) {
		this.car_name = car_name;
	}
	public String getCar_model() {
		return car_model;
	}
	public void setCar_model(String car_model) {
		this.car_model = car_model;
	}
	public String getCar_built() {
		return car_built;
	}
	public void setCar_built(String car_built) {
		this.car_built = car_built;
	}
	public String getCar_manufac_year() {
		return car_manufac_year;
	}
	public void setCar_manufac_year(String car_manufac_year) {
		this.car_manufac_year = car_manufac_year;
	}

	public List<String> getUsedcarimages() {
		return usedcarimages;
	}

	public void setUsedcarimages(List<String> usedcarimages) {
		this.usedcarimages = usedcarimages;
	}
	
	
}
