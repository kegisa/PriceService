package com.victorlevin.PriceService.controller;

import com.victorlevin.PriceService.domain.Stock;
import com.victorlevin.PriceService.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/price")
public class PriceController {
    private final StockPriceService priceService;

    @GetMapping
    public List<Stock> getAllStocks() {
        return priceService.getAllStocks();
    }

    @GetMapping("/{ticker}")
    public Stock getPriceByTicker(@PathVariable String ticker) {
        return priceService.getPriceByTicker(ticker);
    }

    @PostMapping
    public Stock addStock(@RequestBody Stock stock) {
        return priceService.addStock(stock);
    }

}
