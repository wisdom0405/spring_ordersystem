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

        for (OrderingSaveReqDto.OrderDetailDto orderDetailDto : dto.getOrderDetailDtoList()){
            // OrderDetailDto에 딸린 productId를 가지고 product 객체 찾음
            Product product = productRepository.findById(orderDetailDto.getProductId()).orElseThrow(()->new EntityNotFoundException("product is not found"));

            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .ordering(ordering)
                    .quantity(orderDetailDto.getProductCount())
                    .build();
            ordering.getOrderDetails().add(orderDetail);
        }

        Ordering savedOrdering = orderingRepository.save(ordering);

        return savedOrdering;
    }
}
