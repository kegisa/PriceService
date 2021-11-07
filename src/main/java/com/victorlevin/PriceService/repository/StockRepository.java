package com.victorlevin.PriceService.repository;

import com.victorlevin.PriceService.domain.FigiWithPrice;
import org.springframework.data.keyvalue.repository.KeyValueRepository;


public interface StockRepository extends KeyValueRepository<FigiWithPrice, String> {

}
