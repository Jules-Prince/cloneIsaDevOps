package fr.univcotedazur.multifidelity.controllers;

import fr.univcotedazur.multifidelity.constant.RouteConstants;
import fr.univcotedazur.multifidelity.controllers.dto_in.SurveyAnswerDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_in.SurveyDtoIn;
import fr.univcotedazur.multifidelity.controllers.dto_out.SurveyAnswerDtoOut;
import fr.univcotedazur.multifidelity.controllers.dto_out.SurveyDtoOut;
import fr.univcotedazur.multifidelity.entities.Survey;
import fr.univcotedazur.multifidelity.exceptions.error.ErrorDto;
import fr.univcotedazur.multifidelity.interfaces.ConsumerGetter;
import fr.univcotedazur.multifidelity.interfaces.SatisfactionGetter;
import fr.univcotedazur.multifidelity.interfaces.SurveyAnswerer;
import fr.univcotedazur.multifidelity.interfaces.SurveyCreator;
import fr.univcotedazur.multifidelity.interfaces.SurveyGetter;
import fr.univcotedazur.multifidelity.modelMapper.ModelMapperSurvey;
import fr.univcotedazur.multifidelity.modelMapper.ModelMapperSurveyAnswer;
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

import static fr.univcotedazur.multifidelity.constant.RouteConstants.SocietyRoutes.BASE_SOCIETIES_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(path = RouteConstants.SurveyRoutes.BASE_SURVEY_ROUTE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "Survey Controller", description = "Controller handling surveys")
public class SurveyController {

    @Value("${back.url}")
    private String backUrl;

    @Autowired
    private ModelMapperSurvey modelMapperSurvey;

    @Autowired
    private ModelMapperSurveyAnswer modelMapperSurveyAnswer;

    @Autowired
    private ConsumerGetter consumerGetter;

    @Autowired
    private SurveyGetter surveyGetter;

    @Autowired
    private SurveyCreator surveyCreator;

    @Autowired
    private SatisfactionGetter satisfactionGetter;

    @Autowired
    private SurveyAnswerer surveyAnswerer;

    Logger log = Logger.getLogger(SurveyController.class.getName());


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDto handleExceptions(MethodArgumentNotValidException e) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError("Cannot process Survey information because the request is not valid");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }

    @Operation(summary = "Create a new survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creation of the survey successfully"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @PostMapping( consumes = APPLICATION_JSON_VALUE,produces =APPLICATION_JSON_VALUE ) // path is a REST CONTROLLER NAME
    public ResponseEntity<SurveyDtoOut> createSurvey(@RequestBody @Valid SurveyDtoIn surveyDtoIn) {
        log.info("creation of a survey");
        Survey newSurvey = surveyCreator.createSurvey(modelMapperSurvey.convertDtoInToSurvey(surveyDtoIn));
        URI uri = URI.create(String.format("%s%s/%s",backUrl,BASE_SOCIETIES_ROUTE,newSurvey.getId()));
        return ResponseEntity.created(uri).body(modelMapperSurvey.convertSurveyToDtoOut(newSurvey));
    }

    @Operation(summary = "get all the surveys of the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful recovery of candidates"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
    })
    @GetMapping(produces =APPLICATION_JSON_VALUE )
    public ResponseEntity<List<SurveyDtoOut>> getAllSurvey() {
        log.info("recovery of all surveys");
        return ResponseEntity.ok(surveyGetter.getAllSurvey().stream().map(modelMapperSurvey::convertSurveyToDtoOut)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "get a survey by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful recovery of candidates"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
            @ApiResponse(responseCode = "404", description = "No survey found for given id"),
    })
    @GetMapping(path = "/{id}",produces =APPLICATION_JSON_VALUE )
    public ResponseEntity<SurveyDtoOut> getSurveyById(@PathVariable Long id) {
        log.info("recovery of the survey with id : "+id.toString());
        return ResponseEntity.ok(modelMapperSurvey.convertSurveyToDtoOut(surveyGetter.getSurveyById(id)));
    }

    @Operation(summary = "get the satisfaction of a survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful recovery of candidates"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
            @ApiResponse(responseCode = "404", description = "No survey found for given id"),
    })
    @GetMapping(path = "/{id}/satisfaction", produces =APPLICATION_JSON_VALUE )
    public ResponseEntity<Float> getSatisfaction(@PathVariable Long id) {
        log.info("recovery of the satisfaction of the survey with id : "+id.toString());
        Survey survey = surveyGetter.getSurveyById(id);
        return ResponseEntity.ok(satisfactionGetter.getSatisfaction(survey));
    }

    @Operation(summary = "add an answer to a survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful recovery of candidates"),
            @ApiResponse(responseCode = "422", description = "the request is not valid"),
            @ApiResponse(responseCode = "404", description = "No survey found for given id"),
            @ApiResponse(responseCode = "400", description = "the answer to the questionnaire is not correct"),
    })
    @PostMapping (path = "{id}/answer", consumes = APPLICATION_JSON_VALUE,produces =APPLICATION_JSON_VALUE)
    public ResponseEntity<SurveyAnswerDtoOut> addAnswerToSurvey(@PathVariable Long id,@RequestBody @Valid SurveyAnswerDtoIn surveyAnswerDtoIn) {
        log.info("add an answer to the survey with id : "+id.toString());
        Survey survey = surveyGetter.getSurveyById(id);
        return ResponseEntity.ok(modelMapperSurveyAnswer.convertSurveyAnswerToDtoOut(surveyAnswerer.addAnswerToSurvey(survey,modelMapperSurveyAnswer.convertDtoInToSurveyAnswer(surveyAnswerDtoIn,consumerGetter))));
    }
}

