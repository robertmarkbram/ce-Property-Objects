package org.rmb.propertyobjects;

import lombok.extern.slf4j.Slf4j;
import org.rmb.propertyobjects.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CodingExercisePropertyObjectsApplication implements CommandLineRunner {

   private ApplicationProperties applicationProperties;

   @Autowired
   public CodingExercisePropertyObjectsApplication(
         final ApplicationProperties applicationProperties) {
      this.applicationProperties = applicationProperties;
   }

   public static void main(String[] args) {
      SpringApplication.run(CodingExercisePropertyObjectsApplication.class, args).close();
   }

   @java.lang.Override
   public void run(final java.lang.String... args) throws Exception {
      log.info("Map of brands");
      applicationProperties.getMapOfBrands().forEach((key, brand) -> {
         log.info("   Key: {}, value: {}", key, brand);
      });
      log.info("List of brands");
      applicationProperties.getListOfBrands().forEach((brand) -> {
         log.info("   Brand: {}", brand);
      });
   }
}
