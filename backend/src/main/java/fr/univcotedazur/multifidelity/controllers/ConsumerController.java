package fr.univcotedazur.multifidelity.controllers;

import fr.univcotedazur.multifidelity.controllers.dto_in.ConsumerDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.AdvantageDtoOut;
import fr.univcotedazur.multifidelity.controllers.dto_out.ConsumerDtoOut;
import fr.univcotedazur.multifidelity.controllers.dto_out.SelectedAdvantageDtoOut;
import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.error.ErrorDto;
import fr.univcotedazur.multifidelity.interfaces.ConsumerGetter;
import fr.univcotedazur.multifidelity.interfaces.ConsumerModifier;
import fr.univcotedazur.multifidelity.interfaces.RegistryConsumerProcessor;
import fr.univcotedazur.multifidelity.modelMapper.ModelMapperConsumer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static fr.univcotedazur.multifidelity.constant.RouteConstants.ConsumerRoutes.BASE_CONSUMER_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(path = BASE_CONSUMER_ROUTE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "Consumer Controller", description = "Controller handling consumers")
public class ConsumerController {

    @Value("${back.url}")
    private String backUrl;

    @Autowired
    private  RegistryConsumerProcessor registryConsumerProcessor;

    @Autowired
    private  ConsumerModifier consumerModifier;

    @Autowired
    private ConsumerGetter consumerGetter;

    @Autowired
    private ModelMapperConsumer modelMapperConsumer;


    Logger log = Logger.getLogger(ConsumerController.class.getName());


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDto handleExceptions(MethodArgumentNotValidException e) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError("Cannot process Consumer information because the request is not valid");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }


    @Operation(summary = "Create a new consumer with an email and a password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creation of the consumer successfully"),
            @ApiResponse(responseCode = "401", description = "this email is already in use by an another account "),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @PostMapping(path = "/sign-in", consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<ConsumerDtoOut> signIn(@RequestBody @Valid ConsumerDtoIn consumerDtoIn) {
        log.info("creation of a consumer account");
        Consumer newConsumer = registryConsumerProcessor.signIn(consumerDtoIn.getEmail(), consumerDtoIn.getPassword());
        URI uri = URI.create(String.format("%s%s/%s",backUrl,BASE_CONSUMER_ROUTE,newConsumer.getId()));
        return ResponseEntity.created(uri).body(modelMapperConsumer.convertConsumerToDto(newConsumer));
    }


    @Operation(summary = "LogIn of a existing consumer with his email and his password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LogIn of the consumer successfully"),
            @ApiResponse(responseCode = "404", description = "A consumer with this email and this password was not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @PostMapping(path = "/log-in", consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<ConsumerDtoOut> logIn(@RequestBody  ConsumerDtoIn consumerDtoIn) {
        log.info("connection of a consumer");
        return ResponseEntity.ok(modelMapperConsumer.convertConsumerToDto(registryConsumerProcessor.logIn(consumerDtoIn.getEmail(), consumerDtoIn.getPassword())));
    }

    @Operation(summary = "update a consumer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consumer updated successfully"),
            @ApiResponse(responseCode = "404", description = "The consumer with given id is not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @PutMapping(path ="/{consumerId}",consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsumerDtoOut> updateConsumer(@PathVariable Long consumerId, @RequestBody @Valid ConsumerDtoIn consumerDtoIn) {
        log.info(String.format("modification of a consumer with the id : %s",consumerId));
        Consumer consumer = consumerGetter.getConsumerById(consumerId);
        return ResponseEntity.ok(modelMapperConsumer.convertConsumerToDto(consumerModifier.updateConsumer(consumer,modelMapperConsumer.convertDtoInToConsumer(consumerDtoIn))));
    }

    @Operation(summary = "get a consumer by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful recovery of candidates"),
            @ApiResponse(responseCode = "404", description = "the ressource was not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(path ="/{consumerId}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsumerDtoOut> getConsumerById(@PathVariable Long consumerId) {
        log.info("get a consumer by his id");
        return ResponseEntity.ok(modelMapperConsumer.convertConsumerToDto(consumerGetter.getConsumerById(consumerId)));
    }

    @Operation(summary = "get all the consumers of the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful recovery of candidates"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConsumerDtoOut>> getAllConsumer() {
        log.info("get all the consumers");
        return ResponseEntity.ok(consumerGetter.getAllConsumer().stream().map(modelMapperConsumer::convertConsumerToDto)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "delete all the consumers of the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "successful recovery of candidates"),
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public void deleteAllConsumer() {
        consumerModifier.deleteAllConsumer();
    }

}

