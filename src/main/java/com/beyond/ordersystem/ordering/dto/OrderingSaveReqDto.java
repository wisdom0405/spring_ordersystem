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
    @Builder // 객체 안에 리스트가 들어있는 형태이므로 내부클래스로 OrderDetailDto 구현
    public static class OrderDetailDto{
        private Long productId; // 상품id
        private Integer productCount; // 주문상품개수
    }

    public Ordering toEntity(Member member){
        return Ordering.builder()
                .member(member)
//                .orderStatus(OrderStatus.ORDERED)
                .build();
    }

}
