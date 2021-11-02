package com.victorlevin.PriceService.service;

import com.victorlevin.PriceService.domain.Stock;
import com.victorlevin.PriceService.dto.*;
import com.victorlevin.PriceService.exception.StockAlreadyExistException;
import com.victorlevin.PriceService.exception.StockNotFoundException;
import com.victorlevin.PriceService.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockPriceService {
    private final StockRepository stockRepository;
    private final TinkoffPriceService tinkoffPriceService;

    public Stock getPriceByFigi(String figi) {
        return stockRepository.findById(figi).orElseThrow(() -> new StockNotFoundException("Stock not found. Try another figi."));
    }

    public Stock addStock(Stock stock) {
        if(stockRepository.existsById(stock.getFigi())) {
            throw new StockAlreadyExistException("Stock already exist.");
        }

        return stockRepository.save(stock);
    }

    public StockFigiesPricesDto getStocksByFigies(FigiesDto figiesDto) {
        long start = System.currentTimeMillis();
        List<Stock> stocks = new ArrayList<>();
        List<String> figiList = figiesDto.getFigies();

        log.info("Getting from Redis {}", figiList);
        long startGettingFromRedis = System.currentTimeMillis();
        stockRepository.findAllById(figiList).forEach(i -> stocks.add(i));
        log.info("Time getting from Redis - {}", System.currentTimeMillis() - startGettingFromRedis);
        figiList.removeAll(stocks.stream().map(m -> m.getFigi()).collect(Collectors.toList()));
        if(!figiList.isEmpty()) {
            long startTinkoff = System.currentTimeMillis();
            log.info("Getting from TinkoffService {}" , figiList);
            List<Stock> figiPriceFromTinkoff = tinkoffPriceService.getPricesByFigies(figiList);
            log.info("Save to Redis {}", figiPriceFromTinkoff);
            log.info("Time getting from Tinkoff - {}", System.currentTimeMillis() - startTinkoff);
            long startSaveToRedis = System.currentTimeMillis();
            stockRepository.saveAll(figiPriceFromTinkoff);
            stocks.addAll(figiPriceFromTinkoff);
            log.info("Time saving to redis - {}", System.currentTimeMillis() - startSaveToRedis);
        }

        log.info("Time getting stocks - {}", System.currentTimeMillis() - start);
        return new StockFigiesPricesDto(stocks);
    }
}
