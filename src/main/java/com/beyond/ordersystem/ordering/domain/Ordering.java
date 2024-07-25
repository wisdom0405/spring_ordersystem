package com.beyond.ordersystem.ordering.domain;

import com.beyond.ordersystem.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Ordering {
    // id, member(1:n관계, ManyToOne), orderStatus(ORDERED, CANCLED), orderdetail(OneToMany)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "ordering", cascade = CascadeType.PERSIST)
//    @Builder.Default
    private List<OrderDetail> orderDetails;

}
