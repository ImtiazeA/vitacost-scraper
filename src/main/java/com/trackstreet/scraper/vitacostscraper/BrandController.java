package com.trackstreet.scraper.vitacostscraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class BrandController {

    @Autowired
    private VitacostService vitacostService;

    @GetMapping("/brand")
    public List<String> getProductLinks(@RequestParam String brandName) {

        List<String> productLinks = vitacostService.getProductLinks(brandName);

        return productLinks;
    }

}
