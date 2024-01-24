package fr.univcotedazur.multifidelity;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@OpenAPIDefinition(info = @Info(title = "multi fidelity API", version = "1.0"))
@EnableAspectJAutoProxy
@SpringBootApplication //(exclude = {DataSourceAutoConfiguration.class })
public class MultiFidelityServer {

    public static void main(String[] args) {
        SpringApplication.run(MultiFidelityServer.class, args);
    }

}
