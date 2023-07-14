package com.example.nyt.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NytResponse {
    private String headline;
    private String summary;
    private String section;
    private String page;
    private String publicationDate;
}
