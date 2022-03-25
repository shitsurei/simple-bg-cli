package io.github.shitsurei.common.util;

import io.github.shitsurei.dao.constants.CustomProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 16:14
 */
@Component
@Slf4j
public class JwtUtil {

    @Autowired
    private CustomProperties properties;

    /**
     * 生成token签名
     *
     * @param subject
     * @return
     */
    public String createToken(String subject) {
        Date now = new Date();
        // 过期时间
        Date expireDate = new Date(now.getTime() + properties.getJwtExpire() * 1000);

        //创建Signature SecretKey
        final SecretKey key = Keys.hmacShaKeyFor(properties.getJwtSecret().getBytes(StandardCharsets.UTF_8));

        //header参数
        final Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("algorithm", "HS256");
        headerMap.put("type", "JWT");

        //生成token
        String token = Jwts.builder()
                .setHeader(headerMap)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .setIssuer(properties.getJwtIssuer())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info("生成JWT[" + token + "]");
        return token;
    }


    /**
     * 解析token
     *
     * @param token token
     * @return
     */
    public Claims parseToken(String token) {

        Claims claims;
        try {
            //创建Signature SecretKey
            final SecretKey key = Keys.hmacShaKeyFor(properties.getJwtSecret().getBytes(StandardCharsets.UTF_8));

            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.debug("Parse JWT token success!");
        } catch (JwtException e) {
            log.error("Parse JWT error: " + e.getMessage());
            return null;
        }
        return claims;
    }

    /**
     * 判断token是否过期
     *
     * @param expiration
     * @return
     */
    public boolean isExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
