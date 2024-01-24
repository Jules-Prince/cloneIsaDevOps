package fr.univcotedazur.cli.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.multifidelity.cli.commands.ConsumerCommands;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.RequestEntity.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ConsumerCommands.class)
public class ConsumerCommandsTest {
/*
        @Autowired
        private ConsumerCommands consumer;

        @Autowired
        private MockRestServiceServer server;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void consumerSignIn() {
            objectMapper.writeValueAsString();
            server
                    .expect(requestTo("/consumers"))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess("[\"CHOCOLALALA\",\"DARK_TEMPTATION\",\"SOO_CHOCOLATE\"]", MediaType.APPLICATION_JSON));

            assertEquals(List<CliConsumer.class>, consumer.consumers());
        }

    }


 */
}
