package fr.univcotedazur.multifidelity.controllers;

import fr.univcotedazur.multifidelity.controllers.dto_out.CardDtoOut;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.error.ErrorDto;
import fr.univcotedazur.multifidelity.interfaces.CardClientService;
import fr.univcotedazur.multifidelity.interfaces.CardGetter;
import fr.univcotedazur.multifidelity.interfaces.ConsumerGetter;
import fr.univcotedazur.multifidelity.modelMapper.ModelMapperCard;
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

import java.net.URI;
import java.util.logging.Logger;

import static fr.univcotedazur.multifidelity.constant.RouteConstants.CardRoutes.BASE_CARD_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = BASE_CARD_ROUTE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "Card Controller", description = "Controller handling cards")
public class CardController {

    @Value("${back.url}")
    private String backUrl;

    @Autowired
    private ConsumerGetter consumerGetter;

    @Autowired
    private CardClientService cardClientService;


    @Autowired
    private CardGetter cardGetter;

    @Autowired
    private ModelMapperCard modelMapperCard;

    Logger log = Logger.getLogger(CardController.class.getName());

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDto handleExceptions(MethodArgumentNotValidException e) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError("Cannot process Consumer information because the request is not valid");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }


    @Operation(summary = "adding money to loyalty card balance of a consumer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "reload balance successfully"),
            @ApiResponse(responseCode = "404", description = "The consumer or the card is not found"),
            @ApiResponse(responseCode = "401", description = "The bank refused the payment or The authorized balance of the card will be exceeded by this additional balance   "),
            @ApiResponse(responseCode = "422", description = "The request is not valid"),
            @ApiResponse(responseCode = "502", description = "probleme of connection with the bank"),
    })
    @PutMapping(path ="/{consumerId}/balance", consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<Float> reloadBalance(@RequestBody float amount, @PathVariable Long consumerId) {
        log.info("adding money to loyalty card balance of a consumer");
        Consumer consumer = consumerGetter.getConsumerById(consumerId);
        return ResponseEntity.ok(cardClientService.reloadBalance(amount,consumer));
    }


    @Operation(summary = "get the balance of loyalty card of a consumer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get balance successfully"),
            @ApiResponse(responseCode = "404", description = "The consumer or the card is not found"),
    })
    @GetMapping(path ="/{consumerId}/balance",produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<Float> getBalance(@PathVariable Long consumerId) {
        log.info("get money to loyalty card balance of a consumer");
        Consumer consumer = consumerGetter.getConsumerById(consumerId);
        return ResponseEntity.ok(cardClientService.getBalance(consumer));
    }

    @Operation(summary = "a consumer create his a fidelity card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "card create successfully"),
            @ApiResponse(responseCode = "404", description = "The consumer is not found"),
            @ApiResponse(responseCode = "404", description = "The card is already retry"),
            @ApiResponse(responseCode = "404", description = "The consumer have already a card"),
    })
    @PostMapping(path ="/{consumerId}/card",consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<CardDtoOut> createCard(@PathVariable Long consumerId) {
        log.info("a fidelity card is created for the consumer with id : "+consumerId+"");
        Consumer consumer = consumerGetter.getConsumerById(consumerId);
        Card newCard = cardClientService.createCard(consumer);
        URI uri = URI.create(String.format("%s%s/%s",backUrl,BASE_CARD_ROUTE,newCard.getId()));
        return ResponseEntity.created(uri).body(modelMapperCard.convertCardToDto(newCard));
    }

    @Operation(summary = "Get all points by consumer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Points successfully fetched"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
            @ApiResponse(responseCode = "404", description = "consumer not found for given id or card not found for this consumer"),
    })
    @GetMapping(path = "/{consumerId}/points",produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<Integer> getPointsByConsumer(@PathVariable Long consumerId){
        log.info("get points by consumer");
        Consumer consumer = consumerGetter.getConsumerById(consumerId);
        return ResponseEntity.ok(cardClientService.getPoint(consumer));
    }

    @Operation(summary = "get card by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful"),
            @ApiResponse(responseCode = "404", description = "No card found for the given id"),
    })
    @GetMapping(path = "/{cardId}",produces = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<CardDtoOut> getCardById(@PathVariable Long cardId) {
        log.info(String.format("get selected advantage with id : %s",cardId));
        Card card = cardGetter.getCardById(cardId);
        return ResponseEntity.ok(modelMapperCard.convertCardToDto(card));
    }
}
