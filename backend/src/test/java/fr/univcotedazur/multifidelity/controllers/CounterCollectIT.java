package fr.univcotedazur.multifidelity.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CounterCollectIT {
/*


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    SelectedAdvantageRepository selectedAdvantageRepository;

    @Autowired
    AdvantageRepository advantageRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private CardRepository cardRepository;

    Society society;
    Partner partner;

    @BeforeEach
    public void setUp() {
        selectedAdvantageRepository.deleteAll();
        advantageRepository.deleteAll();
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
        cardRepository.deleteAll();
        partner = new Partner("Les café de Paris", "cafe@gmail.com", "ilovecoffee");
        partnerRepository.save(partner);
        society = new Society( "Le café du Marais", partner, LocalTime.of(8, 0, 0, 0), LocalTime.of(18, 0, 0, 0), "Paris");
        societyRepository.save(society);
    }

    @AfterEach
    public void after() {
        selectedAdvantageRepository.deleteAll();
        advantageRepository.deleteAll();
        societyRepository.deleteAll();
        partnerRepository.deleteAll();
        cardRepository.deleteAll();
    }

    @Test
    public void testCollectGift() throws Exception {
        Card card = new Card( 0, 0);
        cardRepository.save(card);
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0), LocalDateTime.of(2021, 12, 31, 0, 0, 0, 0), 2);
        advantageRepository.save(validAdvantage);
        SelectedAdvantage selectedAdvantage = new SelectedAdvantage(StateSelectedAdvantage.AVAILABLE, card, validAdvantage, LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0));
        selectedAdvantageRepository.save(selectedAdvantage);
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.CounterCollectRoutes.BASE_COLLECT_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selectedAdvantage.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void notFoundCollectGift() throws Exception {
        Card card = new Card( 0, 0);
        cardRepository.save(card);
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0), LocalDateTime.of(2021, 12, 31, 0, 0, 0, 0), 2);
        advantageRepository.save(validAdvantage);
        SelectedAdvantage selectedAdvantage = new SelectedAdvantage( StateSelectedAdvantage.AVAILABLE, card, validAdvantage, LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0));
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.CounterCollectRoutes.BASE_COLLECT_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(235)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void alreadyReceivedGiftCollect() throws Exception {
        Card card = new Card( 0, 0);
        cardRepository.save(card);
        Advantage validAdvantage = new Advantage("Café offert", 2, false, 2, society, LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0), LocalDateTime.of(2021, 12, 31, 0, 0, 0, 0), 2);
        advantageRepository.save(validAdvantage);
        SelectedAdvantage selectedAdvantage = new SelectedAdvantage(StateSelectedAdvantage.AVAILABLE, card, validAdvantage, LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0));
        selectedAdvantageRepository.save(selectedAdvantage);
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.CounterCollectRoutes.BASE_COLLECT_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selectedAdvantage.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.put(RouteConstants.CounterCollectRoutes.BASE_COLLECT_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selectedAdvantage.getId())))
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

 */

}
