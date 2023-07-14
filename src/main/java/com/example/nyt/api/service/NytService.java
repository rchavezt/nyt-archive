package com.example.nyt.api.service;

import com.example.nyt.api.dto.NytResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("nytService")
public class NytService extends RouteBuilder {

    @Value("${nyt.api.key}")
    String nytApiKey;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void configure() throws Exception {
        from("direct:nytService")
                .process(exchange -> {
                    exchange.setProperty("year", exchange.getIn().getHeader("year"));
                    exchange.setProperty("month", exchange.getIn().getHeader("month"));
                    exchange.setProperty("day", Integer.parseInt(exchange.getIn().getHeader("day").toString()));
                    exchange.setProperty("apiKey", nytApiKey);
                })
                .toD("https://api.nytimes.com/svc/archive/v1/${exchangeProperty.year}/${exchangeProperty.month}.json?api-key=${exchangeProperty.apiKey}&bridgeEndpoint=true")
                .process(exchange -> {
                    var response = objectMapper.readValue(exchange.getMessage().getBody(String.class), java.util.Map.class);
                    List<NytResponse> serviceResponse = new ArrayList<>();
                    for (Map doc : (List<Map>)((Map)response.get("response")).get("docs")) {
                        if (extractDay(doc.get("pub_date").toString()) == (Integer) exchange.getProperty("day")) {
                            var nytResponse = new NytResponse(
                                    ((Map) doc.get("headline")).get("main").toString(),
                                    doc.get("lead_paragraph") != null ? doc.get("lead_paragraph").toString() : null,
                                    doc.get("section_name") != null ? doc.get("section_name").toString() : null,
                                    doc.get("print_page") != null ? doc.get("print_page").toString() : null,
                                    doc.get("pub_date") != null ? doc.get("pub_date").toString() : null
                            );
                            serviceResponse.add(nytResponse);
                        }
                    }
                    exchange.getIn().setBody(serviceResponse);
                });
    }

    private int extractDay(String date){
        return Integer.parseInt(date.split("-")[2].substring(0,2));
    }
}
