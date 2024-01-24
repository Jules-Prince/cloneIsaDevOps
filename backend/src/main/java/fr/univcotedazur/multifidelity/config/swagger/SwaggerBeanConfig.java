package fr.univcotedazur.multifidelity.config.swagger;

import fr.univcotedazur.multifidelity.exceptions.error.ApiError;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;


@Configuration
public class SwaggerBeanConfig {


    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("api")
                .addOpenApiCustomiser(this::addDefaultErrorResponses)
                .build();
    }


    private void addDefaultErrorResponses(OpenAPI openApi) {
        String errorResponse = ApiError.class.getSimpleName();
        Schema errorSchema = new Schema();
        errorSchema.setName(errorResponse);
        errorSchema.set$ref(String.format("#/components/schemas/%s" , errorResponse));

        openApi.getComponents().getSchemas().putAll(ModelConverters.getInstance().read(ApiError.class));
        openApi.getPaths().values().forEach(
                pathItem -> pathItem.readOperations().forEach(
                        operation -> {
                            ApiResponses apiResponses = operation.getResponses();
                            apiResponses.forEach(
                                    (s, apiResponse) -> {
                                        if (HttpStatus.valueOf(Integer.parseInt(s)).isError()) {
                                            addContent(apiResponse, errorSchema);
                                        }
                                    }
                            );
                        }
                )
        );
    }

    private ApiResponse addContent(ApiResponse apiResponse, Schema schema) {
        MediaType mediaType = new MediaType();
        mediaType.schema(schema);
        return apiResponse.content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType));
    }
}
