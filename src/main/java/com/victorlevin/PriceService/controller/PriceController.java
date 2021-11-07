package com.victorlevin.PriceService.controller;

import com.victorlevin.PriceService.domain.FigiWithPrice;
import com.victorlevin.PriceService.dto.*;
import com.victorlevin.PriceService.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PriceController {
    private final StockPriceService priceService;

    @PostMapping("/prices")
    public StocksWithPrices getStocksWithPrices(@RequestBody StocksDto stocksDto) {
        return priceService.getPrices(stocksDto);
    }

    @PostMapping("/add")
    public FigiWithPrice addStock(@RequestBody FigiWithPrice figiWithPrice) {
        return priceService.addStock(figiWithPrice);
    }

}
