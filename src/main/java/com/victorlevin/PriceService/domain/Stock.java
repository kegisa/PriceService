package com.victorlevin.PriceService.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@AllArgsConstructor
@RedisHash(value = "Stock", timeToLive = 60L)
@Data
public class Stock {
    @Id
    private String figi;
    private Double price;
}
