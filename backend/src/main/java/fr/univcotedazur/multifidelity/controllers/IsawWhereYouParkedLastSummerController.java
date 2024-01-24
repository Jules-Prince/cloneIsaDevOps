package fr.univcotedazur.multifidelity.controllers;


import fr.univcotedazur.multifidelity.connectors.externaldto.ParcDto;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.interfaces.AdvantageUsage;
import fr.univcotedazur.multifidelity.interfaces.ConsumerGetter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static fr.univcotedazur.multifidelity.constant.RouteConstants.ISWYPLSRoutes.BASE_ISWYPLS_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = BASE_ISWYPLS_ROUTE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "IsawWhereYouParkedLastSummer Controller", description = "Controller handling IsawWhereYouParkedLastSummer")
public class IsawWhereYouParkedLastSummerController {

    @Autowired
    private ConsumerGetter consumerGetter;

    @Autowired
    private AdvantageUsage advantageUsage;

    @Operation(summary = "Notify consumer and of parking time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "consumer notified"),
            @ApiResponse(responseCode = "404", description = "consumer not found"),
    })
    @PutMapping(consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> notifyConsumer(@RequestBody @Valid ParcDto parcDto) {
        Consumer consumer = consumerGetter.getConsumerByPlate(parcDto.getPlate());
        advantageUsage.terminateAdvantage(consumer,parcDto);
        return ResponseEntity.noContent().build();
    }
}
