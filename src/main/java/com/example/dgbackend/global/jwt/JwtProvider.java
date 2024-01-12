package com.example.dgbackend.global.jwt;

import com.example.dgbackend.domain.enums.Role;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final MemberRepository memberRepository;
    public static final long ACCESS_TOKEN_VALID_TIME = 60 * 60 * 1000L;
    public static final long REFRESH_TOKEN_VALID_TIME = 25 * 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    /**
     * ACCESS TOKEN 발급
     */
    public String generateAccessToken(final String id) {
        Claims claims = createClaims(id);
        Date now = new Date();
        long expiredDate = calculateExpirationDate(now);
        SecretKey secretKey = generateKey();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(expiredDate))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT Claims 생성 (소셜 로그인의 email 저장)
    private Claims createClaims(String id) {
        return Jwts.claims().setSubject(id);
    }

    // JWT 만료 시간 계산
    private long calculateExpirationDate(Date now) {
        return now.getTime() + ACCESS_TOKEN_VALID_TIME;
    }

    // JWT Key 생성
    private SecretKey generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Token 유효성 검사
     */
    public void isValidToken(final String token) {
        try {
            SecretKey secretKey = generateKey();
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
//            throw new ApiException()
        } catch (Exception e) {
//            throw new ApiException()
        }
    }

    // Jwt Token으로 Authentication에 사용자 등록
    public void getAuthenticationFromToken(final String token) {
        log.info("----------------- getAuthenticationFromToken jwt token: " + token);
        Member loginMember = getEmail(token);

        setContextHolder(token, loginMember);
    }

    private Member getEmail(String token) {
        String email = getUserIdFromToken(token);

        return memberRepository.findByEmail(email)
//                .orElseThrow(() -> new ApiException())
    }

    private String getUserIdFromToken(String token) {
        SecretKey secretKey = generateKey();

        // parsing 해서 body값 가져오기
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        log.info("-------------- JwtProvider.getUserIdFromAccessToken: " + claims.getSubject());
        return claims.getSubject();
    }

    // SecurityContextHolder에 등록
    private void setContextHolder(String token, Member loginMember) {
        List<GrantedAuthority> authorities = extractAuthoritiesFromMember(loginMember);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginMember, token, authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    // 사용자 권한 가져오기
    private List<GrantedAuthority> extractAuthoritiesFromMember(Member member) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        Role role = member.getRole();
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }


}
