package com.profi_shop.security;


import com.profi_shop.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    public static final Logger LOG = LoggerFactory.getLogger(JwtTokenProvider.class);

    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());

        Date expireDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String, Object> claimsMap = new HashMap<>();

        claimsMap.put("id",userId);
        claimsMap.put("username",user.getUsername());
        claimsMap.put("firstname",user.getFirstname());
        claimsMap.put("lastname",user.getLastname());

        return Jwts.builder().setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512,SecurityConstants.SECRET)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SecurityConstants.SECRET));
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException ex){
            LOG.error(ex.getMessage());
            return false;
        }
    }

    public Long getUserIdFromToken(String token){
        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SecurityConstants.SECRET));
        Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();

        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }
}
