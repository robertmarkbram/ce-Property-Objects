# Reading Objects from Property files

Source available in this [Github repo](https://github.com/robertmarkbram/ce-Property-Objects).

## Properties File

Properties file - `yml` in this case. It represents a hierarchy of values that we want to read into an object.

```yml
application:
  map-of-brands:
    first:
      id: 012
      name: Hopes and Dreams
      date-created: "2020-12-04T11:41:00.000+10:00"
    second:
      id: 013
      name: Space and Rainbows
      date-created: "2019-10-14T10:41:12.000+10:00"
  list-of-brands:
    -
      id: 014
      name: Pancakes and Snowglobles
      date-created: "2010-11-01T01:43:00.000+10:00"
    -
      id: 020
      name: Green and Read
      date-created: "1984-02-15T11:11:20.000+10:00"
```

## Domain Class

The object:

```java
package org.rmb.propertyobjects.domain;

import lombok.Data;

import java.time.ZonedDateTime;

/**
 * An object we are going to read from a properties file.
 */
@Data
public class Brand {

   private Long id;

   private String name;

   private ZonedDateTime dateCreated;
}
```

## Application Properties Class

This is being read as part of application properties - which has a list and a map of these. I am using Lombok to avoid all the getter/setter boilerplate. It would be nicer to use [records](https://blogs.oracle.com/javamagazine/records-come-to-java) for these instead, but Spring doesn't know how to bind properties to records yet.

```java
package org.rmb.propertyobjects.config;

import lombok.Data;
import org.rmb.propertyobjects.domain.Brand;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationProperties {
   private Map<String, Brand> mapOfBrands;
   private List<Brand> listOfBrands;
}
```

## Date Converter

To convert a property to a date, you need a converter.

```java
package org.rmb.propertyobjects.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@ConfigurationPropertiesBinding
public class DateConverter implements Converter<String, ZonedDateTime> {

   @Override
   public ZonedDateTime convert(String source) {
      if (source == null) {
         return null;
      }
      return ZonedDateTime.parse(source);
   }
}
```

## Gradle Build File

Gradle config.

```gradle
plugins {
    id 'org.springframework.boot' version '2.4.0'
    id 'io.spring.dependency-management' version '1.0.10.RELEASE'
    id 'java'
}

group = 'org.rmb'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '15'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    compileOnly 'org.projectlombok:lombok:1.18.12'
    annotationProcessor 'org.projectlombok:lombok'
    annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
}

test {
    useJUnitPlatform()
}
```

## Spring Boot Class

Spring Boot class.

```java
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
```

Output from running this (modified to remove logging chaff):

```
Map of brands
   Key: first, value: Brand(id=10, name=Hopes and Dreams, dateCreated=2020-12-04T11:41+10:00)
   Key: second, value: Brand(id=11, name=Space and Rainbows, dateCreated=2019-10-14T10:41:12+10:00)
List of brands
   Brand: Brand(id=12, name=Pancakes and Snowglobles, dateCreated=2010-11-01T01:43+10:00)
   Brand: Brand(id=16, name=Green and Read, dateCreated=1984-02-15T11:11:20+10:00)
```