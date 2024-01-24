package fr.univcotedazur.multifidelity.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelity.connectors.externaldto.ParcDto;
import fr.univcotedazur.multifidelity.constant.RouteConstants;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.SelectedAdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
import fr.univcotedazur.multifidelity.repositories.ValidityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IsawWhereYouParkedLastSummerIT {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ConsumerRepository consumerRepository;


    @Autowired
    private AdvantageRepository advantageRepository;


    @Autowired
    private ValidityRepository validityRepository;


    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private SocietyRepository societyRepository;



    @Autowired
    private SelectedAdvantageRepository selectedAdvantageRepository;



    @BeforeEach
    public void setUp() {
        selectedAdvantageRepository.deleteAll();
        advantageRepository.deleteAll();
        validityRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        societyRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        selectedAdvantageRepository.deleteAll();
        advantageRepository.deleteAll();
        validityRepository.deleteAll();
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
        societyRepository.deleteAll();
    }

/*
    @Test
    public void validPartnerSignInTest() throws Exception {
        Society society = new Society();
        society = societyRepository.save(society);
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(10));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setId(1L);
        validity.setParc(true);
        validityRepository.save(validity);
        Advantage advantage = new Advantage("café offert", 12, false, 2.5F, society, 0);
        advantage.setValidity(validity);
        advantageRepository.save(advantage);
        Consumer consumer = new Consumer();
        consumer.setLicencePlate("plate");
        Card card = new Card();
        card.setPoint(20);
        card.setConsumer(consumer);
        cardRepository.save(card);
        consumer.setCard(card);
        consumerRepository.save(consumer);
        SelectedAdvantage selectedAdvantage = new SelectedAdvantage(StateSelectedAdvantage.IN_PROCESSES,card,advantage,LocalDateTime.now());
        selectedAdvantageRepository.save(selectedAdvantage);
        ParcDto parcDto = new ParcDto("plate",5);
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.ISWYPLSRoutes.BASE_ISWYPLS_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parcDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

 */ //TODO REPAIR


    @Test
    public void validPartnerSignInTestNotFound() throws Exception {
        ParcDto parcDto = new ParcDto("plate",5);
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.ISWYPLSRoutes.BASE_ISWYPLS_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parcDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
