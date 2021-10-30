package com.victorlevin.PriceService.controller;

import com.victorlevin.PriceService.domain.Stock;
import com.victorlevin.PriceService.dto.StockDto;
import com.victorlevin.PriceService.dto.StockPricesDto;
import com.victorlevin.PriceService.dto.TickersDto;
import com.victorlevin.PriceService.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/price")
public class PriceController {
    private final StockPriceService priceService;

    @PostMapping("/getByTickers")
    public StockPricesDto getStocksByTickers(@RequestBody TickersDto tickers) {
        return priceService.getStocksByTickers(tickers);
    }

    @GetMapping("/{ticker}")
    public StockDto getPriceByTicker(@PathVariable String ticker) {
        return priceService.getPriceByTicker(ticker);
    }

    @PostMapping("/add")
    public Stock addStock(@RequestBody Stock stock) {
        return priceService.addStock(stock);
    }

}
