package com.beyond.ordersystem.member.repository;

import com.beyond.ordersystem.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Page<Member> findAll(Pageable pageable);
    Optional<Member> findByEmail(String email);

}
