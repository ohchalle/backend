package com.example.ohchallbe.domain.postScrap.dto;

import lombok.Getter;

@Getter
public class PostScrapResponseDto {
    private boolean scraped;

    public PostScrapResponseDto(boolean scraped) {
        this.scraped = scraped;
    }

    public boolean isScraped() {
        return scraped;
    }
}
