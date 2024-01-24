package fr.univcotedazur.multifidelity.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelity.constant.RouteConstants;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
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
public class CardIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ConsumerRepository consumerRepository;

    Consumer consumer;

    @BeforeEach
    public void setUp() {
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        consumerRepository.deleteAll();
        cardRepository.deleteAll();
    }

    @Test
    public void validGetBalanceTest() throws Exception {
        Card card =cardRepository.save(new Card( 0, 0));
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",card);
        consumer.setCard(card);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.CardRoutes.BASE_CARD_ROUTE + "/"+ consumer.getId()+ "/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consumer)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundConsumerGetBalanceTest() throws Exception {
        Card card =cardRepository.save(new Card( 0, 0));
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",card);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.CardRoutes.BASE_CARD_ROUTE + "/"+ 144+ "/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consumer)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void notFoundCardGetBalanceTest() throws Exception {
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.CardRoutes.BASE_CARD_ROUTE + "/"+ consumer.getId()+ "/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consumer)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void validCreationTest() throws Exception {
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.CardRoutes.BASE_CARD_ROUTE + "/"+ consumer.getId()+ "/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consumer)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void NotFoundCreationTest() throws Exception {
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.CardRoutes.BASE_CARD_ROUTE + "/"+ 122+ "/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consumer)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void alreadyACardCreationTest() throws Exception {
        Card card =cardRepository.save(new Card( 0, 0));
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",card);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.CardRoutes.BASE_CARD_ROUTE + "/"+ consumer.getId()+ "/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consumer)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllPointsTest() throws Exception {
        Card card =cardRepository.save(new Card( 0, 0));
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",card);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.CardRoutes.BASE_CARD_ROUTE + "/"+ consumer.getId()+ "/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consumer)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundCardGetAllPointsTest() throws Exception {
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.CardRoutes.BASE_CARD_ROUTE + "/"+ consumer.getId()+ "/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consumer)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void notFoundConsumerGetAllPointsTest() throws Exception {
        Card card =cardRepository.save(new Card( 0, 0));
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",card);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.CardRoutes.BASE_CARD_ROUTE + "/"+ 122+ "/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consumer)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
