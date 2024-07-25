package com.beyond.ordersystem.ordering.Service;

import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.member.repository.MemberRepository;
import com.beyond.ordersystem.ordering.Repository.OrderingRepository;
import com.beyond.ordersystem.ordering.domain.OrderDetail;
import com.beyond.ordersystem.ordering.domain.Ordering;
import com.beyond.ordersystem.ordering.dto.OrderingSaveReqDto;
import com.beyond.ordersystem.product.Repository.ProductRepository;
import com.beyond.ordersystem.product.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderingService {
    private final OrderingRepository orderingRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public OrderingService(OrderingRepository orderingRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.orderingRepository = orderingRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Ordering createOrdering(OrderingSaveReqDto dto){
        // memberId에 해당하는 member객체 찾음
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(()->new EntityNotFoundException("member is not found"));

        // Ordering(주문)객체 만듦 => 아직 save 전이라 id가 null인 것처럼 보여질수있음
        Ordering ordering = Ordering.builder()
                .member(member) // 위에서 찾은 member객체
                .orderDetails(new ArrayList<>()) // 아직 아무것도 안들어간 orderDetail 리스트
                .build();

        for (OrderingSaveReqDto.OrderDetailDto orderDetailDto : dto.getOrderDetailDtoList()){ // OrderingSaveReqDto의 orderDetailDto리스트 요소 하나씩 꺼내옴
            // OrderDetailDto에 딸린 productId를 가지고 product 객체 찾음
            Product product = productRepository.findById(orderDetailDto.getProductId()).orElseThrow(()->new EntityNotFoundException("product is not found"));

            OrderDetail orderDetail = OrderDetail.builder() // 주문상세 OrderDetail 객체 조립
                    .product(product)
                    .ordering(ordering)
                    .quantity(orderDetailDto.getProductCount())
                    .build();
            ordering.getOrderDetails().add(orderDetail); // 여기서 이제 orderDetails 리스트에 orderDetail 하나씩 차례로 add해준다.
        }

        Ordering savedOrdering = orderingRepository.save(ordering); // 여기서 save해줘도 jpa에 의해서 선후관계 알아서 맞춰서 처리해주기 때문에 코드의 선후관계 안따져도 OK
        // 이제 Ordering 객체를 save 해줬으므로 Ordering 객체의 id값 나올 것 -> 이후 필요한 요소 jpa가 알아서 처리해준다.
        return savedOrdering;
    }
}
