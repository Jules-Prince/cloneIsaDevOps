package fr.univcotedazur.multifidelity.controllers;

import fr.univcotedazur.multifidelity.controllers.dto_in.AdvantageDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.AdvantageDtoOut;
import fr.univcotedazur.multifidelity.controllers.dto_out.SelectedAdvantageDtoOut;
import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.error.ErrorDto;
import fr.univcotedazur.multifidelity.interfaces.AdvantageGetter;
import fr.univcotedazur.multifidelity.interfaces.AdvantageUsage;
import fr.univcotedazur.multifidelity.interfaces.CatalogModifier;
import fr.univcotedazur.multifidelity.interfaces.ConsumerGetter;
import fr.univcotedazur.multifidelity.interfaces.SelectedAdvantageGetter;
import fr.univcotedazur.multifidelity.interfaces.SocietyGetter;
import fr.univcotedazur.multifidelity.modelMapper.ModelMapperAdvantage;
import fr.univcotedazur.multifidelity.modelMapper.ModelMapperSelectedAdvantage;
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

import static fr.univcotedazur.multifidelity.constant.RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = BASE_ADVANTAGE_ROUTE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "Advantage Controller", description = "Controller handling Advantages")
public class AdvantageController {


    @Value("${back.url}")
    private String backUrl;


    @Autowired
    private CatalogModifier catalogModifier;

    @Autowired
    private AdvantageGetter advantageGetter;


    @Autowired
    private SelectedAdvantageGetter selectedAdvantageGetter;

    @Autowired
    private ModelMapperAdvantage modelMapperAdvantage;

    @Autowired
    private ModelMapperSelectedAdvantage modelMapperSelectedAdvantage;
    @Autowired
    private SocietyGetter societyGetter;

    @Autowired
    private ConsumerGetter consumerGetter;

