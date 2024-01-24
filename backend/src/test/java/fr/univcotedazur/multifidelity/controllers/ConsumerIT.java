package fr.univcotedazur.multifidelity.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelity.constant.RouteConstants;
import fr.univcotedazur.multifidelity.controllers.dto_in.ConsumerDtoIn;
import fr.univcotedazur.multifidelity.entities.Consumer;
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
public class ConsumerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ConsumerRepository consumerRepository;

    @BeforeEach
    public void setUp() {
        consumerRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        consumerRepository.deleteAll();
    }

    @Test
    public void validConsumerSignInTest() throws Exception {
        ConsumerDtoIn validCustomer = new ConsumerDtoIn("momo@gmail.com", "mdp", "1234567890123456", "MO-112-MO");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.ConsumerRoutes.BASE_CONSUMER_ROUTE + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCustomer)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void invalidEmailSignInTest() throws Exception {
        ConsumerDtoIn invalidEmail = new ConsumerDtoIn("momo", "mdp", "1234567890123456", "MO-112-MO");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.ConsumerRoutes.BASE_CONSUMER_ROUTE + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmail)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    public void invalidCreditCardSignInTest() throws Exception {
        ConsumerDtoIn invalidEmail = new ConsumerDtoIn("momo@gmail.com", "mdp", "1", "MO-112-MO");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.ConsumerRoutes.BASE_CONSUMER_ROUTE + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmail)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    public void alreadyUsedEmailSignInTest() throws Exception {
        Consumer consumer = new Consumer("momo@gmail.com", "momo", "1111111111111111", "TE-112-ST", null);
        consumerRepository.save(consumer);
        ConsumerDtoIn invalidEmail = new ConsumerDtoIn("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.ConsumerRoutes.BASE_CONSUMER_ROUTE + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmail)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void validConsumerLogInTest() throws Exception {
        Consumer valid = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        consumerRepository.save(valid);
        ConsumerDtoIn tryConnect = new ConsumerDtoIn("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.ConsumerRoutes.BASE_CONSUMER_ROUTE + "/log-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tryConnect)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundLogInTest() throws Exception {
        ConsumerDtoIn tryConnect = new ConsumerDtoIn("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.ConsumerRoutes.BASE_CONSUMER_ROUTE + "/log-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tryConnect)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    public void invalidEmailLogInTest() throws Exception {
        ConsumerDtoIn tryConnect = new ConsumerDtoIn("momo", "mdp", "1111111111111111", "MO-112-MO");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.ConsumerRoutes.BASE_CONSUMER_ROUTE + "/log-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tryConnect)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    public void validUpdateConsumerTest() throws Exception {
        Consumer valid = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        consumerRepository.save(valid);
        ConsumerDtoIn tryUpdate = new ConsumerDtoIn("mama@gmail.com", "mama", "2222222222222222", "UP-112-DA");
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.ConsumerRoutes.BASE_CONSUMER_ROUTE + "/{consumerId}", valid.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tryUpdate)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundUpdateConsumerTest() throws Exception {
        ConsumerDtoIn tryUpdate = new ConsumerDtoIn("mama@gmail.com", "mama", "2222222222222222", "UP-112-DA");
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.ConsumerRoutes.BASE_CONSUMER_ROUTE + "/{consumerId}", 122)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tryUpdate)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllConsumerTest() throws Exception {
        Consumer valid = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        consumerRepository.save(valid);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.ConsumerRoutes.BASE_CONSUMER_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllConsumerEmptyTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.ConsumerRoutes.BASE_CONSUMER_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
