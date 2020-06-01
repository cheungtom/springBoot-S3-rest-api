package com.example.springBootAws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Configuration
public class ApplicationConfiguration {

 private Log log = LogFactory.getLog(getClass());

 private String profileName;
 private String serverPort;

 @Bean
 static PropertySourcesPlaceholderConfigurer pspc() {
  return new PropertySourcesPlaceholderConfigurer();
 }

 // <1>
 @Configuration
 @Profile("peer1")
 public static class Peer1Configuration {

  @Bean
  InitializingBean init() {
   return () -> LogFactory.getLog(getClass()).info("peer1 InitializingBean");
  }
 }

 @Configuration
 @Profile({ "peer2" })
 // <2>
 public static class Peer2Configuration {

  @Bean
  InitializingBean init() {
   return () -> LogFactory.getLog(getClass()).info("peer2 InitializingBean");
  }
 }

 // <3>
 @Bean
 InitializingBean which(Environment e,
                        @Value("${server.port}") String serverPort) {
  this.profileName = StringUtils.arrayToCommaDelimitedString(e.getActiveProfiles());
  this.serverPort = serverPort;

   return () -> {
   log.info("activeProfiles: '" + profileName + "'");
   log.info("server.port: " + serverPort);
  };
 }

	public String getProfileName() {
		return profileName;
	}

	public String getServerPort() {
		return serverPort;
	}
}
