package com.beyond.ordersystem.member.controller;

import com.beyond.ordersystem.common.auth.JwtTokenProvider;
import com.beyond.ordersystem.common.dto.CommonResDto;
import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.member.dto.MemberLoginDto;
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
import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping("/ordersystem/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MemberController(MemberService memberService,
                            JwtTokenProvider jwtTokenProvider){
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("member/create")
    public ResponseEntity<Object> memberCreate(@Valid @RequestBody MemberSaveReqDto dto){
        Member member = memberService.memberCreate(dto);
        // body에 들어가는 HttpStatus상태
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "memberCreate 성공", member.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED); //header에 들어가는 상태
    }

    @GetMapping("member/list")
    public ResponseEntity<Object> productList(Pageable pageable){
        Page<MemberResDto> memberResDtos = memberService.memberList(pageable);
        // body에 들어가는 HttpStatus상태
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "memberList 조회 성공", memberResDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK); //header에 들어가는 상태
    }

    @PostMapping("/doLogin")
    public ResponseEntity<Object> doLogin(@RequestBody MemberLoginDto dto){
        // email, password가 일치하는지 검증
        Member member = memberService.login(dto);

        // 일치할 경우 accessToken 생성
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString()); // payload에 패스워드 넣으면 안됨(복호화될 수 있는 요소이기 때문)

        // 생성된 토큰을 CommonResDto에 담아 사용자에게 return
        Map<String, Object> logInfo = new HashMap<>();
        logInfo.put("id", member.getId());
        logInfo.put("token", jwtToken);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "login is successful", logInfo);

        return new ResponseEntity<>(commonResDto ,HttpStatus.OK);
    }
}
