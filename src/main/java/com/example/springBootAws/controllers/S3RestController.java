package com.example.springBootAws.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.springBootAws.ApplicationConfiguration;
import com.example.springBootAws.models.S3Object;
import com.example.springBootAws.services.AWSS3Service;

@RestController
@RequestMapping("/v1/s3")
public class S3RestController {

	/*
	@Value("${aws.aws_access_key_id}")
	private String awsAccessKeyId;
	
	@Value("${aws.aws_secret_access_key}")
	private String awsSecretAccessKey;
	
	@Value("${aws.region}")
	private String awsRegion;
	
	private static AWSCredentials credentials;
	*/	
	
 private Log log = LogFactory.getLog(getClass());

 private final ApplicationConfiguration applicationConfiguration;
 //@Autowired
 //private CustomerRepository customerRepository;
  
@Autowired
 public S3RestController(ApplicationConfiguration applicationConfiguration) { // <2>
  this.applicationConfiguration = applicationConfiguration;
 }

 // <1>
 @RequestMapping(method = RequestMethod.OPTIONS)
 ResponseEntity<?> options() {

  //@formatter:off
  return ResponseEntity
   .ok()
   .allow(HttpMethod.GET)
   .allow(HttpMethod.POST)
          .build();
   //@formatter:on
 }

 @GetMapping
 ResponseEntity<Collection<S3Object>> getCollection(@RequestParam(value = "folderName", required = true) String folderName) {
	 //log.info("At controller, active profile = " + this.applicationConfiguration.getProfileName());
	 //log.info("At controller, server port = " + this.applicationConfiguration.getServerPort());
	 //if (folderName == null) folderName = "default";
	 List<S3Object> dummyResult = new ArrayList<S3Object>();
	 /*
	 Customer dummy = null;
	 if ("peer1".equals(this.applicationConfiguration.getProfileName()))
		 dummy = new Customer(Long.parseLong("1"), "Tom C P", "Cheung");
	 else if ("peer2".equals(this.applicationConfiguration.getProfileName()))
		 dummy = new Customer(Long.parseLong("2"), "Evian Wong", "Cheung LOVES");

	 if ( dummy != null )
		 dummyResult.add(dummy);
	 */
	 
	 if (folderName == null) return ResponseEntity.ok(dummyResult);
		 
	 Collection<S3Object> data = getFromS3(folderName);
	 if (data != null) dummyResult.addAll(data);

	 return ResponseEntity.ok(dummyResult);
 }
 
 private Collection<S3Object> getFromS3(String folderName){ 
	 
	 /*
	 synchronized(this){
	    if (credentials == null) {
	    	//put your accesskey and secretkey here. Must be AWS IAM user, cannot assume role
	    	//For OpenShift, pass credential by Secret, then inject by env variable into Pod 
	        credentials = new BasicAWSCredentials(
	    		//s3.admin
	            //"<AWS accesskey>", 
	            awsAccessKeyId,
	            //"<AWS secretkey>"
	            awsSecretAccessKey
	        );
	    }
	 }
	 */
	 
	 //set-up the client
     AmazonS3 s3client = AmazonS3ClientBuilder
       .standard()
       .withCredentials(new DefaultAWSCredentialsProviderChain())
       //.withCredentials(new AWSStaticCredentialsProvider(credentials))
       //OpenPass is at eu-west-3 Europe (Paris) region. You OpenPass program here connect 
       //to Entity AWS resource account which is at different region (ap-southeast-1 SG)
       //So you need to specify the Entity AWS resource account region here.
       //.withRegion(Regions.AP_SOUTHEAST_1)
       //.withRegion(awsRegion)
       .build();
     
     AWSS3Service awsService = new AWSS3Service(s3client);

     List<S3Object> s3ObjectList = null;
     try {
    	 ObjectListing objectListing = awsService.listObjects(folderName);
    	 if (objectListing != null)
    		 s3ObjectList = new ArrayList<S3Object>();
    	 
    	 for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
             System.out.println(os.getKey());
             
			 S3Object s3Object = new S3Object(null, os.getKey(), os.getBucketName(), null, os.getSize(), null);
			 s3ObjectList.add(s3Object); 
    	 }
     } catch (Exception e) {
         System.err.println("Unable to retrieve data: ");
         System.err.println(e.getMessage());
     } finally {
    	 //if (client != null) client.shutdown();
    	 return s3ObjectList;
     }	 
 }
}
