package com.example.demo;

import java.time.LocalDateTime;

public class CacheObject {
    private String identifier;
    private LocalDateTime date;
    private Integer numberOfSearches;

    CacheObject(String identifier, LocalDateTime date){
        this.identifier = identifier;
        this.date = date;
        this.numberOfSearches = 0;
    }
    public String getIdentifier() { return identifier; }
    public LocalDateTime getDate() { return date; }
    public Integer getNumberOfSearches() { return numberOfSearches; }
    public void updateNumberOfSearches() { numberOfSearches++; }
}
