package fr.univcotedazur.multifidelity.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelity.constant.RouteConstants;
import fr.univcotedazur.multifidelity.controllers.dto_in.AdvantageDtoIn;
import fr.univcotedazur.multifidelity.entities.Advantage;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.SelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.entities.StateSelectedAdvantage;
import fr.univcotedazur.multifidelity.entities.Validity;
import fr.univcotedazur.multifidelity.repositories.AdvantageRepository;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootTest
@AutoConfigureMockMvc
public class AdvantageIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdvantageRepository advantageRepository;

    @Autowired
    private ValidityRepository validityRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private SelectedAdvantageRepository selectedAdvantageRepository;

    @Autowired
    private CardRepository cardRepository;

    Society society;
    Partner partner;

    Consumer consumer;

    @BeforeEach
    public void setUp() {
        cardRepository.deleteAll();
        validityRepository.deleteAll();
        selectedAdvantageRepository.deleteAll();
        advantageRepository.deleteAll();
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
        consumerRepository.deleteAll();
        partner = new Partner("Les café de Paris", "cafe@gmail.com", "ilovecoffee");
        partnerRepository.save(partner);
        society = new Society("Le café du Marais", partner, LocalTime.of(8, 0, 0, 0), LocalTime.of(18, 0, 0, 0), "Paris");
        society =societyRepository.save(society);
    }

    @AfterEach
    public void after() {
        advantageRepository.deleteAll();
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
        consumerRepository.deleteAll();
    }

    @Test
    public void validAdvantageCreationTest() throws Exception {
        AdvantageDtoIn validAdvantage = new AdvantageDtoIn("Café offert", 2, false, 0, 2, society.getId());
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAdvantage)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void alreadyExistingAdvantageCreationTest() throws Exception {
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society,2);
        advantageRepository.save(validAdvantage);
        AdvantageDtoIn copieur = new AdvantageDtoIn("Café offert", 2, false, 0, 2, society.getId());
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(copieur)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void validDeletionTest() throws Exception {
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        advantageRepository.save(validAdvantage);
        mockMvc.perform(MockMvcRequestBuilders.delete(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/" + validAdvantage.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void notFoundDeletionTest() throws Exception {
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        mockMvc.perform(MockMvcRequestBuilders.delete(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/" + 234)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void validUpdateTest() throws Exception {
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        validAdvantage =advantageRepository.save(validAdvantage);
        AdvantageDtoIn update = new AdvantageDtoIn("Café offert 2", 3, true, 3, 2, society.getId());
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/" + validAdvantage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundUpdateTest() throws Exception {
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        AdvantageDtoIn update = new AdvantageDtoIn("Café offert 2", 3, true, 3, 2, society.getId());
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/" + "234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void validGetFromSocietyTest() throws Exception {
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        advantageRepository.save(validAdvantage);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/society" + "/" + society.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundGetFromSocietyTest() throws Exception {
        Society notFoundSociety = new Society("Le café du Marais", partner, LocalTime.of(8, 0, 0, 0), LocalTime.of(18, 0, 0, 0), "Paris");
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/society" + "/" +  144)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllTest() throws Exception {
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        advantageRepository.save(validAdvantage);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllByConsumerTest() throws Exception {
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        advantageRepository.save(validAdvantage);
        Card card =cardRepository.save(new Card( 0, 0));
        Consumer valid = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",card);
        valid.setVfp(false);
        consumerRepository.save(valid);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/consumer" + "/" + valid.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundAllByConsumerTest() throws Exception {
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        advantageRepository.save(validAdvantage);
        Card card =cardRepository.save(new Card( 0, 0));
        Consumer valid = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",card);
        valid.setVfp(false);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/consumer" + "/" + 123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void validUseAdvantageTest() throws Exception {
        Card card = new Card( 0, 0);
        cardRepository.save(card);
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        advantageRepository.save(validAdvantage);
        SelectedAdvantage selectedAdvantage = new SelectedAdvantage(StateSelectedAdvantage.AVAILABLE, card, validAdvantage, LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0));
        selectedAdvantageRepository.save(selectedAdvantage);
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE+"/use-advantage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selectedAdvantage.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundUseAdvantageTest() throws Exception {
        Card card = new Card( 0, 0);
        cardRepository.save(card);
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        advantageRepository.save(validAdvantage);
        SelectedAdvantage selectedAdvantage = new SelectedAdvantage( StateSelectedAdvantage.AVAILABLE, card, validAdvantage, LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0));
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE+"/use-advantage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(235)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void expireUseAdvantageTest() throws Exception {
        Card card = new Card( 0, 0);
        cardRepository.save(card);
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        advantageRepository.save(validAdvantage);
        SelectedAdvantage selectedAdvantage = new SelectedAdvantage(StateSelectedAdvantage.AVAILABLE, card, validAdvantage, LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0));
        selectedAdvantageRepository.save(selectedAdvantage);
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE+"/use-advantage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selectedAdvantage.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selectedAdvantage.getId())))
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

    @Test
    public void validSelectAdvantage() throws Exception {
        Card card = new Card( 100, 100);
        cardRepository.save(card);
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        advantageRepository.save(validAdvantage);
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",card);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/" + consumer.getId() + "/select-advantage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAdvantage.getId())))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void notFoundSelectAdvantage() throws Exception {
        Card card = new Card( 100, 100);
        cardRepository.save(card);
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        advantageRepository.save(validAdvantage);
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",card);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/" + consumer.getId()+1 + "/select-advantage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAdvantage.getId())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void notEnoughPointSelectAdvantage() throws Exception {
        Card card = new Card( 0, 0);
        cardRepository.save(card);
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        advantageRepository.save(validAdvantage);
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",card);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/" + consumer.getId() + "/select-advantage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAdvantage.getId())))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getAllSelectedEligibleByConsumerTest() throws Exception {
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(2));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setNumberOfUse(1);
        validity =validityRepository.save(validity);
        validAdvantage.setValidity(validity);
        advantageRepository.save(validAdvantage);
        Card card =cardRepository.save(new Card( 0, 0));
        Consumer valid = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",card);
        valid.setVfp(false);
        consumerRepository.save(valid);
        SelectedAdvantage selectedAdvantage = new SelectedAdvantage(StateSelectedAdvantage.AVAILABLE, card, validAdvantage, LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0));
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/selected/consumer" + "/" + valid.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundetAllSelectedEligibleByConsumerTest() throws Exception {
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, 2);
        Validity validity = new Validity();
        validity.setDuration(Duration.ofDays(2));
        validity.setDurationPerUseBetweenUse(Duration.ofDays(1));
        validity.setNumberOfUse(1);
        validity =validityRepository.save(validity);
        validAdvantage.setValidity(validity);
        advantageRepository.save(validAdvantage);
        Card card =cardRepository.save(new Card( 0, 0));
        Consumer valid = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",card);
        valid.setVfp(false);
        consumerRepository.save(valid);
        SelectedAdvantage selectedAdvantage = new SelectedAdvantage(StateSelectedAdvantage.AVAILABLE, card, validAdvantage, LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0));
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.AdvantageRoutes.BASE_ADVANTAGE_ROUTE + "/selected/consumer" + "/" + valid.getId()+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}
