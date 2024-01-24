package fr.univcotedazur.multifidelity.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelity.constant.RouteConstants;
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
public class StatisticIT {

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
    public void getProfitBySocietyTest() throws Exception {
        Society society = new Society("Polytech", partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        societyRepository.save(society);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.StatisticRoutes.BASE_STATISTIC_ROUTE + "/profits/society/" + society.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundGetProfitBySociety() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.StatisticRoutes.BASE_STATISTIC_ROUTE + "/profits/society/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAll() throws Exception {
        Society society = new Society("Polytech", partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        societyRepository.save(society);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.StatisticRoutes.BASE_STATISTIC_ROUTE + "/profits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getFrequencyBySociety() throws Exception {
        Society society = new Society("Polytech", partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        societyRepository.save(society);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.StatisticRoutes.BASE_STATISTIC_ROUTE + "/frequency-purchase/societies/" + society.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundFrequencyBySociety() throws Exception {
        Society society = new Society("Polytech", partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        societyRepository.save(society);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.StatisticRoutes.BASE_STATISTIC_ROUTE + "/frequency-purchase/societies/" + society.getId()+1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllFrequency() throws Exception {
        Society society = new Society("Polytech", partner, LocalTime.of(8, 0), LocalTime.of(18, 0), "adresse");
        societyRepository.save(society);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.StatisticRoutes.BASE_STATISTIC_ROUTE + "/frequency-purchase")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
