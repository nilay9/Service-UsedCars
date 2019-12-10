package com.usedCars.ServiceUsedCars.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.usedCars.ServiceUsedCars.dao.UsedCarDao;
import com.usedCars.ServiceUsedCars.exception.FileStorageException;
import com.usedCars.ServiceUsedCars.exception.MyFileNotFoundException;
import com.usedCars.ServiceUsedCars.pojo.FileStorageProperties;
import com.usedCars.ServiceUsedCars.pojo.UsedCarDetails;

@Service
public class FileStorageService {
	 private Path fileStorageLocation = null;
	 	
	 @Autowired
	 UsedCarDao usedCarDao;
	 
	 @Autowired
	 UsedCarService usedCarService;

	    @Autowired
	    public FileStorageService(FileStorageProperties fileStorageProperties) {
	        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
	                .toAbsolutePath().normalize();

	        try {
	            Files.createDirectories(this.fileStorageLocation);
	        } catch (Exception ex) {
	            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
	        }
	    }

	    public String storeFile(String id,MultipartFile file) {
	        // Normalize file name
	        String fullfileName = StringUtils.cleanPath(file.getOriginalFilename());
	        String newname[] = fullfileName.split("\\.");
	        String fileName = newname[0];
	        
	        try {
	            // Check if the file's name contains invalid characters
	            if(fileName.contains("..")) {
	                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
	            }
	            
	            Optional<UsedCarDetails> cardetail = usedCarService.findUsedCarById(id);
	            UsedCarDetails carupdatedetail= new UsedCarDetails();
	            String newcarimagename = null;
	            if(cardetail.isPresent()) {
	            	 newcarimagename = cardetail.get().getCar_id()+"_"+cardetail.get().getCar_name();
	            } else {
		        	System.out.println("not");
		        }
	            
	           
	            System.out.println(newcarimagename);
	            List<UsedCarDetails> imagelist = usedCarDao.findByUsedcarimagesContains(newcarimagename);
	            System.out.println(imagelist.size());
	            List<String> updatedlist = new ArrayList<String>();
	            if(imagelist.size() == 0) {
	            	fileName = newcarimagename+"-1"+"."+newname[1];
	            	updatedlist.add(fileName);
	            	System.out.println("oii"+fileName);
	            }else {
	            	UsedCarDetails details = (UsedCarDetails) imagelist.get(0);
	            	String name = details.getUsedcarimages().get(details.getUsedcarimages().size() -1);
	            	System.out.println(name);
	            	String namearr[] = name.split("-");
	            	String namearr1[] = namearr[1].split("\\.");
            		int listcountnumber = Integer.parseInt(namearr1[0]);
            		fileName = newcarimagename +"-"+(listcountnumber+1)+"."+newname[1];
            		System.out.println("osc"+fileName);
            		updatedlist.addAll(details.getUsedcarimages());
            		updatedlist.add(fileName);             
	               	            	           	
	            }
	            carupdatedetail = cardetail.get();
                carupdatedetail.setUsedcarimages(updatedlist);
                usedCarDao.save(carupdatedetail);
	            
	            // Copy file to the target location (Replacing existing file with the same name)
	            Path targetLocation = this.fileStorageLocation.resolve(fileName);
	            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	            
	            return fileName;
	        } catch (IOException ex) {
	            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
	        }
	    }

	    public Resource loadFileAsResource(String fileName) {
	        try {
	            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
	            Resource resource = new UrlResource(filePath.toUri());
	            if(resource.exists()) {
	                return resource;
	            } else {
	                throw new MyFileNotFoundException("File not found " + fileName);
	            }
	        } catch (MalformedURLException ex) {
	            throw new MyFileNotFoundException("File not found " + fileName, ex);
	        }
	    }

		
}
