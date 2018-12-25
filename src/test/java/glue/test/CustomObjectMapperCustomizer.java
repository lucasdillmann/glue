package glue.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import glue.web.jaxrs.jersey.databind.ObjectMapperCustomizer;

import javax.enterprise.inject.Default;

@Default
public class CustomObjectMapperCustomizer implements ObjectMapperCustomizer {

    @Override
    public void customize(ObjectMapper objectMapper) {
        objectMapper
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .configure(SerializationFeature.INDENT_OUTPUT, true);
    }
}
