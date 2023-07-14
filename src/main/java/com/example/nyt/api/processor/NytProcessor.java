package com.example.nyt.api.processor;

import com.example.nyt.api.dto.NytResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class NytProcessor implements Processor {

    @Autowired
    ObjectMapper objectMapper;
    @Override
    public void process(Exchange exchange) throws Exception {
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
    }

    private int extractDay(String date){
        return Integer.parseInt(date.split("-")[2].substring(0,2));
    }
}
