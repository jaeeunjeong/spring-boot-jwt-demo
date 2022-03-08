package me.toyproject.loginjwt.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * 토큰의 생성, 토큰의 유효성 검증
 */
@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long tokenValidityMilliSecond;

    private Key key;

    public TokenProvider(@Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds){
        this.secret = secret;
        this.tokenValidityMilliSecond = tokenValidityInSeconds * 1000;
    }

    /**
     * 빈이 생성되고 의존성을 주입받은 다음에 base64로 디코딩 한 secret 값을 주입하기 위함.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Authentication에 있는 권한 정보를 이용해서 토큰을 생성한다.
     * 정보를 바탕으로 권한을 가져오고, 유효시간과 암호화를 통해 토큰을 생성한다.
     */
    public String createToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validateTime = new Date(now + this.tokenValidityMilliSecond);

        String newToken = Jwts.builder().setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validateTime).compact();
        return newToken;
    }

    /**
     * 토큰을 이용해서 권한 정보를 리턴하는 메서드
     * 토큰을 이용해서 클레임을 만들고, 클레임에서 권한 정보를 가져와서 유저 객체를 만든다.
     *
     * claim : JWT의 속성 정보
     */
    public Authentication getAuthentication(String token){

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Collection<? extends GrantedAuthority> authorities  = Arrays.stream(
                claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 토큰의 유효성을 검증하기 위함 -> 파싱 후 검증하고 리턴한다.
     */
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token);
            return true;
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            logger.info("잘못된 JWT 서명입니다.");
        }catch (ExpiredJwtException e){
            logger.info("만료된 Token 입니다.");
        }catch (UnsupportedJwtException e){
            logger.info("지원하지 않는 JWT Token 입니다.");
        }catch (MissingClaimException e){
            logger.info("알 수 없는 에러가 발생했습니다.");
        }
        return false;
    }
}
