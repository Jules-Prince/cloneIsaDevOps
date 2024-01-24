package fr.univcotedazur.multifidelity.controllers;


import fr.univcotedazur.multifidelity.controllers.dto_in.SocietyDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.SocietyDtoOut;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.error.ErrorDto;
import fr.univcotedazur.multifidelity.interfaces.PartnerGetter;
import fr.univcotedazur.multifidelity.interfaces.SocietyGetter;
import fr.univcotedazur.multifidelity.interfaces.SocietyModifier;
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

import static fr.univcotedazur.multifidelity.constant.RouteConstants.SocietyRoutes.BASE_SOCIETIES_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = BASE_SOCIETIES_ROUTE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "Society Controller", description = "Controller handling Society")
public class SocietyController {

    @Value("${back.url}")
    private String backUrl;

    @Autowired
    private SocietyModifier societyModifier;


    @Autowired
    private ModelMapperSociety modelMapperSociety;


    @Autowired
    private SocietyGetter societyGetter;


    @Autowired
    private PartnerGetter partnerGetter;

    Logger log = Logger.getLogger(SocietyController.class.getName());


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDto handleExceptions(MethodArgumentNotValidException e) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError("Cannot process Mayor information because the request is not valid");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }


    @Operation(summary = "Create a new society")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creation of the society successfully"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
            @ApiResponse(responseCode = "404", description = "partner not found with given id or this name is already in use by an another society"),
   })
    @PostMapping(consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SocietyDtoOut> createSociety(@RequestBody @Valid SocietyDtoIn societyDtoIn) {
        log.info("creation of a new society");
        Society newSociety = societyModifier.addSociety(modelMapperSociety.convertDtoInToSociety(societyDtoIn,partnerGetter));
        URI uri = URI.create(String.format("%s%s/%s",backUrl,BASE_SOCIETIES_ROUTE,newSociety.getId()));
        SocietyDtoOut societyDtoOut = modelMapperSociety.convertSocietyToDtoOut(newSociety);
        societyDtoOut.setPartnerId(newSociety.getPartner().getId());
        return ResponseEntity.created(uri).body(societyDtoOut);
    }


    @Operation(summary = "the partner update his society's informations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "society updated successfully"),
            @ApiResponse(responseCode = "404", description = "The society with given id was not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @PutMapping(path = "/{societyId}", consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SocietyDtoOut> updateSocietyInformation(@PathVariable Long societyId, @RequestBody @Valid SocietyDtoIn societyDtoIn) {
        log.info("update of information of society with id : " + societyId);
        Society oldSociety = societyGetter.getSocietyById(societyId);
        return ResponseEntity.ok(modelMapperSociety.convertSocietyToDtoOut(societyModifier.updateSocietyInformation(oldSociety,modelMapperSociety.convertDtoInToSociety(societyDtoIn,partnerGetter))));

    }

    @Operation(summary = "Get list of all societies of the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "societies successfully returned "),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SocietyDtoOut>> getAllSocieties() {
        log.info("get all societies");
        return ResponseEntity.ok(societyGetter.getAllSocieties().stream().map(modelMapperSociety::convertSocietyToDtoOut).collect(Collectors.toList()));
    }

    @Operation(summary = "Get society by his name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "society successfully returned "),
            @ApiResponse(responseCode = "404", description = "society with given name not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(path = "/name/{societyName}",produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<SocietyDtoOut> getSocietyByName(@PathVariable String societyName) {
        log.info("get society with name : " + societyName);
        return ResponseEntity.ok(modelMapperSociety.convertSocietyToDtoOut(societyGetter.getSocietyByName(societyName)));
    }


    @Operation(summary = "Get society by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "society successfully returned "),
            @ApiResponse(responseCode = "404", description = "society with given name not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(path = "/{societyId}",produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<SocietyDtoOut> getSocietyById(@PathVariable Long societyId) {
        log.info("get society with id : " + societyId);
        return ResponseEntity.ok(modelMapperSociety.convertSocietyToDtoOut(societyGetter.getSocietyById(societyId)));
    }



}
