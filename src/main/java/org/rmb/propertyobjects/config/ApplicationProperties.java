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
