package com.example.nyt.api.service;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NytService extends RouteBuilder {

    @Value("${nyt.api.key}")
    String nytApiKey;

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
                .process("nytProcessor");
    }
}
