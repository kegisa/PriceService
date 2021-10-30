package com.victorlevin.PriceService.controller;

import com.victorlevin.PriceService.domain.Stock;
import com.victorlevin.PriceService.dto.*;
import com.victorlevin.PriceService.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PriceController {
    private final StockPriceService priceService;

    @PostMapping("/prices")
    public StockFigiesPricesDto getStocksByFigies(@RequestBody FigiesDto figiesDto) {
        return priceService.getStocksByFigies(figiesDto);
    }

    @GetMapping("/{figi}")
    public Stock getPriceByTicker(@PathVariable String figi) {
        return priceService.getPriceByFigi(figi);
    }

    @PostMapping("/add")
    public Stock addStock(@RequestBody Stock stock) {
        return priceService.addStock(stock);
    }

}
