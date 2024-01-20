package com.valleon.pakamapp.config;

import com.valleon.pakamapp.modules.authentication.entity.CustomUserDetails;
import com.valleon.pakamapp.modules.authentication.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtil {

    //    @Value("${app.jwt-secret}")
    private static String SECRET_KEY = "daf66e01593f61a15b857cf433aae03a005812b31234e149036bcc8dee755dbb";

    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private static Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("CustomerCode", userDetails.getUser().getCustomerCode());
        claims.put("CustomerRole", Arrays.toString(userDetails.getUser()
                .getRole()
                .stream()
//                .map((role) -> role.getName())).collect(Collectors.toSet()););
                .map(Role::getName).toList().toArray()));
        claims.put("CustomerEmail", userDetails.getUsername());
        return createToken(claims, userDetails);
    }

    private static String createToken(Map<String, Object> claims, UserDetails userDetail) {

        return Jwts.builder().setClaims(claims)
                .setSubject(userDetail.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public static Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

