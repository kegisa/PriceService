package com.victorlevin.PriceService.repository;

import com.victorlevin.PriceService.domain.Stock;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface StockRepository extends KeyValueRepository<Stock, String> {
}
