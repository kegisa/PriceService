package com.victorlevin.PriceService.service;

import com.victorlevin.PriceService.domain.FigiWithPrice;
import com.victorlevin.PriceService.dto.*;
import com.victorlevin.PriceService.exception.StockAlreadyExistException;
import com.victorlevin.PriceService.exception.StockNotFoundException;
import com.victorlevin.PriceService.feignclient.StockServiceClient;
import com.victorlevin.PriceService.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockPriceService {
    private final StockRepository stockRepository;
    private final StockServiceClient stockServiceClient;

    public FigiWithPrice addStock(FigiWithPrice figiWithPrice) {
        if(stockRepository.existsById(figiWithPrice.getFigi())) {
            throw new StockAlreadyExistException("Stock already exist.");
        }

        return stockRepository.save(figiWithPrice);
    }

    public StocksWithPrices getPrices(StocksDto stocksDto) {
        long start = System.currentTimeMillis();
        List<StockWithPrice> result = new ArrayList<>();
        List<Stock> searchStockPrices = new ArrayList<>(stocksDto.getStocks());
        List<StockWithPrice> fromReddis = getFromReddis(searchStockPrices);
        List<String> foundedFigiesFromReddis = fromReddis.stream().map(s -> s.getFigi()).collect(Collectors.toList());
        result.addAll(fromReddis);

        List<Stock> notFoundInRepo = searchStockPrices.stream()
                .filter(s -> !foundedFigiesFromReddis.contains(s.getFigi())).collect(Collectors.toList());

        if(!notFoundInRepo.isEmpty()) {
            List<StockWithPrice> fromApi = getPricesFromApi(notFoundInRepo);
            result.addAll(fromApi);
            saveToRedis(fromApi);
        }

        checkAllOk(searchStockPrices, result);
        log.info("All time - {}", System.currentTimeMillis() - start);
        return new StocksWithPrices(result);
    }

    private List<StockWithPrice> getFromReddis(List<Stock> searchStockPrices) {
        long start = System.currentTimeMillis();
        List<StockWithPrice> stocksFromReddis = new ArrayList<>();
        List<String> figies = searchStockPrices.stream().map(s -> s.getFigi()).collect(Collectors.toList());
        List<FigiWithPrice> foundedFigies = new ArrayList<>();
        log.info("Getting from Redis {}", figies);
        stockRepository.findAllById(figies).forEach(f -> foundedFigies.add(f));
        log.info("Founded figies in Redis {}", foundedFigies);
        if(!foundedFigies.isEmpty()) {
            Map<String, Double> figiWithPrice = foundedFigies.stream()
                    .collect(Collectors.toMap(FigiWithPrice::getFigi, FigiWithPrice::getPrice));

            searchStockPrices.stream()
                    .filter(s -> figiWithPrice.containsKey(s.getFigi()))
                    .forEach(s -> stocksFromReddis.add(new StockWithPrice(s, figiWithPrice.get(s.getFigi()))));
        }
        log.info("Time for getting from Reddis - {}", System.currentTimeMillis() - start);
        return stocksFromReddis;
    }

    private void saveToRedis(List<StockWithPrice> stocks) {
        stockRepository.saveAll(stocks.stream().map(s -> new FigiWithPrice(s.getFigi(), s.getPrice(), s.getSource())).collect(Collectors.toList()));
    }

    private List<StockWithPrice> getPricesFromApi(List<Stock> notFoundInRepo) {
        long start = System.currentTimeMillis();
        StocksDto stocksDto = new StocksDto(notFoundInRepo);
        StocksWithPrices stocksWithPrices = stockServiceClient.getPrices(stocksDto);
        log.info("Time for getting from API - {}", System.currentTimeMillis() - start);
        return stocksWithPrices.getStocks();
    }

    private void checkAllOk(List<Stock> inputStocks, List<StockWithPrice> result) {
        if(inputStocks.size() != result.size()) {
            List<String> foundedStocks = result.stream().map(sp -> sp.getTicker()).collect(Collectors.toList());
            List<Stock> stockNotFound = inputStocks.stream()
                    .filter(s -> !foundedStocks.contains(s.getTicker()))
                    .collect(Collectors.toList());

            throw new StockNotFoundException(String.format("Stocks %s not found", stockNotFound));
        }
    }
}
