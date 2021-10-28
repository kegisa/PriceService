package com.victorlevin.PriceService.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class StockDto {
    private String ticker;
    private Double price;
}
