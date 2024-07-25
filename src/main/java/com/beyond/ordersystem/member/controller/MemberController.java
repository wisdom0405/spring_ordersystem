package com.beyond.ordersystem.member.controller;

import com.beyond.ordersystem.common.dto.CommonResDto;
import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.member.dto.MemberResDto;
import com.beyond.ordersystem.member.dto.MemberSaveReqDto;
import com.beyond.ordersystem.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ordersystem/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> memberCreate(@Valid @RequestBody MemberSaveReqDto dto){
        Member member = memberService.memberCreate(dto);
        // body에 들어가는 HttpStatus상태
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "memberCreate 성공", member.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED); //header에 들어가는 상태
    }

    @GetMapping("/list")
    public ResponseEntity<Object> productList(Pageable pageable){
        Page<MemberResDto> memberResDtos = memberService.memberList(pageable);
        // body에 들어가는 HttpStatus상태
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "memberList 조회 성공", memberResDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK); //header에 들어가는 상태
    }

}
