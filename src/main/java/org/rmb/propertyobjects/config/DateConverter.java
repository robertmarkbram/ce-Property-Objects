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
