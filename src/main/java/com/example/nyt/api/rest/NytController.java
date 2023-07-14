package com.example.nyt.api.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class NytController extends RouteBuilder {

    @Value("${server.port}")
    String serverPort;

    @Override
    public void configure() throws Exception {
        //http://localhost:8080/nyt/api-doc
        restConfiguration().contextPath("/nyt") //
                .port(serverPort)
                .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Test REST API")
                .apiProperty("api.version", "v1")
                .apiProperty("cors", "true") // cross-site
                .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");


        rest("/archive").consumes(MediaType.APPLICATION_JSON_VALUE).produces(MediaType.APPLICATION_JSON_VALUE)
                .bindingMode(RestBindingMode.json)
                .get("/{year}/{month}/{day}").to("direct:nytService");
    }
}
