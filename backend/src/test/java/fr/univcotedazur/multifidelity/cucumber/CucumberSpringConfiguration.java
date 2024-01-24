package fr.univcotedazur.multifidelity.cucumber;

import fr.univcotedazur.multifidelity.interfaces.Bank;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@CucumberContextConfiguration
@SpringBootTest
public class CucumberSpringConfiguration {

    @MockBean
    private Bank bank;
}