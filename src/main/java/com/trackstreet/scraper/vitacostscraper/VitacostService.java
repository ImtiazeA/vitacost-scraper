package com.trackstreet.scraper.vitacostscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VitacostService {

    private static final Logger log = LoggerFactory.getLogger(VitacostService.class);

    public List<String> getProductLinks(String brandName) {

        List<String> productLinks = new ArrayList<>();

        try {

            log.trace("Retrieving Page: 1");

            String firstPageAddress = "https://www.vitacost.com/productsearch.aspx?t=" + brandName + "&mp=1";

            Document document = Jsoup.connect(firstPageAddress).get();

            log.trace("Retrieving Products for Page: 1");

            List<String> productLinksFromFirstPage = getProductLinks(document);

            productLinks.addAll(productLinksFromFirstPage);

            int pageCount = getPageCount(document, 60);

            for (int i = 2; i <= pageCount; i++) {

                log.trace("Retrieving Page: {}", i);
                String currentPageAddress = firstPageAddress + "&pg=" + i;
                Document currentDocument = Jsoup.connect(currentPageAddress).get();


                log.trace("Retrieving Products for Page: {}", i);
                List<String> currentProductLinks = getProductLinks(currentDocument);

                log.trace("Retrieved Products for Page: {}, Product Count: {}", i, currentProductLinks.size());

                productLinks.addAll(currentProductLinks);

                log.trace("Total Product Count: {} Till Page {} ", productLinks.size(), i);

            }

            return productLinks;

        } catch (IOException e) {
            log.error("Error Getting Product Page for Brand Name: " + brandName + ", Reason: " + e.getMessage(), e);
        }

        return productLinks;
    }

    private List<String> getProductLinks(Document document) {
        Elements itemAnchors = document.select("#resultsForm a[itemprop='name']");

        List<String> itemHrefs = itemAnchors.eachAttr("abs:href");

        return itemHrefs;
    }

    public int getPageCount(Document document, int productsPerPage) {
        Elements pageCountRetrievingElements = document.select("#IamMasterFrameYesIam_ctl02_Pagination_Top_UserControl_srListingResultsText strong");

        if (pageCountRetrievingElements.size() != 2) {

            // TODO: this can also happen for invalid search term, have to take this in account

            log.error("Failed to Find Elements to Retrieve Page Count");
            throw new RuntimeException("Failed to Find Elements to Retrieve Page Count");

        }

        Element pageCountElement = pageCountRetrievingElements.get(1);

        String pageCountText = pageCountElement.text();

        int recordCount = Integer.parseInt(pageCountText);

        // 211 / 6 = 3 (without floating point, as both are int
        // Math.ceil(211 / 60.0) -> 4.0, cast to int
        int pageCount = (int) Math.ceil(recordCount / (double) productsPerPage);

        return pageCount;

    }

}
