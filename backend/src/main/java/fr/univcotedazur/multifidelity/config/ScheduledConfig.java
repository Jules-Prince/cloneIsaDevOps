package fr.univcotedazur.multifidelity.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan("fr.univcotedazur.multifidelity")
public class ScheduledConfig {
}
