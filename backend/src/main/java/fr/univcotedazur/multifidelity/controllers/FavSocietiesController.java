package fr.univcotedazur.multifidelity.controllers;

import fr.univcotedazur.multifidelity.controllers.dto_out.ConsumerDtoOut;
import fr.univcotedazur.multifidelity.controllers.dto_out.SocietyDtoOut;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.error.ErrorDto;
import fr.univcotedazur.multifidelity.interfaces.ConsumerGetter;
import fr.univcotedazur.multifidelity.interfaces.ConsumerModifier;
import fr.univcotedazur.multifidelity.interfaces.SocietyGetter;
import fr.univcotedazur.multifidelity.modelMapper.ModelMapperConsumer;
import fr.univcotedazur.multifidelity.modelMapper.ModelMapperSociety;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static fr.univcotedazur.multifidelity.constant.RouteConstants.FavSocietiesRoutes.BASE_FAV_SOCIETIES_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = BASE_FAV_SOCIETIES_ROUTE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "Fav Societies Controller", description = "Controller handling Fav Societies")
public class FavSocietiesController {


    @Value("${back.url}")
    private String backUrl;

    @Autowired
    private SocietyGetter societyGetter;


    @Autowired
    private ConsumerGetter  consumerGetter;

    @Autowired
    private ConsumerModifier consumerModifier;

    @Autowired
    private ModelMapperConsumer modelMapperConsumer;

    @Autowired
    private ModelMapperSociety modelMapperSociety;

    Logger log = Logger.getLogger(FavSocietiesController.class.getName());

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDto handleExceptions(MethodArgumentNotValidException e) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError("Cannot process Consumer information because the request is not valid");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }


    @Operation(summary = "Get all fav societies from a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "societies successfully returned "),
            @ApiResponse(responseCode = "404", description = "Consumer with given name not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(path = "/{consumerId}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SocietyDtoOut>> getFavSocieties(@PathVariable Long consumerId) {
        log.info(String.format("get all fav societies from a customer with id : %s",consumerId));
        Consumer consumer = consumerGetter.getConsumerById(consumerId);
        return ResponseEntity.ok(consumerGetter.getFavSocietiesByConsumer(consumer).stream().map(modelMapperSociety::convertSocietyToDtoOut).collect(Collectors.toList()));
    }


    @Operation(summary = "add fav society to a consumer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "society added to fav successfully"),
            @ApiResponse(responseCode = "404", description = "The consumer or the society is not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @PostMapping(path ="/{consumerId}",consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<ConsumerDtoOut> createFavSociety(@PathVariable Long consumerId, @RequestBody Long societyId) {
        log.info(String.format("add a society to the customer's favorites with id : %s",societyId));
        Consumer consumer = consumerGetter.getConsumerById(consumerId);
        Society society = societyGetter.getSocietyById(societyId);
        Consumer newConsumer =  consumerModifier.addFavSociety(society,consumer);
        URI uri = URI.create(String.format("%s%s/%s",backUrl,BASE_FAV_SOCIETIES_ROUTE,newConsumer.getId()));
        return ResponseEntity.created(uri).body(modelMapperConsumer.convertConsumerToDto(newConsumer));
    }

    @Operation(summary = "delete fav society to a consumer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "society deleted to fav successfully"),
            @ApiResponse(responseCode = "404", description = "The consumer or the society is not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @DeleteMapping(path ="/{consumerId}", consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<Void> deleteFavSociety(@PathVariable Long consumerId , @RequestBody Long societyId) {
        log.info(String.format("delete a society to the customer's favorites with id : %s",societyId));
        Consumer consumer = consumerGetter.getConsumerById(consumerId);
        Society society =  societyGetter.getSocietyById(societyId);
        consumerModifier.deleteFavSociety(society,consumer);
        return ResponseEntity.noContent().build();
    }

}
