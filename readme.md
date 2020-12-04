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
