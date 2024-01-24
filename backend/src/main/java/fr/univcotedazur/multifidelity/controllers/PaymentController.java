package fr.univcotedazur.multifidelity.controllers;

import fr.univcotedazur.multifidelity.controllers.dto_in.PurchaseDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.PurchaseDtoOut;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Purchase;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.exceptions.ResourceNotFoundException;
import fr.univcotedazur.multifidelity.exceptions.error.ErrorDto;
import fr.univcotedazur.multifidelity.interfaces.CardGetter;
import fr.univcotedazur.multifidelity.interfaces.ConsumerGetter;
import fr.univcotedazur.multifidelity.interfaces.Pay;
import fr.univcotedazur.multifidelity.interfaces.PurchaseGetter;
import fr.univcotedazur.multifidelity.interfaces.SocietyGetter;
import fr.univcotedazur.multifidelity.modelMapper.ModelMapperPurchase;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static fr.univcotedazur.multifidelity.constant.RouteConstants.PaymentRoutes.BASE_PAYMENT_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = BASE_PAYMENT_ROUTE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "payment Controller", description = "Controller handling payment")
public class PaymentController {


    @Value("${back.url}")
    private String backUrl;


    @Autowired
    private PurchaseGetter purchaseGetter;


    @Autowired
    private ConsumerGetter consumerGetter;


    @Autowired
    private SocietyGetter societyGetter;

    @Autowired
    private CardGetter cardGetter;

    @Autowired
    private Pay pay;

    @Autowired
    private ModelMapperPurchase modelMapperPurchase;

    Logger log = Logger.getLogger(PaymentController.class.getName());


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDto handleExceptions(MethodArgumentNotValidException e) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError("Cannot process payment information because the request is not valid");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }

    @Operation(summary = "Create a purchase pay with the fidelity card and a partner society")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Purchase successful created"),
            @ApiResponse(responseCode = "401", description = "Purchase could not be completed because the balance is too low "),
            @ApiResponse(responseCode = "404", description = "Card or society not found for gicen id "),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @PostMapping(path ="/with-card",consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PurchaseDtoOut> createPurchasePayWithCard(@RequestBody @Valid PurchaseDtoIn purchaseDtoIn)  {
        log.info("adding a purchase made with a loyalty card");
        Purchase newPurchase = pay.createPurchasePayWithCard(modelMapperPurchase.convertDtoInToPurchase(purchaseDtoIn,societyGetter,cardGetter));
        URI uri = URI.create(String.format("%s%s/%s",backUrl,BASE_PAYMENT_ROUTE,newPurchase.getId()));
        return ResponseEntity.created(uri).body(modelMapperPurchase.convertPurchaseToDtoOut(newPurchase));
    }

    @Operation(summary = "Create a purchase with the fidelity card and a partner society")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Purchase successful created"),
            @ApiResponse(responseCode = "404", description = "Card or society not found for given id "),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<PurchaseDtoOut> createPurchase(@RequestBody @Valid PurchaseDtoIn purchaseDtoIn) throws ResourceNotFoundException {
        log.info("adding a purchase");
        Purchase newPurchase = pay.createPurchase(modelMapperPurchase.convertDtoInToPurchase(purchaseDtoIn,societyGetter,cardGetter));
        URI uri = URI.create(String.format("%s%s/%s",backUrl,BASE_PAYMENT_ROUTE,newPurchase.getId()));
        return ResponseEntity.created(uri).body(modelMapperPurchase.convertPurchaseToDtoOut(newPurchase));
    }

    @Operation(summary = "Get all purchase by consumer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All purchases successfully fetched"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
            @ApiResponse(responseCode = "404", description = "consumer not found for given id "),
    })
    @GetMapping(path = "/consumer/{consumerId}",produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<List<PurchaseDtoOut>> getAllPurchasesByConsumer(@PathVariable Long consumerId){
        log.info("get all purchases  by consumer");
        Consumer consumer = consumerGetter.getConsumerById(consumerId);
        return ResponseEntity.ok(purchaseGetter.getAllPurchasesByConsumer(consumer).stream().map(modelMapperPurchase::convertPurchaseToDtoOut).collect(Collectors.toList()));
    }


    @Operation(summary = "Get all purchase by society")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All purchases successfully fetched"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
            @ApiResponse(responseCode = "404", description = "consumer not found for given id "),
    })
    @GetMapping(path = "/society/{societyId}",produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<List<PurchaseDtoOut>> getPurchasesBySociety(@PathVariable Long societyId){
        log.info("get all purchases  by society");
        Society society = societyGetter.getSocietyById(societyId);
        return ResponseEntity.ok(purchaseGetter.getAllPurchasesBySociety(society).stream().map(modelMapperPurchase::convertPurchaseToDtoOut).collect(Collectors.toList()));
    }

}