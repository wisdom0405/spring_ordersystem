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
// memberId받아서 회원을 식별할 필요가 없어졌으므로 OrderDetailDto를 꺼낸다.
public class OrderingSaveReqDto {
//    private Long memberId; // 토큰을 까보면 사용자를 식별할 수 있기 때문에 이제 memberId를 받을 필요없음
//    private List<OrderDetailDto> orderDetailDtoList;

    private Long productId; // 상품id
    private Integer productCount; // 주문상품개수

//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder // 객체 안에 리스트가 들어있는 형태이므로 내부클래스로 OrderDetailDto 구현
//    public static class OrderDetailDto{
//        private Long productId; // 상품id
//        private Integer productCount; // 주문상품개수
//    }

    public Ordering toEntity(Member member){
        return Ordering.builder()
                .member(member)
//                .orderStatus(OrderStatus.ORDERED)
                .build();
    }

}
