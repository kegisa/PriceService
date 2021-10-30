package com.victorlevin.PriceService.service;

import com.victorlevin.PriceService.domain.Stock;
import com.victorlevin.PriceService.dto.StockDto;
import com.victorlevin.PriceService.dto.StockPricesDto;
import com.victorlevin.PriceService.dto.TickersDto;
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

    public StockDto getPriceByTicker(String ticker) {
        Stock stock =  stockRepository.findById(ticker).orElseThrow(() -> new StockNotFoundException("Stock not found. Try another ticker."));
        return new StockDto(stock.getTicker(), stock.getPrice());
    }

    public Stock addStock(Stock stock) {
        if(stockRepository.existsById(stock.getTicker())) {
            throw new StockAlreadyExistException("Stock already exist.");
        }

        return stockRepository.save(stock);
    }

    public StockPricesDto getStocksByTickers(TickersDto tickers) {
        List<StockDto> stocks = new ArrayList<>();
        List<String> tickerList = tickers.getTickers();

         stockRepository.findAllById(tickerList)
                 .forEach(stock -> stocks.add(new StockDto(stock.getTicker(), stock.getPrice())));

         tickerList.removeAll(stocks.stream().map(m -> m.getTicker()).collect(Collectors.toList()));
         log.debug("Need to get from TinkoffService {}" , tickerList);
         return new StockPricesDto(stocks);
    }
}
