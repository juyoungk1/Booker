package com.bookers.backend.domain.book.service;

import com.bookers.backend.domain.book.dto.AladinSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    @Value("${aladin.api.key}")
    private String ttbKey;

    @Value("${aladin.api.url}")
    private String aladinUrl;

    private final RestClient restClient = RestClient.create();

    public List<AladinSearchResponse.Item> searchBooks(String query) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        // 알라딘 API 요청 URL 만들기
        URI uri = UriComponentsBuilder.fromHttpUrl(aladinUrl)
                .queryParam("ttbkey", ttbKey)
                .queryParam("Query", query) // 검색어
                .queryParam("QueryType", "Keyword")
                .queryParam("MaxResults", 10) // 10개만 가져오기
                .queryParam("start", 1)
                .queryParam("SearchTarget", "Book")
                .queryParam("Output", "JS") // JSON 형식으로 받기
                .queryParam("Version", "20131101")
                .build()
                .toUri();

        // 외부 API 호출 (GET)
        AladinSearchResponse response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(AladinSearchResponse.class);

        return response != null ? response.item() : Collections.emptyList();
    }
}