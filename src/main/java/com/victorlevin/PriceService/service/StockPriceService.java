package com.victorlevin.PriceService.service;

import com.victorlevin.PriceService.domain.Stock;
import com.victorlevin.PriceService.exception.StockAlreadyExistException;
import com.victorlevin.PriceService.exception.StockNotFoundException;
import com.victorlevin.PriceService.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockPriceService {
    private final StockRepository stockRepository;

    public Stock getPriceByTicker(String ticker) {
        return stockRepository.findById(ticker).orElseThrow(() -> new StockNotFoundException("Stock not found. Try another ticker."));
    }

    public Stock addStock(Stock stock) {
        if(stockRepository.existsById(stock.getTicker())) {
            throw new StockAlreadyExistException("Stock already exist.");
        }

        return stockRepository.save(stock);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
}
