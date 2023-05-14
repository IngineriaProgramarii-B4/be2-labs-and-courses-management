package com.example.user.configurations;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateTimeFormatConfiguration {

    String format = "dd.MM.yyyy HH:mm";
    // configs for Jackson ser/deser
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {

        return builder -> {
            builder.simpleDateFormat(format);
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(format)));
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(format)));
        };
    }
}