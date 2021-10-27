package com.victorlevin.PriceService.repository;

import com.victorlevin.PriceService.domain.Stock;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

import java.util.List;

public interface StockRepository extends KeyValueRepository<Stock, String> {
    List<Stock> findAll();
}
