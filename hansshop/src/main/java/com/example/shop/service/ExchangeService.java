// Service Example (ExchangeService.java)
package com.example.shop.service;

import com.example.shop.model.Exchange;
import com.example.shop.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    private List<Exchange> exchanges = new ArrayList<>();

    public List<Exchange> getAllExchanges() {
        return exchanges;
    }

    public void cancelExchange(Long exchangeId) {
        exchanges.removeIf(exchange -> exchange.getId().equals(exchangeId));
        exchangeRepository.deleteById(exchangeId);
    }
}