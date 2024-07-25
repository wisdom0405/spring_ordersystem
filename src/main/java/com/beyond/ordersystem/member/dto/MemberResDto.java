package com.beyond.ordersystem.member.dto;

import com.beyond.ordersystem.common.domain.Address;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResDto {
    private Long id;
    private String name;
    private String email;
    private Address address;
}
