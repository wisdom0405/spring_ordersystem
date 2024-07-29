package com.beyond.ordersystem.ordering.Repository;

import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.ordering.domain.Ordering;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderingRepository extends JpaRepository<Ordering,Long> {
    List<Ordering> findByMember(Member member);
}
