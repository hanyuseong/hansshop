// Repository Example (ExchangeRepository.java)
package com.example.shop.repository;

import com.example.shop.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
}
