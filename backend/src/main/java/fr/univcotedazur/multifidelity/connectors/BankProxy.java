package fr.univcotedazur.multifidelity.connectors;

import fr.univcotedazur.multifidelity.connectors.externaldto.PaymentDTO;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.BankConnexionException;
import fr.univcotedazur.multifidelity.interfaces.Bank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class BankProxy implements Bank {

    @Value("${bank.host.baseurl}")
    private String bankHostandPort;

    private RestTemplate restTemplate = new RestTemplate();

    private static final String CARD_VALIDATER = "896983";

    @Override
    public boolean pay(Consumer consumer, float value) throws  BankConnexionException{
        try {
            ResponseEntity<PaymentDTO> result = restTemplate.postForEntity(
                    bankHostandPort + "/cctransactions",
                    new PaymentDTO(consumer.getCreditCardNumber()+CARD_VALIDATER, value),
                    PaymentDTO.class
            );
            return (result.getStatusCode().equals(HttpStatus.CREATED));
        }
        catch (HttpClientErrorException errorException) {
            if (errorException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                return false;
            }
            throw new BankConnexionException("error while the connection with the bank");
        }
    }
}