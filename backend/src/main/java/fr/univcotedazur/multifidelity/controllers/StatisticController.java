package fr.univcotedazur.multifidelity.controllers;

import fr.univcotedazur.multifidelity.constant.RouteConstants;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.error.ErrorDto;
import fr.univcotedazur.multifidelity.interfaces.SocietyGetter;
import fr.univcotedazur.multifidelity.interfaces.StatisticGiver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = RouteConstants.StatisticService.BASE_STATISTICS_ROUTE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "StatisticsService Controller", description = "Controller handling statistics")
public class StatisticController {

    @Autowired
    private StatisticGiver statisticGiver;

    @Autowired
    private SocietyGetter societyGetter;

    Logger log = Logger.getLogger(StatisticController.class.getName());


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDto handleExceptions(MethodArgumentNotValidException e) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError("Cannot process statistic information because the request is not valid");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }

    @Operation(summary = "Get profit by society")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "statistics for a society successfully returned"),
            @ApiResponse(responseCode = "404", description = "society with given id was not found"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(path = "/profits/society/{societyId}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Float> getProfitBySocietyId(@PathVariable Long societyId) {
        log.info("Get profit of society with id " + societyId);
        Society society = societyGetter.getSocietyById(societyId);
        return ResponseEntity.ok(statisticGiver.getProfitBySociety(society));
    }

    @Operation(summary = "Get profit made by the platform in one city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "statistics successfully returned"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(path = "/profits",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Float> getProfit() {
        log.info("Get profit of the platform");
        return ResponseEntity.ok(statisticGiver.getProfit());
    }


    @Operation(summary = "Get frequency of purchase in a society per day over the last week")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "frequency successfully returned"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(path = "/frequency-purchase/societies/{societyId}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Float> getFrequencyPurchaseSociety(@PathVariable Long societyId) {
        log.info("Get frequency of purchase of society with id " + societyId);
        Society society = societyGetter.getSocietyById(societyId);
        return ResponseEntity.ok(statisticGiver.getFrequencyPurchaseBySociety(society));
    }

    @Operation(summary = "Get frequency of purchase in the platform per day over the last week")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "frequency successfully returned"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(path = "/frequency-purchase",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Float> getFrequencyPurchase() {
        log.info("Get frequency of purchase of the platform");
        return ResponseEntity.ok(statisticGiver.getFrequencyPurchase());
    }
}
