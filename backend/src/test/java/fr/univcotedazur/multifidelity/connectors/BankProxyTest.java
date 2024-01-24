package fr.univcotedazur.multifidelity.connectors;

import fr.univcotedazur.multifidelity.entities.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;

@SpringBootTest
public class BankProxyTest {


    @Mock
    BankProxy bank;
    Consumer consumer;

    @Test
    public void testPayValid(){
        consumer = new Consumer("djulo.fr@bg.com", "fleur1234", "123456789", "YOOO", null);
        when(bank.pay(consumer, 10)).thenReturn(true);
        Assertions.assertTrue(bank.pay(consumer, 10));
    }

    @Test
    public void testPayInvalid(){
        consumer = new Consumer("djulo.fr@bg.com", "fleur1234", "coucou-jecasse-TOUUUUT", "YOOO", null);
        when(bank.pay(consumer, 10)).thenReturn(false);
        Assertions.assertFalse(bank.pay(consumer, 10));
    }

}
