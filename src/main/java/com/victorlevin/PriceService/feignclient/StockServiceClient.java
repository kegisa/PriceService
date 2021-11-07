package com.victorlevin.PriceService.feignclient;

import com.victorlevin.PriceService.dto.StocksDto;
import com.victorlevin.PriceService.dto.StocksWithPrices;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "stockservice", url = "${api.stockConfig.stockService}", configuration = FeignConfig.class)
public interface StockServiceClient {
    @PostMapping("${api.stockConfig.getPrices}")
    StocksWithPrices getPrices(StocksDto stocksDto);
}
