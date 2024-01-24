package fr.univcotedazur.multifidelity.connectors;

import fr.univcotedazur.multifidelity.connectors.externaldto.ParcDto;
import fr.univcotedazur.multifidelity.entities.Consumer;
import fr.univcotedazur.multifidelity.exceptions.IWYPLSConnexionException;
import fr.univcotedazur.multifidelity.interfaces.IsawWhereYouParkedLastSummer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ISWYPLS_Proxy implements IsawWhereYouParkedLastSummer {


    //TODO ADD CONNECTOR TESTS
    @Value("${iswypls.host.baseurl}")
    private String ISWYPLPort;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean startSession(Consumer consumer) throws IWYPLSConnexionException {
        try {
            ParcDto parcDto = new ParcDto(consumer.getLicencePlate(),30);
            ResponseEntity<ParcDto> result = restTemplate.postForEntity(
                    ISWYPLPort ,
                    parcDto,
                    ParcDto.class
            );
            return (result.getStatusCode().equals(HttpStatus.CREATED));
        }
        catch (HttpClientErrorException errorException) {
            if (errorException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                return false;
            }
            throw new IWYPLSConnexionException("error while the connection with the IsawWhereYouParkedLastSummer host ");
        }
    }
}
