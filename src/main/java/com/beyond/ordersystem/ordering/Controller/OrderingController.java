package com.beyond.ordersystem.ordering.Controller;

import com.beyond.ordersystem.common.dto.CommonResDto;
import com.beyond.ordersystem.ordering.Service.OrderingService;
import com.beyond.ordersystem.ordering.domain.Ordering;
import com.beyond.ordersystem.ordering.dto.OrderingListResDto;
import com.beyond.ordersystem.ordering.dto.OrderingSaveReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("ordersystem/ordering/")
public class OrderingController {

    private final OrderingService orderingService;

    @Autowired
    public OrderingController(OrderingService orderingService){
        this.orderingService = orderingService;
    }

    @PostMapping("order/create")
    public ResponseEntity<Object> orderingCreate (@RequestBody OrderingSaveReqDto dto){
        Ordering ordering = orderingService.orderCreate(dto);
        // 엔티티그대로 return할 경우 순환참조에 빠질수있음(OneToMany, ManyToOne 관계걸려있는 경우)
        // 엔티티 그대로 return하고 싶다면 createResDto 만들어서 리턴할 것.
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "정상완료", ordering.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    @GetMapping("order/list")
    public ResponseEntity<Object> orderList(){
        List<OrderingListResDto> orderList = orderingService.orderList();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "정상조회완료", orderList);
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }


}
