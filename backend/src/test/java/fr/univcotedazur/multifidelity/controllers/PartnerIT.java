package fr.univcotedazur.multifidelity.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelity.constant.RouteConstants;
import fr.univcotedazur.multifidelity.controllers.dto_in.PartnerDtoIn;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
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
public class PartnerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PartnerRepository partnerRepository;

    @BeforeEach
    public void setUp() {
        partnerRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        partnerRepository.deleteAll();
    }

    @Test
    public void validPartnerSignInTest() throws Exception {
        PartnerDtoIn partnerDtoIn = new PartnerDtoIn("Polytech", "popo@gmail.com", "mdp");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.PartnerRoutes.BASE_PARTNER_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partnerDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void alreadyExistingPartnerSignInTest() throws Exception {
        Partner partner = new Partner("Polytech", "popo@gmail.com", "mdp");
        partnerRepository.save(partner);
        PartnerDtoIn partnerDtoIn = new PartnerDtoIn("Polytech", "popo@gmail.com", "mdp");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.PartnerRoutes.BASE_PARTNER_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partnerDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }


    @Test
    public void validLogInTest() throws Exception {
        Partner partner = new Partner("Polytech", "popo@gmail.com", "mdp");
        partnerRepository.save(partner);
        PartnerDtoIn partnerDtoIn = new PartnerDtoIn("Polytech", "popo@gmail.com", "mdp");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.PartnerRoutes.BASE_PARTNER_ROUTE + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partnerDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void invalidLogInTest() throws Exception {
        Partner partner = new Partner("Polytech", "popo@gmail.com", "mdp");
        partnerRepository.save(partner);
        PartnerDtoIn partnerDtoIn = new PartnerDtoIn("Polytech", "popo", "mdp");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.PartnerRoutes.BASE_PARTNER_ROUTE + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partnerDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getAllTest() throws Exception {
        Partner partner = new Partner("Polytech", "popo@gmail.com", "mdp");
        partnerRepository.save(partner);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.PartnerRoutes.BASE_PARTNER_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getByIdTest() throws Exception {
        Partner partner = new Partner("Polytech", "popo@gmail.com", "mdp");
        partnerRepository.save(partner);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.PartnerRoutes.BASE_PARTNER_ROUTE + "/" + partner.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
