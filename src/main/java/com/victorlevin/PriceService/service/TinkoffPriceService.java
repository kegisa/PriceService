package com.victorlevin.PriceService.service;

import com.victorlevin.PriceService.config.ApiConfig;
import com.victorlevin.PriceService.domain.Stock;
import com.victorlevin.PriceService.dto.GetPricesDto;
import com.victorlevin.PriceService.dto.StockFigiesPricesDto;
import com.victorlevin.PriceService.exception.TinkoffServiceException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TinkoffPriceService {
    private final ApiConfig config;

    private final RestTemplate restTemplate;

    public TinkoffPriceService(RestTemplateBuilder restTemplateBuilder, ApiConfig config) {
        this.config = config;
        this.restTemplate = restTemplateBuilder.build();
    }


    public List<Stock> getPricesByFigies(List<String> figies) {
        List<Stock> prices = new ArrayList<>();
        String url = config.getTinkoffService() + config.getGetPricesByFigies();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        GetPricesDto dto = new GetPricesDto(figies);
        HttpEntity<GetPricesDto> entity = new HttpEntity<>(dto, headers);
        ResponseEntity<StockFigiesPricesDto> responseEntity
                = this.restTemplate.postForEntity(url, entity, StockFigiesPricesDto.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            responseEntity.getBody().getPrices().forEach(i -> prices.add(new Stock(i.getFigi(), i.getPrice())));
            return prices;
        } else {
            throw new TinkoffServiceException(responseEntity.toString());
        }
    }
}
