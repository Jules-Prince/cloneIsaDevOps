package fr.univcotedazur.multifidelity.controllers;

import fr.univcotedazur.multifidelity.controllers.dto_in.PartnerDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.PartnerDtoOut;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.interfaces.PartnerAccountManager;
import fr.univcotedazur.multifidelity.interfaces.PartnerGetter;
import fr.univcotedazur.multifidelity.modelMapper.ModelMapperPartner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static fr.univcotedazur.multifidelity.constant.RouteConstants.PartnerRoutes.BASE_PARTNER_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(path = BASE_PARTNER_ROUTE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "Partner Controller", description = "Controller handling partner")
public class PartnerController {


    @Value("${back.url}")
    private String backUrl;


    @Autowired
    private PartnerAccountManager partnerAccountManager;

    @Autowired
    private PartnerGetter partnerGetter;


    @Autowired
    private ModelMapperPartner modelMapperPartner;

    Logger log = Logger.getLogger(PartnerController.class.getName());



    @Operation(summary = "LogIn of an existing partner with his email and his password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LogIn of the partner successfully"),
            @ApiResponse(responseCode = "401", description = "A partner with this email and this password was not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @PostMapping(path = "/login", consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PartnerDtoOut> login(@RequestBody PartnerDtoIn partnerDtoIn) {
        return ResponseEntity.ok(modelMapperPartner.convertPartnerToDtoOut(partnerAccountManager.login(partnerDtoIn.getEmail(), partnerDtoIn.getPassword())));
    }


    @Operation(summary = "Create a new partner with an email and a password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creation of the partner successfully"),
            @ApiResponse(responseCode = "404", description = "this email or name is already in use by an another account "),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PartnerDtoOut> createPartnerAccount(@RequestBody @Valid PartnerDtoIn partnerDtoIn) {
        log.info("creation of a new partner");
        Partner newPartner =partnerAccountManager.addPartnerAccount(modelMapperPartner.convertDtoInToPartner(partnerDtoIn));
        URI uri = URI.create(String.format("%s%s/%s",backUrl,BASE_PARTNER_ROUTE,newPartner.getId()));
        return ResponseEntity.created(uri).body(modelMapperPartner.convertPartnerToDtoOut(newPartner));
    }

    @Operation(summary = "get all the partner of the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful recovery of partners"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping( produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PartnerDtoOut>> getAllPartner() {
        log.info("get all partners");
        return ResponseEntity.ok(partnerGetter.getAllPartners().stream().map(modelMapperPartner::convertPartnerToDtoOut)
                .collect(Collectors.toList()));
    }


    @Operation(summary = "Get partner by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "partner successfully returned "),
            @ApiResponse(responseCode = "404", description = "partner with given name not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(path = "/{id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PartnerDtoOut> getPartnerById(@PathVariable Long id) {
        log.info("get partner with id : "+id);
        return ResponseEntity.ok(modelMapperPartner.convertPartnerToDtoOut(partnerGetter.getPartnerById(id)));
    }


}

