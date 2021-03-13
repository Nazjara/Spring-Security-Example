package com.nazjara.service.service;

import com.nazjara.service.web.model.BeerOrderDto;
import com.nazjara.service.web.model.BeerOrderPagedList;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BeerOrderService {
    BeerOrderPagedList listOrders(UUID customerId, Pageable pageable);

    BeerOrderDto placeOrder(UUID customerId, BeerOrderDto beerOrderDto);

    BeerOrderDto getOrderById(UUID customerId, UUID orderId);

    BeerOrderDto getOrderById(UUID orderId);

    void pickupOrder(UUID customerId, UUID orderId);

    BeerOrderPagedList listOrders(Pageable pageable);
}
