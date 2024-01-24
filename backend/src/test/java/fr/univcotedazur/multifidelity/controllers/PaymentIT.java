package fr.univcotedazur.multifidelity.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelity.constant.RouteConstants;
import fr.univcotedazur.multifidelity.controllers.dto_in.PurchaseDtoIn;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
import fr.univcotedazur.multifidelity.repositories.PurchaseRepository;
import fr.univcotedazur.multifidelity.repositories.SocietyRepository;
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

import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PaymentIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    SocietyRepository societyRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ConsumerRepository consumerRepository;

    Partner partner;

    Society society;

    @BeforeEach
    public void setUp() {
        purchaseRepository.deleteAll();
        partnerRepository.deleteAll();
        societyRepository.deleteAll();
        cardRepository.deleteAll();
        consumerRepository.deleteAll();
        partner = new Partner("Polytech", "popo@gmail.com", "mdp");
        partnerRepository.save(partner);
        society = new Society("Polytech", partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        societyRepository.save(society);
    }

    @AfterEach
    public void tearDown() {
        purchaseRepository.deleteAll();
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
        cardRepository.deleteAll();
        consumerRepository.deleteAll();
    }

    @Test
    public void validCreateWithCard() throws Exception {
        Card card = new Card(1,1);
        Long cardId = cardRepository.save(card).getId();
        Consumer consumer = new Consumer();
        consumer.setCard(card);
        consumerRepository.save(consumer);
        card.setConsumer(consumer);
        card = cardRepository.findById(cardId).get();
        PurchaseDtoIn purchaseDtoIn = new PurchaseDtoIn(1, LocalDateTime.of(2021, 1, 1, 0,0,0), society.getId(), card.getId());
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.PaymentRoutes.BASE_PAYMENT_ROUTE + "/with-card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void notFoundCreateWithCard() throws Exception {
        Card card = new Card(1,1);
        cardRepository.save(card);
        PurchaseDtoIn purchaseDtoIn = new PurchaseDtoIn(1, LocalDateTime.of(2021, 1, 1, 0,0,0), society.getId(), card.getId()+1);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.PaymentRoutes.BASE_PAYMENT_ROUTE + "/with-card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void lowBalanceCreateWithCard() throws Exception {
        Card card = new Card(0,0);
        cardRepository.save(card);
        PurchaseDtoIn purchaseDtoIn = new PurchaseDtoIn(1, LocalDateTime.of(2021, 1, 1, 0,0,0), society.getId(), card.getId());
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.PaymentRoutes.BASE_PAYMENT_ROUTE + "/with-card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void validCreate() throws Exception {
        Card card = new Card(1,1);
        Long cardId = cardRepository.save(card).getId();
        Consumer consumer = new Consumer();
        consumer.setCard(card);
        consumerRepository.save(consumer);
        card.setConsumer(consumer);
        card = cardRepository.findById(cardId).get();
        PurchaseDtoIn purchaseDtoIn = new PurchaseDtoIn(1, LocalDateTime.of(2021, 1, 1, 0,0,0), society.getId(), card.getId());
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.PaymentRoutes.BASE_PAYMENT_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void notFoundCreate() throws Exception {
        Card card = new Card(1,1);
        cardRepository.save(card);
        PurchaseDtoIn purchaseDtoIn = new PurchaseDtoIn(1, LocalDateTime.of(2021, 1, 1, 0,0,0), society.getId(), card.getId()+1);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.PaymentRoutes.BASE_PAYMENT_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllByConsumer() throws Exception {
        Consumer consumer = new Consumer("momo@gmail.com", "momo", "1111111111111111", "TE-112-ST", null);
        consumerRepository.save(consumer);
        Card card = new Card(1,1);
        cardRepository.save(card);
        PurchaseDtoIn purchaseDtoIn = new PurchaseDtoIn(1, LocalDateTime.of(2021, 1, 1, 0,0,0), society.getId(), card.getId());
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.PaymentRoutes.BASE_PAYMENT_ROUTE + "/consumer/" + consumer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundGetAllByConsumer() throws Exception {
        Consumer consumer = new Consumer("momo@gmail.com", "momo", "1111111111111111", "TE-112-ST", null);
        consumerRepository.save(consumer);
        Card card = new Card(1,1);
        cardRepository.save(card);
        PurchaseDtoIn purchaseDtoIn = new PurchaseDtoIn(1, LocalDateTime.of(2021, 1, 1, 0,0,0), society.getId(), card.getId());
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.PaymentRoutes.BASE_PAYMENT_ROUTE + "/consumer/" + consumer.getId()+1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllBySociety() throws Exception {
        Card card = new Card(1,1);
        cardRepository.save(card);
        PurchaseDtoIn purchaseDtoIn = new PurchaseDtoIn(1, LocalDateTime.of(2021, 1, 1, 0,0,0), society.getId(), card.getId());
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.PaymentRoutes.BASE_PAYMENT_ROUTE + "/society/" + society.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundetAllBySociety() throws Exception {
        Card card = new Card(1,1);
        cardRepository.save(card);
        PurchaseDtoIn purchaseDtoIn = new PurchaseDtoIn(1, LocalDateTime.of(2021, 1, 1, 0,0,0), society.getId(), card.getId());
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.PaymentRoutes.BASE_PAYMENT_ROUTE + "/society/" + society.getId()+1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
