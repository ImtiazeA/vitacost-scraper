package com.trackstreet.scraper.vitacostscraper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class VitacostScraperApplication {

    private static final Logger log = LoggerFactory.getLogger(VitacostScraperApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(VitacostScraperApplication.class, args);

        VitacostService vitacostService = ac.getBean(VitacostService.class);

        List<String> productLinks = vitacostService.getProductLinks("Carlon Labs");

        try {
            String linksAsJson = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(productLinks);

            log.info("All Links: " + linksAsJson);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to Get JSON from List of Product", e);
        }

        ac.close();
    }

}
