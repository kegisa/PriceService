package com.victorlevin.PriceService.dto;


import com.victorlevin.PriceService.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockFigiesPricesDto {
    private List<Stock> prices;
}
