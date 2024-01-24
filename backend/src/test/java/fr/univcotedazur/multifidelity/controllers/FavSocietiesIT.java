package fr.univcotedazur.multifidelity.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelity.constant.RouteConstants;
import fr.univcotedazur.multifidelity.entities.Card;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.entities.Society;
import fr.univcotedazur.multifidelity.repositories.CardRepository;
import fr.univcotedazur.multifidelity.repositories.ConsumerRepository;
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
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FavSocietiesIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SocietyRepository societyRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ConsumerRepository consumerRepository;

    Partner partner;
    Consumer consumer;

    @BeforeEach
    public void setUp() {
        consumerRepository.deleteAll();
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
        partner = new Partner("Les café de Paris", "cafe@gmail.com", "ilovecoffee");
        partner =partnerRepository.save(partner);
    }

    @AfterEach
    public void tearDown() {
        consumerRepository.deleteAll();
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
    }

    @Test
    public void validGetAllByConsumerId() throws Exception {
        Society society = new Society("Le café du Marais", partner, LocalTime.of(8, 0, 0, 0), LocalTime.of(18, 0, 0, 0), "Paris");
        societyRepository.save(society);
        List<Society> fav = List.of(society);
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        consumer.setFavSocieties(fav);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.FavSocietiesRoutes.BASE_FAV_SOCIETIES_ROUTE + "/" + consumer.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(consumer.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundAllByConsumerId() throws Exception {
        Society society = new Society( "Le café du Marais", partner, LocalTime.of(8, 0, 0, 0), LocalTime.of(18, 0, 0, 0), "Paris");
        societyRepository.save(society);
        List<Society> fav = List.of(society);
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        consumer.setFavSocieties(fav);
        mockMvc.perform(MockMvcRequestBuilders.get(RouteConstants.FavSocietiesRoutes.BASE_FAV_SOCIETIES_ROUTE + "/" + 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(122)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void validAddFavSociety() throws Exception {
        Society society = new Society("Le café du Marais", partner, LocalTime.of(8, 0, 0, 0), LocalTime.of(18, 0, 0, 0), "Paris");
        societyRepository.save(society);
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        List<Society> fav = new ArrayList<>();
        consumer.setFavSocieties(fav);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.FavSocietiesRoutes.BASE_FAV_SOCIETIES_ROUTE + "/" + consumer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(society.getId())))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void notFoundConsumerAddFavSociety() throws Exception {
        Society society = new Society("Le café du Marais", partner, LocalTime.of(8, 0, 0, 0), LocalTime.of(18, 0, 0, 0), "Paris");
        societyRepository.save(society);
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.FavSocietiesRoutes.BASE_FAV_SOCIETIES_ROUTE + "/" + 122)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(society.getId())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void notFoundSocietyAddFavSociety() throws Exception {
        Society society = new Society( "Le café du Marais", partner, LocalTime.of(8, 0, 0, 0), LocalTime.of(18, 0, 0, 0), "Paris");
        societyRepository.save(society);
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        List<Society> fav = List.of(society);
        consumer.setFavSocieties(fav);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.post(RouteConstants.FavSocietiesRoutes.BASE_FAV_SOCIETIES_ROUTE + "/" + consumer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(society.getId() + 1)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void validDeleteFavSociety() throws Exception {
        Society society = new Society("Le café du Marais", partner, LocalTime.of(8, 0, 0, 0), LocalTime.of(18, 0, 0, 0), "Paris");
        societyRepository.save(society);
        List<Society> fav = new ArrayList<>();
        fav.add(society);
        Card card = new Card(0,0);
        cardRepository.save(card);
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO", card);
        consumer.setFavSocieties(fav);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.delete(RouteConstants.FavSocietiesRoutes.BASE_FAV_SOCIETIES_ROUTE + "/" + consumer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(society.getId())))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    public void notFoundConsumerDeleteFavSociety() throws Exception {
        Society society = new Society("Le café du Marais", partner, LocalTime.of(8, 0, 0, 0), LocalTime.of(18, 0, 0, 0), "Paris");
        societyRepository.save(society);
        List<Society> fav = List.of(society);
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        consumer.setFavSocieties(fav);
        mockMvc.perform(MockMvcRequestBuilders.delete(RouteConstants.FavSocietiesRoutes.BASE_FAV_SOCIETIES_ROUTE + "/" + 122)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(society.getId())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void notFoundSocietyDeleteFavSociety() throws Exception {
        Society society = new Society("Le café du Marais", partner, LocalTime.of(8, 0, 0, 0), LocalTime.of(18, 0, 0, 0), "Paris");
        societyRepository.save(society);
        List<Society> fav = List.of(society);
        consumer = new Consumer("momo@gmail.com", "mdp", "1111111111111111", "MO-112-MO",null);
        consumer.setFavSocieties(fav);
        consumerRepository.save(consumer);
        mockMvc.perform(MockMvcRequestBuilders.delete(RouteConstants.FavSocietiesRoutes.BASE_FAV_SOCIETIES_ROUTE + "/" + consumer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(society.getId() + 1)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
