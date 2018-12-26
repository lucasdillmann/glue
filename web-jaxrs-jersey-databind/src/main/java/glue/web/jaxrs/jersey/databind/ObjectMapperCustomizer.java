package glue.web.jaxrs.jersey.databind;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface ObjectMapperCustomizer {

    void customize(ObjectMapper objectMapper);
}
