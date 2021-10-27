package com.victorlevin.PriceService.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@AllArgsConstructor
@RedisHash(value = "Stock", timeToLive = -1L)
@Data
public class Stock {
    @Id
    private String ticker;
    private Double price;
    private Currency currency;
}
