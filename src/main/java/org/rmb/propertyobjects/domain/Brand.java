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
