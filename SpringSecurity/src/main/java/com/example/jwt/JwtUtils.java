package com.example.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtUtils
{
    private static final Logger logger= LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtSecret}")
    private static String jwtSecret;
    @Value("${spring.app.jwtExpirationMS}")
    private String jwtExpirationMS;
    public static String getJwtFromHeader(HttpServletRequest request)
    {
        String bearerToken=request.getHeader("Authorization");
        logger.debug("Authorization Header: {}",bearerToken);
        if(bearerToken!=null && bearerToken.startsWith("Bearer "))
        {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateTokenFromUsername(UserDetails userDetails)
    {
        String username=userDetails.getUsername();
        return Jwts.builder().subject(username).issuedAt(new Date()).expiration(new Date((new Date()).getTime()+jwtExpirationMS)).
                signWith(key()).compact();
    }

    public static String getUsernameFromJwtToken(String token)
    {
        return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload().getSubject();
    }



    private static Key key()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public static boolean validateJwtToken(String authToken)
    {
        try{
            Jwts.parser().verifyWith((SecretKey)key()).build().parseSignedClaims(authToken);
            return true;
        }
        catch (MalformedJwtException e)
        {
            logger.error("Invalid JWT token :{}",e.getMessage());
        }
        catch (ExpiredJwtException e)
        {
            logger.error("Expired JWT token :{}",e.getMessage());
        }
        catch (UnsupportedJwtException e)
        {
            logger.error("JWT token unsupported:{}",e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            logger.error("JWT claims string is empty:{}",e.getMessage());
        }
        return false;
    }
}
