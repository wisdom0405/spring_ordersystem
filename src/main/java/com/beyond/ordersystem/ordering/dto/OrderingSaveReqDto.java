package com.beyond.ordersystem.ordering.dto;

import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.ordering.Controller.OrderingController;
import com.beyond.ordersystem.ordering.domain.OrderDetail;
import com.beyond.ordersystem.ordering.domain.OrderStatus;
import com.beyond.ordersystem.ordering.domain.Ordering;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderingSaveReqDto {
    private Long memberId;
    private List<OrderDetailDto> orderDetailDtoList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderDetailDto{
        private Long productId; // 상품id
        private Integer productCount; // 주문상품개수
    }

}