    @Autowired
    private AdvantageUsage advantageUsage;
    Logger log = Logger.getLogger(AdvantageController.class.getName());


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDto handleExceptions(MethodArgumentNotValidException e) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError("Cannot process Partner information because the request is not valid");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }



    @Operation(summary = "Add an advantage to the catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Advantage added successfully"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
            @ApiResponse(responseCode = "404", description = "The society of the advantage was not found or a similar advantage already exists in this society"),
    })
    @PostMapping( consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdvantageDtoOut> createAdvantage(@RequestBody @Valid AdvantageDtoIn advantageDtoIn)  {
        log.info("add advantage to the catalog");
        Advantage newAdvantage = catalogModifier.createAdvantage(modelMapperAdvantage.convertDtoInToAdvantage(advantageDtoIn,societyGetter));
        URI uri = URI.create(String.format("%s%s/%s",backUrl,BASE_ADVANTAGE_ROUTE,newAdvantage.getId()));
        return ResponseEntity.created(uri).body(modelMapperAdvantage.convertAdvantageToDtoOut(newAdvantage));
    }

    @Operation(summary = "the partner delete an advantage to the catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Advantage deleted successfully"),
            @ApiResponse(responseCode = "404", description = "The advantage with given id was not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @DeleteMapping(path = "/{advantageId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAdvantage(@PathVariable Long advantageId) {
        log.info(String.format("deletion of a advantage with the id : %s",advantageId));
        Advantage advantage = advantageGetter.getAdvantageById(advantageId);
        catalogModifier.deleteAdvantage(advantage);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "update an advantage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Advantage updated successfully"),
            @ApiResponse(responseCode = "404", description = "The advantage with given id is not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @PutMapping(path ="/{advantageId}",consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdvantageDtoOut> updateAdvantage(@PathVariable Long advantageId, @RequestBody @Valid AdvantageDtoIn advantageDtoIn) {
        log.info(String.format("modification of a advantage with the id : %s",advantageId));
        Advantage advantage = advantageGetter.getAdvantageById(advantageId);
        return ResponseEntity.ok(modelMapperAdvantage.convertAdvantageToDtoOut(catalogModifier.updateAdvantage(advantage,modelMapperAdvantage.convertDtoInToAdvantage(advantageDtoIn,societyGetter))));
    }

    @Operation(summary = "Get all advantages from a society by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Advantages successfully returned for a society"),
            @ApiResponse(responseCode = "404", description = "society with given id not found"),
    })
    @GetMapping(path = "/society/{societyId}", produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<List<AdvantageDtoOut>> getAllAdvantagesBySociety(@PathVariable Long societyId) {
        log.info(String.format("get all advantages from a society by his id : %s",societyId));
        Society society = societyGetter.getSocietyById(societyId);
        return ResponseEntity.ok(advantageGetter.getAllAdvantagesBySociety(society).stream().map(modelMapperAdvantage::convertAdvantageToDtoOut).collect(Collectors.toList()));
    }

    @Operation(summary = "Get all advantages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Advantages successfully returned"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdvantageDtoOut>> getAllAdvantages() {
        log.info("get all advantages");
        return ResponseEntity.ok(advantageGetter.getAllAdvantages().stream().map(modelMapperAdvantage::convertAdvantageToDtoOut).collect(Collectors.toList()));
    }

    @Operation(summary = "Get all advantage available for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Advantages successfully returned"),
            @ApiResponse(responseCode = "404", description = "Consumer with given name not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(path = "/consumer/{consumerId}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdvantageDtoOut>> getAllAdvantageEligible(@PathVariable Long consumerId) {
        log.info(String.format("get all advantage available for a customer with the id : %s",consumerId));
        Consumer consumer = consumerGetter.getConsumerById(consumerId);
        return ResponseEntity.ok(advantageGetter.getAllAdvantageEligibleByConsumer((consumer)).stream().map(modelMapperAdvantage::convertAdvantageToDtoOut).collect(Collectors.toList()));
    }

    @Operation(summary = "Get all selected advantage available for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Advantages successfully returned"),
            @ApiResponse(responseCode = "404", description = "Consumer with given name not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(path = "selected/consumer/{consumerId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SelectedAdvantageDtoOut>> getAllSelectedAdvantageEligible(@PathVariable Long consumerId) {
        log.info(String.format("get all advantage available for a customer with the id : %s",consumerId));
        Consumer consumer = consumerGetter.getConsumerById(consumerId);
        return ResponseEntity.ok(selectedAdvantageGetter.getAllSelectedAdvantageEligibleByConsumer((consumer)).stream().map(modelMapperSelectedAdvantage::convertSelectedAdvantageToDto).collect(Collectors.toList()));
    }

    @Operation(summary = "consumer select an advantage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "creation of a successful advantage selection"),
            @ApiResponse(responseCode = "404", description = "The consumer or the advantage is not found"),
            @ApiResponse(responseCode = "401", description = "you don't have any points"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path ="/{consumerId}/select-advantage", consumes = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public SelectedAdvantageDtoOut selectAdvantage(@RequestBody Long advantageId, @PathVariable Long consumerId) {
        Consumer consumer = consumerGetter.getConsumerById(consumerId);
        Advantage advantage = advantageGetter.getAdvantageById(advantageId);
        return modelMapperSelectedAdvantage.convertSelectedAdvantageToDto(advantageUsage.selectAdvantage(advantage,consumer));
    }


    @Operation(summary = "use of an advantage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful"),
            @ApiResponse(responseCode = "404", description = "No selected advantage found for the given id"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
            @ApiResponse(responseCode = "405", description = "expire"),
            @ApiResponse(responseCode = "502", description = "IWYPLS refused start session "),
    })
    @PutMapping(path = "/use-advantage",consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<SelectedAdvantageDtoOut> useAdvantage(@RequestBody Long selectedAdvantageId) {
        log.info(String.format("modification of selected advantage to RETRY state with id: %s",selectedAdvantageId));
        SelectedAdvantage selectedAdvantage = selectedAdvantageGetter.getSelectedAdvantageById(selectedAdvantageId);
        return ResponseEntity.ok(modelMapperSelectedAdvantage.convertSelectedAdvantageToDto(advantageUsage.useAdvantage(selectedAdvantage)));
    }

    @Operation(summary = "get selected advantage by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful"),
            @ApiResponse(responseCode = "404", description = "No selected advantage found for the given id"),
    })
    @GetMapping(path = "/selected/{selectedAdvantageId}",produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<SelectedAdvantageDtoOut> getSelectedAdvantageById(@PathVariable Long selectedAdvantageId) {
        log.info(String.format("get selected advantage with id : %s",selectedAdvantageId));
        SelectedAdvantage selectedAdvantage = selectedAdvantageGetter.getSelectedAdvantageById(selectedAdvantageId);
        return ResponseEntity.ok(modelMapperSelectedAdvantage.convertSelectedAdvantageToDto(selectedAdvantage));
    }
}
