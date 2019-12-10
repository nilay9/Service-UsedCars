package com.usedCars.ServiceUsedCars.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.usedCars.ServiceUsedCars.dao.UsedCarDao;
import com.usedCars.ServiceUsedCars.pojo.UsedCarDetails;
import com.usedCars.ServiceUsedCars.pojo.UsedCarImages;
import com.usedCars.ServiceUsedCars.service.FileStorageService;
import com.usedCars.ServiceUsedCars.service.UsedCarService;

@RestController
@RequestMapping("/car")
public class UsedCarController {

	@Autowired
    private FileStorageService fileStorageService;
	
	@Autowired
	private UsedCarService usedCarService;
	
	@PostMapping("/uploadCar")
	public ResponseEntity<Map<String,String>> uploadFile(@RequestBody UsedCarDetails usedCarDetails) {
		UsedCarDetails cardetName = usedCarService.storeUsedCar(usedCarDetails);
		Map<String,String> newcar = new HashMap<String, String>();
		newcar.put("Car_ID", cardetName.getCar_id());
		newcar.put("Car_Name", cardetName.getCar_name());
		newcar.put("Car_Model", cardetName.getCar_model());
		newcar.put("Car_Built", cardetName.getCar_built());
		newcar.put("Car_Manufacturer_Year", cardetName.getCar_manufac_year());
		newcar.put("Upload_Car_images", "http://localhost:8080/car/uploadFile"+"/"+cardetName.getCar_id());
		return new ResponseEntity<Map<String,String>>(newcar, HttpStatus.OK);
    }
	    
    @PostMapping("/uploadFile/{id}")
    public UsedCarImages uploadFile(@PathVariable String id,@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(id,file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/car/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UsedCarImages(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles/{id}")
    public List<UsedCarImages> uploadMultipleFiles(@PathVariable String id,@RequestParam("files") MultipartFile[] files) throws IOException {
    	List<UsedCarImages> list = new ArrayList<UsedCarImages>();
    	
    	try {
	    	Arrays.asList(files).stream().forEach(file -> 
	        {
	        	 String fileName = fileStorageService.storeFile(id,file);
	            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	                    .path("/car/downloadFile/")
	                    .path(fileName)
	                    .toUriString();
	            UsedCarImages fileuploaddetails = new UsedCarImages(fileName, fileDownloadUri,
	                    file.getContentType(), file.getSize());
	            list.add(fileuploaddetails);
	            
	        });
    	} catch (Exception e) {
    		System.out.println(e);
			e.printStackTrace();
		}
    	return list;
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
