package fr.univcotedazur.multifidelity.components;

import fr.univcotedazur.multifidelity.entities.Partner;
import fr.univcotedazur.multifidelity.exceptions.AccountNotFoundException;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistSimilarRessourceException;
import fr.univcotedazur.multifidelity.exceptions.AlreadyExistingAccountException;
import fr.univcotedazur.multifidelity.interfaces.PartnerAccountManager;
import fr.univcotedazur.multifidelity.interfaces.PartnerGetter;
import fr.univcotedazur.multifidelity.repositories.PartnerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Commit
public class PartnerManagerTest {

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private PartnerAccountManager partnerAccountManager;

    @Autowired
    private PartnerGetter partnerGetter;

    Partner partner;

    @BeforeEach
    public void setUpContext() {
        partner = new Partner("Boulangerie", "boulang@gmail.com", "mdp");
        partnerRepository.save(partner);
    }


    @AfterEach
    public void cleanUpContext() throws Exception{
        partnerRepository.deleteAll();
    }

    @Test
    public void getPartnerByIdTest() throws Exception {
        Partner partner = partnerGetter.getPartnerById(this.partner.getId());
        assertEquals(this.partner, partner);
    }

    @Test
    public void loginTest() throws Exception {
        Partner partner = partnerAccountManager.login("boulang@gmail.com", "mdp");
        assertEquals(this.partner, partner);
    }

    @Test
    @Transactional(propagation= Propagation.NEVER)
    public void loginTestSameMdpDiffEmail() throws AccountNotFoundException {
        assertThrows(AccountNotFoundException.class, () -> {
            partnerAccountManager.login("boulang2@gmail.com", "mdp");
        });
    }

    @Test
    @Transactional(propagation=Propagation.NEVER)
    public void loginTestSameEmailDiffMdp() throws AccountNotFoundException {
        assertThrows(AccountNotFoundException.class, () -> {
            partnerAccountManager.login("boulang@gmail.com", "mdp2");
        });
    }

    @Test
    public void addPartnerAccountTest() throws Exception {
        Partner newPartner = new Partner("Boulangerie2", "boulang2@gmail.com", "mdp2");
        Partner createdPartner = partnerAccountManager.addPartnerAccount(newPartner);
        assertEquals(createdPartner, newPartner);
        assertEquals(2, partnerRepository.count());
    }

    @Test
    @Transactional(propagation=Propagation.NEVER)
    public void addPartnerAccountSameEmailTest() throws AlreadyExistingAccountException, AlreadyExistSimilarRessourceException {
        Partner copieur = new Partner("Boulangerie2", "boulang@gmail.com", "mdp2");
        assertThrows(AlreadyExistingAccountException.class, () -> {
            partnerAccountManager.addPartnerAccount(copieur);
        });
    }

    @Test
    @Transactional(propagation=Propagation.NEVER)
    public void addPartnerAccountSameNameTest() throws AlreadyExistingAccountException, AlreadyExistSimilarRessourceException {
        Partner copieur = new Partner("Boulangerie", "boulang2@gmail.com", "mdp2");
        assertThrows(AlreadyExistSimilarRessourceException.class, () -> {
            partnerAccountManager.addPartnerAccount(copieur);
        });
    }

}