package com.beyond.ordersystem.ordering.Controller;

import com.beyond.ordersystem.ordering.Service.OrderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderingController {

    private final OrderingService orderingService;

    @Autowired
    public OrderingController(OrderingService orderingService){
        this.orderingService = orderingService;
    }

    public ResponseEntity<Object> createOrdering (){

        return null;
    }




}
