package fr.univcotedazur.multifidelity.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelity.constant.RouteConstants;
import fr.univcotedazur.multifidelity.controllers.dto_in.SocietyDtoIn;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
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

import java.time.LocalTime;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SocietyIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    SocietyRepository societyRepository;

    Partner partner;

    @BeforeEach
    public void setUp() {
        partnerRepository.deleteAll();
        societyRepository.deleteAll();
        partner = new Partner("Polytech", "popo@gmail.com", "mdp");
        partnerRepository.save(partner);
    }

    @AfterEach
    public void tearDown() {
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
    }

    @Test
    public void validSocietyCreateTest() throws Exception {
        SocietyDtoIn societyDtoIn = new SocietyDtoIn("Polytech", partner.getId(), LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.SocietyRoutes.BASE_SOCIETIES_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(societyDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void notFoundSocietyCreateTest() throws Exception {
        SocietyDtoIn societyDtoIn = new SocietyDtoIn("Polytech", partner.getId()+1, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.SocietyRoutes.BASE_SOCIETIES_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(societyDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateSocietyTest() throws Exception {
        Society society = new Society("Polytech", partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        societyRepository.save(society);
        SocietyDtoIn societyDtoIn = new SocietyDtoIn("PolytechUpdated", partner.getId(), LocalTime.of(9, 0), LocalTime.of(19, 0), "adresseUpdated");
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.SocietyRoutes.BASE_SOCIETIES_ROUTE + '/' + society.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(societyDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundUpdateTest() throws Exception {
        SocietyDtoIn societyDtoIn = new SocietyDtoIn("PolytechUpdated", partner.getId(), LocalTime.of(9, 0), LocalTime.of(19, 0), "adresseUpdated");
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.SocietyRoutes.BASE_SOCIETIES_ROUTE + '/' + 100)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(societyDtoIn)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllTest() throws Exception {
        Society society = new Society("Polytech", partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        societyRepository.save(society);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.SocietyRoutes.BASE_SOCIETIES_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void getByNameTest() throws Exception {
        Society society = new Society("Polytech", partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        societyRepository.save(society);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.SocietyRoutes.BASE_SOCIETIES_ROUTE + "/name/" + society.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundGetByNameTest() throws Exception {
        Society society = new Society("Polytech", partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        societyRepository.save(society);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.SocietyRoutes.BASE_SOCIETIES_ROUTE + "/name/" + society.getName() + "trucEnTrop")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getByIdTest() throws Exception {
        Society society = new Society("Polytech", partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        societyRepository.save(society);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.SocietyRoutes.BASE_SOCIETIES_ROUTE + "/"+ society.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundGetByIdTest() throws Exception {
        Society society = new Society("Polytech", partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        societyRepository.save(society);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.SocietyRoutes.BASE_SOCIETIES_ROUTE + "/id/" + society.getId() + "12")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}
