package com.beyond.ordersystem.common.configs;

import com.beyond.ordersystem.common.auth.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // pre:사전검증 post:사후검증
public class SecurityConfigs {

    @Autowired
    private JwtAuthFilter jwtAuthFilter; // 해당토큰이 정상인지 아닌지 검증하는 필터 (체인 내에서 코드 작성하면 길고 복잡해지므로 따로 파일로 분리해서 작성)

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 필터 체인
        return httpSecurity
                .csrf().disable()
                .cors().and() // CORS활성화 (같은도메인끼리 통신해야 한다. 다른 도메인에서 서버로 호출하는 것을 금지)
                // eg) localhost : 8080 과 localhost:8081 통신불가(오류반환)
                .httpBasic().disable()
                .authorizeRequests()
                    .antMatchers("/member/create", "/", "/doLogin", "/refresh-token")// 예외 url : 회원가입, 홈, 로그인
                    .permitAll() // 나머지는 인증처리
                .anyRequest().authenticated()
                .and()
                // 세션로그인이 아닌 stateless한 token을 사용하겠다라는 의미
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 사용자로부터 받아온 토큰이 정상인지 아닌지를 검증하는 코드
                // 로그인시 사용자는 서버로부터 토큰을 발급받고, 매요청마다 해당 토큰을 http header에 넣어 요청
                // 아래 코드는 사용자로부터 받아온 토큰이 정상인지 아닌지를 검증하는 코드
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // user이름과 비밀번호 비교하는 필터
                .build();
    }
}
