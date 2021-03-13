package com.nazjara.web.controllers.api;

import com.nazjara.security.domain.User;
import com.nazjara.security.permissions.OrderReadPermission2;
import com.nazjara.service.service.BeerOrderService;
import com.nazjara.service.web.model.BeerOrderDto;
import com.nazjara.service.web.model.BeerOrderPagedList;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RequestMapping("/api/v2/")
@RestController
public class BeerOrderController2 {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerOrderService beerOrderService;

    public BeerOrderController2(BeerOrderService beerOrderService) {
        this.beerOrderService = beerOrderService;
    }

    @OrderReadPermission2
    @GetMapping("orders")
    public BeerOrderPagedList listOrders(@AuthenticationPrincipal User user,
                                         @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (user.getCustomer() != null) {
            return beerOrderService.listOrders(user.getCustomer().getId(), PageRequest.of(pageNumber, pageSize));
        } else {
            return beerOrderService.listOrders(PageRequest.of(pageNumber, pageSize));
        }
    }

    @OrderReadPermission2
    @GetMapping("orders/{orderId}")
    public BeerOrderDto getOrder(@PathVariable("orderId") UUID orderId){
        BeerOrderDto beerOrder = beerOrderService.getOrderById(orderId);

        if (beerOrder == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        return beerOrder;
    }
}
