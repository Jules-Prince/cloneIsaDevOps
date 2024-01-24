package fr.univcotedazur.multifidelity.cron;

import fr.univcotedazur.multifidelity.components.VfpUnit;
import fr.univcotedazur.multifidelity.config.ScheduledConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest
@SpringJUnitConfig(ScheduledConfig.class)
public class CronTest {
    @SpyBean
    private VfpUnit vfpUnit;

    /*
    @Test
    public void whenWaitOneSecond_thenScheduledIsCalledAtLeastTenTimes() {

        await()
                .atMost(new Duration(6,MINUTES))
                .untilAsserted(() -> verify(vfpUnit, atLeast(1)).VFPCheck());
   }

     */
}
