package com.beyond.ordersystem.ordering.Repository;

import com.beyond.ordersystem.ordering.domain.OrderDetail;
import org.hibernate.criterion.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
