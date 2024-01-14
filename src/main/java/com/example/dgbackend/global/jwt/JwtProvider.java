package com.example.dgbackend.global.jwt;

import com.example.dgbackend.domain.enums.Role;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.global.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
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
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
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

    /**
     * REFRESH TOKEN 발급
     */
    public String generateRefreshToken(final String id) {
        Claims claims = createClaims(id);
        Date now = new Date();
        SecretKey secretKey = generateKey();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        // Redis에 저장
        redisUtil.setDataExpire(id, refreshToken, Duration.ofDays(7));

        return refreshToken;
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
        Member loginMember = getMemberFromToken(token);

        setContextHolder(token, loginMember);
    }

    private Member getMemberFromToken(String token) {
        String email = getMemberEmailFromToken(token);

        return memberRepository.findByEmail(email).get();
//                .orElseThrow(() -> new ApiException())
    }

    public String getMemberEmailFromToken(String token) {
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
        List<GrantedAuthority> authorities = getAuthoritiesFromMember(loginMember);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginMember, token, authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    // 사용자 권한 가져오기
    private List<GrantedAuthority> getAuthoritiesFromMember(Member member) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        Role role = member.getRole();
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }

    // Access Token 재발급
    public String refreshAccessToken(String refreshToken) {
        String userEmail = getMemberEmailFromToken(refreshToken);

        // Refresh Token의 사용자 정보를 기반으로 새로운 Access Token 발급
        return generateAccessToken(userEmail);
    }

    // Access Token을 Header에서 추출
    public String getJwtTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        log.info("-------------------------Authorization Header: " + authorizationHeader);
        if (authorizationHeader != null) {
            return authorizationHeader;
        }
        return null;
    }


}
