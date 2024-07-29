package com.beyond.ordersystem.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JwtAuthFilter extends GenericFilter {
    // jwt(json web token)를 통한 인증토큰필터

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 토큰은 헤더부에 들어간다.
        String bearerToken = ((HttpServletRequest)request).getHeader("Authorization");

        try{ // AccessToken 검증
            if(bearerToken != null){
                // token은 관례적으로 Bearer로 시작하는 문구를 넣어서 요청
                if(!bearerToken.substring(0,7).equals("Bearer ")){
                    throw new AuthenticationServiceException("Bearer 형식이 아닙니다.");
                }
                String token = bearerToken.substring(7); // 7번째 자리 이후 글자만 잘라낸다.
                // token 검증 및 claims 추출
                // token 생성시에 사용한 secret키 값을 넣어 토큰 검증에 사용한다. (비밀키를 넣어서 암호화 해서 만들어진 암호와 비교해봄)
                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody(); // payLoad에 있는 정보 (이메일, 권한 등..) => 이 한줄로 검증코드가 끝남
                // Authentication 객체 생성 + UserDetails객체도 팔요 (인증정보 들어있음 - 이메일, 권한 등..)

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_"+claims.get("role")));
                // UserDetails (Subject() : email)
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                // Authentication 객체 만드는 이유 : 전역적으로 만들어서 사용하기 위해 만듦 (이 사용자가 누군지 식별하기 위해)
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // filterchain에서 그 다음 filtering으로 넘어가도록 하는 메서드
            chain.doFilter(request,response);
        }catch(Exception e){
            log.error(e.getMessage());
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("token error");
        }
    }
}
